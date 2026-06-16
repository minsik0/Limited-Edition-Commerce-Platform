CREATE TABLE payments (
                          payment_id UUID PRIMARY KEY,
                          order_id UUID NOT NULL UNIQUE,
                          user_id UUID NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          amount BIGINT NOT NULL,
                          payment_method VARCHAR(100) NOT NULL,
                          transaction_id VARCHAR(255),
                          approved_at TIMESTAMP,
                          failed_at TIMESTAMP,
                          deleted_at TIMESTAMP
);

CREATE INDEX idx_payments_user_deleted
    ON payments (user_id, deleted_at);

CREATE INDEX idx_payments_user_status_deleted
    ON payments (user_id, status, deleted_at);