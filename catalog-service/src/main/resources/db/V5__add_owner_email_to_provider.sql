ALTER TABLE provider ADD COLUMN IF NOT EXISTS owner_email VARCHAR(255);

-- (optional but useful indexes)
CREATE INDEX IF NOT EXISTS idx_provider_owner_email ON provider(owner_email);