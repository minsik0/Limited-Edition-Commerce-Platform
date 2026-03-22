-- products
CREATE TABLE products (
                          product_id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price BIGINT NOT NULL,
                          max_purchase_per_user INT NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          open_at TIMESTAMP NOT NULL,
                          deleted_at TIMESTAMP
);

-- product_options
CREATE TABLE product_options (
                                 option_id UUID PRIMARY KEY,
                                 product_id UUID NOT NULL,
                                 version BIGINT,
                                 size VARCHAR(50) NOT NULL,
                                 color VARCHAR(50) NOT NULL,
                                 remain_stock INT NOT NULL,
                                 deleted_at TIMESTAMP,
                                 CONSTRAINT fk_product_options_product
                                     FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- products
CREATE INDEX idx_products_cursor
    ON products (open_at DESC, product_id DESC)
    WHERE deleted_at IS NULL;

-- product_options
CREATE INDEX idx_product_options_product_stock
    ON product_options (product_id, remain_stock);