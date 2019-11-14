FROM oracle/graalvm-ce:19.2.1 as graalvm
COPY . /home/app/nhl-season-scraper
WORKDIR /home/app/nhl-season-scraper
RUN gu install native-image
RUN native-image --no-server -cp target/nhl-season-scraper-*.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/nhl-season-scraper .
ENTRYPOINT ["./nhl-season-scraper"]
