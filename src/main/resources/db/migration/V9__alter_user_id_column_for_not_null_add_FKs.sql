ALTER TABLE boat_types
    MODIFY COLUMN user_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_boat_types_user_id FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE boats
    MODIFY COLUMN user_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_boats_user_id FOREIGN KEY (user_id) REFERENCES users(id);
