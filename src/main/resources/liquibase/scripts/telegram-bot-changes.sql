--liquibase formatted sql

--changeset bakulin:1
CREATE TABLE notification_task (
   id int CONSTRAINT key_of_table PRIMARY KEY,
   chat_id int,
   bot_message char(250),
   date_of_message TIMESTAMP
);
--changeset bakulin:4
ALTER TABLE chat_id ADD CONSTRAINT chat_name UNIQUE (chat_id);


--changeset bakulin:7
ALTER TABLE notification_task ALTER COLUMN bot_message SET NOT NULL;
ALTER TABLE notification_task ALTER COLUMN chat_id SET NOT NULL;
ALTER TABLE notification_task ALTER COLUMN date_of_message SET NOT NULL;

