CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(256) NOT NULL,
    email VARCHAR(256) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT email_pk UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items_requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(1024) NOT NULL,
    requestor BIGINT NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT item_requests_id_pk PRIMARY KEY (id),
    CONSTRAINT requestor_fk FOREIGN KEY (requestor) REFERENCES users
);

CREATE TABLE IF NOT EXISTS items (
   id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(256) NOT NULL,
   description VARCHAR(1024) NOT NULL,
   available BOOLEAN NOT NULL,
   owner_id BIGINT REFERENCES users ON DELETE CASCADE,
   request_id BIGINT REFERENCES items_requests ON DELETE CASCADE,
   CONSTRAINT items_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(32) NOT NULL,
    item_id BIGINT REFERENCES items ON DELETE CASCADE,
    booker_id BIGINT REFERENCES users ON DELETE CASCADE,
    CONSTRAINT booking_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(1024) NOT NULL,
    item_id BIGINT REFERENCES items ON DELETE CASCADE,
    user_id BIGINT REFERENCES users ON DELETE CASCADE,
    created TIMESTAMP NOT NULL
);