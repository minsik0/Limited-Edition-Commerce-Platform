CREATE INDEX idx_products_cursor
    ON products (open_at DESC, product_id DESC)
    WHERE deleted_at IS NULL;