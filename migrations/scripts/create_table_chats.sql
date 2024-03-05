--liquibase formatted sql
--changeset RuslanAndrianov:created table chats
CREATE TABLE chats (
    chat_id    BIGINT,
    state      chat_state    NOT NULL    DEFAULT 'UNREGISTERED',

    PRIMARY KEY(chat_id)
);
