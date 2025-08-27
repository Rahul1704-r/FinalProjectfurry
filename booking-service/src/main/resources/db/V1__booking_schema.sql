CREATE TABLE IF NOT EXISTS booking (
  id               BIGSERIAL PRIMARY KEY,
  user_email       VARCHAR(255) NOT NULL,
  pet_id           BIGINT       NOT NULL,
  provider_id      INTEGER      NOT NULL,
  service_type_id  INTEGER      NOT NULL,
  start_time       TIMESTAMP    NOT NULL,
  end_time         TIMESTAMP    NOT NULL,
  status           VARCHAR(20)  NOT NULL,
  notes            TEXT,
  created_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_booking_user_email   ON booking(user_email);
CREATE INDEX IF NOT EXISTS idx_booking_provider_id  ON booking(provider_id);
CREATE INDEX IF NOT EXISTS idx_booking_status       ON booking(status);
CREATE INDEX IF NOT EXISTS idx_booking_time_range   ON booking(provider_id, start_time, end_time);