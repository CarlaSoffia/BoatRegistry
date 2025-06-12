ALTER TABLE boat_types
    ADD COLUMN user_id BIGINT;

ALTER TABLE boats
    ADD COLUMN user_id BIGINT;

UPDATE boat_types SET user_id = 1;
UPDATE boats SET user_id = 1;
