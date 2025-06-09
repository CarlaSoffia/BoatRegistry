CREATE TABLE boat_images (
     id BIGINT NOT NULL AUTO_INCREMENT,
     image LONGBLOB NOT NULL,
     PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE boats ADD COLUMN boat_image_id BIGINT;

ALTER TABLE boats ADD CONSTRAINT fk_boats_boat_image_id FOREIGN KEY (boat_image_id) REFERENCES boat_images(id);
