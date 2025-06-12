ALTER TABLE boats
ADD CONSTRAINT boats_unique_name_per_user UNIQUE (name, user_id);