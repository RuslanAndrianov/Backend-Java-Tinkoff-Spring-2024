--liquibase formatted sql
--changeset RuslanAndrianov:created table links
CREATE TABLE links (
    link_id         BIGINT,
    url             VARCHAR(2048)    NOT NULL,
    last_updated    TIMESTAMP        NOT NULL,
    last_checked    TIMESTAMP        NOT NULL,
    zone_offset     INTEGER          NOT NULL,
    additional_info VARCHAR(2048),

    PRIMARY KEY(link_id)
);
