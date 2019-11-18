CREATE TABLE nhl.players (
    id bigint not null,
    given_name varchar not null,
    family_name varchar not null,
    number smallint,
    position varchar,
    birth_date date,
    birth_locality varchar,
    birth_region varchar,
    birth_country varchar,
    height smallint,
    weight smallint,
    PRIMARY KEY (id)
);
