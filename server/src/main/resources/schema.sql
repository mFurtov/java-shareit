drop table if exists users,items,bookings,requests,comments;
CREATE TABLE IF NOT EXISTS users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(500) UNIQUE NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  is_available BOOLEAN NOT NULL,
  owner_id INTEGER,
  request_id INTEGER,
  CONSTRAINT pk_items PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE,
  end_date TIMESTAMP WITHOUT TIME ZONE,
  item_id INTEGER,
  booker_id INTEGER,
  status VARCHAR,
  CONSTRAINT pk_bookings PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(1000),
  requestor_id INTEGER,
  created TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(1000),
  item_id INTEGER,
  author_id INTEGER,
  created timestamp,
  CONSTRAINT pk_comments PRIMARY KEY (id)
);

ALTER TABLE requests ADD FOREIGN KEY (requestor_id) REFERENCES users (id);

ALTER TABLE items ADD FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE bookings ADD FOREIGN KEY (booker_id) REFERENCES users (id);

ALTER TABLE comments ADD FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE items ADD FOREIGN KEY (request_id) REFERENCES requests (id);

ALTER TABLE comments ADD FOREIGN KEY (id) REFERENCES items (id);

ALTER TABLE bookings ADD FOREIGN KEY (item_id) REFERENCES items (id);

ALTER TABLE comments ADD FOREIGN KEY (item_id) REFERENCES items (id);







