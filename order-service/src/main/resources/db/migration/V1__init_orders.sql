-- orders
CREATE TABLE orders (
                        order_id UUID PRIMARY KEY,
                        user_id UUID NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        total_price INT NOT NULL,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP,
                        deleted_at TIMESTAMP
);

-- order_items
CREATE TABLE order_items (
                             order_item_id UUID PRIMARY KEY,
                             order_id UUID NOT NULL,
                             product_id UUID NOT NULL,
                             product_name VARCHAR(255) NOT NULL,
                             option_id UUID NOT NULL,
                             option_name VARCHAR(255) NOT NULL,
                             price INT NOT NULL,
                             quantity INT NOT NULL,
                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

CREATE INDEX idx_orders_user_status
    ON orders (user_id, status);

CREATE INDEX idx_orders_user_created
    ON orders (user_id, created_at);