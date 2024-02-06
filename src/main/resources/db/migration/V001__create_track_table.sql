CREATE TABLE IF NOT EXISTS track (
    id              UUID                            PRIMARY KEY,
    artist_id       UUID                            NOT NULL,
    title           TEXT                            NOT NULL,
    album           TEXT                            NOT NULL,
    genre           TEXT                            NOT NULL,
    track_length    BIGINT                          NOT NULL,
    created         TIMESTAMP WITH TIME ZONE        NOT NULL
);