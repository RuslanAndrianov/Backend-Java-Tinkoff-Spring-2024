--liquibase formatted sql
--changeset RuslanAndrianov:created table links
CREATE TABLE links (
    link_id         BIGINT,
    url             VARCHAR(2048)               NOT NULL,
    last_updated    TIMESTAMP WITH TIME ZONE    NOT NULL    DEFAULT '1970-01-01 00:00:00+00',
    last_checked    TIMESTAMP WITH TIME ZONE    NOT NULL    DEFAULT CURRENT_TIMESTAMP,
-- TODO: решить проблему со временем
    PRIMARY KEY(link_id)
);
