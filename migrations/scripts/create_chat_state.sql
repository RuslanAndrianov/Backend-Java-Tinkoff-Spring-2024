--liquibase formatted sql
--changeset RuslanAndrianov:created type chat_state
CREATE TYPE chat_state AS ENUM ('UNREGISTERED', 'REGISTERED', 'TRACKING', 'UNTRACKING');
