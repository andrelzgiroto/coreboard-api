
CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE employees (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    employee_type VARCHAR(30) NOT NULL
        CHECK (employee_type IN ('ADMIN', 'OPERATOR')),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);


CREATE SEQUENCE service_order_code_seq
    START WITH 1
    INCREMENT BY 1;


CREATE TABLE service_orders (
    id UUID PRIMARY KEY,
    code VARCHAR(15) NOT NULL UNIQUE,
    service_title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    internal_notes TEXT,
    total_value NUMERIC(18,2) NOT NULL,
    service_order_status VARCHAR(30)
        CHECK (service_order_status IN ('OPEN','IN_PROCESS','FINISHED','DELIVERED','CANCELLED')),
    created_at TIMESTAMP(6) NOT NULL,
    finished_at TIMESTAMP(6),
    delivered_at TIMESTAMP(6),
    customer_id UUID NOT NULL,
    assigned_employee_id UUID NOT NULL
);

CREATE TABLE payment_receipts (
    id UUID PRIMARY KEY,
    service_order_id UUID NOT NULL UNIQUE,
    amount NUMERIC(18,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL
        CHECK (payment_method IN ('PIX','DEBIT_CARD','CREDIT_CARD','CASH')),
    notes TEXT,
    created_at TIMESTAMP(6) NOT NULL,
    received_at TIMESTAMP(6) NOT NULL
);


ALTER TABLE service_orders
    ADD CONSTRAINT fk_service_orders_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers (id);

ALTER TABLE service_orders
    ADD CONSTRAINT fk_service_orders_employee
    FOREIGN KEY (assigned_employee_id)
    REFERENCES employees (id);

ALTER TABLE payment_receipts
    ADD CONSTRAINT fk_payment_receipts_service_order
    FOREIGN KEY (service_order_id)
    REFERENCES service_orders (id);


CREATE INDEX idx_so_status
    ON service_orders (service_order_status);

CREATE INDEX idx_service_orders_customer
    ON service_orders (customer_id);

CREATE INDEX idx_service_orders_employee
    ON service_orders (assigned_employee_id);

CREATE INDEX idx_service_orders_created_at
    ON service_orders (created_at);

CREATE INDEX idx_service_orders_finished_at
    ON service_orders (finished_at);

CREATE INDEX idx_payment_receipts_service_order
    ON payment_receipts (service_order_id);

CREATE INDEX idx_payment_receipts_received_at
    ON payment_receipts (received_at);