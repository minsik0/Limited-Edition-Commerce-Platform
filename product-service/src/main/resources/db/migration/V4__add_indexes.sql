CREATE INDEX idx_products_openat
    ON products (open_at DESC);

CREATE INDEX idx_product_options_product_stock
    ON product_options (product_id, remain_stock);