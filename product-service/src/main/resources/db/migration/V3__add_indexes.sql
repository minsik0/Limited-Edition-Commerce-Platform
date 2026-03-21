CREATE INDEX idx_products_deleted_openat
    ON products (deleted_at, open_at DESC);