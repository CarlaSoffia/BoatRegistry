ALTER TABLE boat_types
DROP INDEX boat_types_unique_name;

ALTER TABLE boat_types
ADD CONSTRAINT boat_types_unique_name_per_user UNIQUE (name, user_id);