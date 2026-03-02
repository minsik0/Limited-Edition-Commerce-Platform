CREATE INDEX idx_products_deleted_at
    ON products (deleted_at);

CREATE INDEX idx_product_options_product_deleted_stock
    ON product_options (product_id, deleted_at, remain_stock);