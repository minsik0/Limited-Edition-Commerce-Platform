CREATE INDEX idx_orders_user_status
    ON orders (user_id, status);

CREATE INDEX idx_orders_user_created
    ON orders (user_id, created_at);