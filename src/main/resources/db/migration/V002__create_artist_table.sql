CREATE TABLE IF NOT EXISTS artist (
    id                          UUID                            PRIMARY KEY,
    name                        TEXT                            NOT NULL,
    created                     TIMESTAMP WITH TIME ZONE        NOT NULL,
    last_featured_on            DATE
);

CREATE INDEX IF NOT EXISTS artist_last_featured_on_idx on artist (last_featured_on);
CREATE UNIQUE INDEX IF NOT EXISTS artist_name_upper_idx ON artist (UPPER(name));

ALTER TABLE track ADD CONSTRAINT fk_track_artist_id_fkey FOREIGN KEY(artist_id) REFERENCES artist(id);