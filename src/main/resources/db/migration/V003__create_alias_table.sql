CREATE TABLE IF NOT EXISTS artist_alias (
    id                          UUID                            PRIMARY KEY,
    artist_id                   UUID                            NOT NULL,
    alias                       TEXT                            NOT NULL,
    created                     TIMESTAMP WITH TIME ZONE        NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS artist_alias_upper_idx ON artist_alias (UPPER(alias), artist_id);

ALTER TABLE artist_alias ADD CONSTRAINT fk_alias_artist_id_fkey FOREIGN KEY(artist_id) REFERENCES artist(id);