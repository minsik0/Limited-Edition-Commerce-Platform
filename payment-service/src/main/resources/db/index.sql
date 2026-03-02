CREATE INDEX idx_payments_user_deleted
    ON payments (user_id, deleted_at);

CREATE INDEX idx_payments_user_status_deleted
    ON payments (user_id, status, deleted_at);