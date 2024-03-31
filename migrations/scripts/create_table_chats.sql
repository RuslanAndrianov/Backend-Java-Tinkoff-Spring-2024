--liquibase formatted sql
--changeset RuslanAndrianov:created table chats
CREATE TABLE chats (
    chat_id        BIGINT,

    PRIMARY KEY(chat_id)
);
