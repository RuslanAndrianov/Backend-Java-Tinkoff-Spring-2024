--liquibase formatted sql
--changeset RuslanAndrianov:created table chats
CREATE TABLE chats (
    chat_id        BIGINT,
    chat_state     varchar(15)    NOT NULL    DEFAULT 'UNREGISTERED',

    PRIMARY KEY(chat_id)
);
