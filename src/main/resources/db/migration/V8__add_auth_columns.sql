ALTER TABLE user_profile ADD COLUMN IF NOT EXISTS email VARCHAR(255);
ALTER TABLE user_profile ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Set default values for existing rows
UPDATE user_profile SET email = user_id || '@placeholder.com' WHERE email IS NULL;
UPDATE user_profile SET password = 'placeholder' WHERE password IS NULL;

-- Add constraints after data is populated
ALTER TABLE user_profile ALTER COLUMN email SET NOT NULL;
ALTER TABLE user_profile ALTER COLUMN password SET NOT NULL;
ALTER TABLE user_profile ADD CONSTRAINT uk_user_profile_email UNIQUE (email);
