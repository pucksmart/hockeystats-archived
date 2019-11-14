FROM oracle/graalvm-ce:19.2.1 as graalvm
COPY . /home/app/nhl-player-scraper
WORKDIR /home/app/nhl-player-scraper
RUN gu install native-image
RUN native-image --no-server -cp target/nhl-player-scraper-*.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/nhl-player-scraper .
ENTRYPOINT ["./nhl-player-scraper"]
