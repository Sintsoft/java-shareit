CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(16391) NOT NULL,
  requestor_id BIGINT references users (id),
  created TIMESTAMP WITHOUT TIME zone,
  CONSTRAINT pk_request PRIMARY KEY (id)
);

create table if not exists items (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	user_id BIGINT references users (id),
	item_name VARCHAR(255) not null,
	description VARCHAR(16391) NOT NULL,
	available BOOLEAN,
	request_id BIGINT references requests (id),
	CONSTRAINT pk_item PRIMARY KEY (id)
);