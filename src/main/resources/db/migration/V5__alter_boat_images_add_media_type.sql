ALTER TABLE boat_images
ADD COLUMN media_type VARCHAR(50) NOT NULL
CHECK (media_type IN ('image/jpeg', 'image/png'));