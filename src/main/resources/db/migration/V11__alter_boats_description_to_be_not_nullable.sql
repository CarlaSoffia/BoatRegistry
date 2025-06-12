UPDATE boats SET description = '' WHERE description IS NULL;

ALTER TABLE boats MODIFY COLUMN description VARCHAR(255) NOT NULL;