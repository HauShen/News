ALTER TABLE article
ALTER COLUMN content TYPE TEXT;

UPDATE article
SET content = 'INVALID DATA'
WHERE content ~ '^[0-9]+$';

