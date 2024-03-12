--liquibase formatted sql
--changeset RuslanAndrianov:created table chats_to_links
CREATE TABLE chats_to_links (
    chat_id    BIGINT    REFERENCES chats(chat_id)    ON DELETE CASCADE,
    link_id    BIGINT    REFERENCES links(link_id)    ON DELETE CASCADE,

    PRIMARY KEY(chat_id, link_id)
);
