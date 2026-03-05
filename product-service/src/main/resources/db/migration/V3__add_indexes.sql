CREATE INDEX idx_products_deleted_openat
    ON products (deleted_at, open_at DESC);

CREATE INDEX idx_product_options_product_deleted_stock
    ON product_options (product_id, deleted_at, remain_stock);