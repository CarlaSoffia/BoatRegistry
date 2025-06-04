CREATE TABLE boat_types (
        id BIGINT NOT NULL AUTO_INCREMENT,
        name VARCHAR(50) NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT boat_types_unique_name unique (name)
) ENGINE=InnoDB;

CREATE TABLE boats (
       id BIGINT NOT NULL AUTO_INCREMENT,
       name VARCHAR(50) NOT NULL,
       description VARCHAR(255),
       built_year BIGINT NOT NULL,
       length_in_meters DECIMAL(10, 2) NOT NULL,
       width_in_meters DECIMAL(10, 2) NOT NULL,
       boat_type_id BIGINT NOT NULL,
       PRIMARY KEY (id),
       CONSTRAINT fk_boats_boat_type_id FOREIGN KEY (boat_type_id) REFERENCES boat_types(id),
       CONSTRAINT boat_chk_built_year CHECK (built_year > 0),
       CONSTRAINT boat_chk_length_in_meters CHECK (length_in_meters > 0),
       CONSTRAINT boat_chk_width_in_meters CHECK (width_in_meters > 0)
) ENGINE=InnoDB;
