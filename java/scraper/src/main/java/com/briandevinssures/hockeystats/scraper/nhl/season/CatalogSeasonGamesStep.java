package com.briandevinssures.hockeystats.scraper.nhl.season;

import com.briandevinssures.hockeystats.scraper.nhl_api.stats.NhlStatsApi;
import com.briandevinssures.hockeystats.scraper.nhl_api.stats.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CatalogSeasonGamesStep implements ItemReader<Schedule>, ItemProcessor<Schedule, List<NhlGameSummary>>, ItemWriter<List<NhlGameSummary>> {

    private final NhlStatsApi nhlStatsApi;
    private final NhlSeasonRepository nhlSeasonRepository;
    private final NhlGameSummaryRepository nhlGameSummaryRepository;

    private Pageable pageable = PageRequest.of(0, 1, new Sort(Sort.Direction.ASC, "seasonId"));

    CatalogSeasonGamesStep(NhlStatsApi nhlStatsApi, NhlSeasonRepository nhlSeasonRepository, NhlGameSummaryRepository nhlGameSummaryRepository) {
        this.nhlStatsApi = nhlStatsApi;
        this.nhlSeasonRepository = nhlSeasonRepository;
        this.nhlGameSummaryRepository = nhlGameSummaryRepository;
    }

    @Override
    public Schedule read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (pageable != null) {
            Page<NhlSeason> seasons = nhlSeasonRepository.findAll(pageable);
            if (seasons.hasNext()) {
                pageable = seasons.nextPageable();
            } else {
                pageable = null;
            }

            NhlSeason nhlSeason = seasons.get().collect(Collectors.toList()).get(0);
            log.info("{}: {} - {}", nhlSeason.getSeasonId(), nhlSeason.getRegularSeasonStartDate(), nhlSeason.getRegularSeasonEndDate());
            Response<Schedule> scheduleResponse = nhlStatsApi.getSchedule(
                    nhlSeason.getRegularSeasonStartDate().toString(),
                    nhlSeason.getRegularSeasonEndDate().toString()).execute();
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

boolean startsWithAsciiUppercase(String input) {
    if (input == null || input.length() == 0) {
        throw new IllegalArgumentException("input string must be non-null and contain at least 1 character");
    }
    // Handle if the character is unicode, will split the letter A and the accents into separate characters
    // Allocating extra strings of only the first character so we don't allocate the input string a second time
    // Will allocate more memory this way only if the input string is 1 character long
    String normalized = Normalizer.normalize(String.valueOf(input.charAt(0)), Normalizer.Form.NFD);
    char firstChar = normalized.charAt(0);
    return 'A' <= firstChar && firstChar <= 'Z';
}
}
