DROP INDEX IF EXISTS idx_products_deleted_at;
DROP INDEX IF EXISTS idx_product_options_product_deleted_stock;
DROP INDEX IF EXISTS idx_products_deleted_openat;

CREATE INDEX idx_products_cursor
    ON products (open_at DESC, product_id DESC)
    WHERE deleted_at IS NULL;