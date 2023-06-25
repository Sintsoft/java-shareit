--drop schema if exists shareit CASCADE;

create schema if not exists shareit;

CREATE TABLE IF NOT EXISTS shareit.users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  user_name CHARACTER VARYING NOT NULL,
  email CHARACTER VARYING NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS shareit.requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description CHARACTER VARYING NOT NULL,
  requestor_id BIGINT references shareit.users (id),
  CONSTRAINT pk_request PRIMARY KEY (id)
);

create table if not exists shareit.items (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	user_id BIGINT references shareit.users (id),
	item_name CHARACTER VARYING not null,
	description CHARACTER VARYING,
	available BOOLEAN,
	request_id BIGINT references shareit.requests (id),
	CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS shareit.bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME zone,
  end_date TIMESTAMP WITHOUT TIME zone,
  item_id BIGINT references shareit.items (id),
  booker_id BIGINT references shareit.users (id),
  status CHARACTER VARYING not null,
  CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS shareit.item_comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  comment_text CHARACTER VARYING,
  item_id BIGINT references shareit.items (id),
  author_id BIGINT references shareit.users (id),
  created TIMESTAMP WITHOUT TIME zone,
  CONSTRAINT pk_comment PRIMARY KEY (id)
);