package com.briandevinssures.hockeystats.scraper.nhl.season;

import com.briandevinssures.hockeystats.scraper.nhl_api.stats.NhlStatsApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Schedule;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.AbstractStep;
import org.springframework.batch.item.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//@Component
@Slf4j
public class CatalogSeasonGamesStep extends AbstractStep implements ItemStream, ItemReader<Schedule>, ItemProcessor<Schedule, List<NhlGameSummary>>, ItemWriter<List<NhlGameSummary>> {
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1.5);
    private static final String STARTING_DATE_CONTEXT_KEY = CatalogSeasonGamesStep.class.getName() + "/startingDate";

    private final NhlStatsApi nhlStatsApi;
    private final NhlGameSummaryRepository nhlGameSummaryRepository;

    private LocalDate startingDate;

    CatalogSeasonGamesStep(JobRepository jobRepository, NhlStatsApi nhlStatsApi, NhlSeasonRepository nhlSeasonRepository, NhlGameSummaryRepository nhlGameSummaryRepository) {
        super("catalog-season-games");
        setJobRepository(jobRepository);
        this.nhlStatsApi = nhlStatsApi;
        this.nhlGameSummaryRepository = nhlGameSummaryRepository;
        this.startingDate = nhlSeasonRepository.findAll(PageRequest.of(0, 1, new Sort(Sort.Direction.ASC, "seasonId"))).get().findFirst().get()
                .getRegularSeasonStartDate().minusMonths(1);
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        String possible = (String) executionContext.get(STARTING_DATE_CONTEXT_KEY);
        if (possible != null) {
            startingDate = LocalDate.parse(possible);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        String save = LocalDate.now().toString();
        if (startingDate != null) {
            save = startingDate.toString();
        }
        executionContext.put(STARTING_DATE_CONTEXT_KEY, save);
    }

    @Override
    public void close() throws ItemStreamException {
    }

    @Override
    public Schedule read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        RATE_LIMITER.acquire();
        if (startingDate != null) {
            LocalDate endingDate = startingDate.plusWeeks(1).minusDays(1);
            log.info("{} - {}", startingDate, endingDate);
            Response<Schedule> scheduleResponse = nhlStatsApi.getSchedule(
                    startingDate.toString(),
                    endingDate.toString()).execute();
            startingDate = startingDate.plusWeeks(1);
            if (startingDate.isAfter(LocalDate.now())) {
                startingDate = null;
            }
            return scheduleResponse.body();
        }
        return null;
    }

    @Override
    public List<NhlGameSummary> process(Schedule item) throws Exception {
        return item.getDates().stream().flatMap(d -> d.getGames().stream())
                .map(g -> new NhlGameSummary()
                        .setGameId(g.getGamePk())
                        .setGameType(g.getGameType())
                        .setSeasonId(g.getSeason())
                        .setGameDate(g.getGameDate())
                        .setGameState(g.getStatus().getDetailedState())
                        .setVenue(g.getVenue().getName())
                        .setAwayTeamId(g.getTeams().getAway().getTeam().getId())
                        .setAwayScore(g.getTeams().getAway().getScore())
                        .setHomeTeamId(g.getTeams().getHome().getTeam().getId())
                        .setHomeScore(g.getTeams().getHome().getScore()))
                .collect(Collectors.toList());
    }

    @Override
    public void write(List<? extends List<NhlGameSummary>> items) throws Exception {
        items.forEach(nhlGameSummaryRepository::saveAll);
    }

    @Override
    protected void doExecute(StepExecution stepExecution) throws Exception {
        update(stepExecution.getExecutionContext());

        Schedule current = null;
        while ((current = read()) != null) {
            List<NhlGameSummary> games = process(current);
            write(Collections.singletonList(games));
            update(stepExecution.getExecutionContext());
        }
    }
}
