CREATE TABLE IF NOT EXISTS notification (
  id BIGSERIAL PRIMARY KEY,
  user_email VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  message TEXT NOT NULL,
  type VARCHAR(50) NOT NULL,    -- BOOKING_CREATED / ACCEPTED / COMPLETED / CANCELLED
  booking_id BIGINT,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  read BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_notification_user_email ON notification(user_email);
CREATE INDEX IF NOT EXISTS idx_notification_created_at ON notification(created_at);