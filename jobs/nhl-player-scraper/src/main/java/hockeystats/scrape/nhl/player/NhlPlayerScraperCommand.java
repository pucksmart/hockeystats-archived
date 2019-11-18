package hockeystats.scrape.nhl.player;

import io.micronaut.configuration.picocli.PicocliRunner;
import javax.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "nhl-player-scraper", description = "...",
    mixinStandardHelpOptions = true)
public class NhlPlayerScraperCommand implements Runnable {

  public static void main(String[] args) throws Exception {
    PicocliRunner.run(NhlPlayerScraperCommand.class, args);
  }

  @Option(names = {"-v", "--verbose"}, description = "...")
  boolean verbose;

  @Inject
  public Job job;

  public void run() {
    job.run();
  }
}
