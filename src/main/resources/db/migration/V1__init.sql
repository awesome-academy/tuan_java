-- =========================================================
-- V1__init.sql
-- Initial database schema
-- =========================================================


-- =========================================================
-- Independent tables
-- =========================================================

CREATE TABLE destinations (
    id        BIGINT NOT NULL AUTO_INCREMENT,
    slug      VARCHAR(255),
    name      VARCHAR(255),
    image     VARCHAR(255),

    CONSTRAINT pk_destinations
        PRIMARY KEY (id),

    CONSTRAINT uk_destinations_slug
        UNIQUE (slug)
);

CREATE TABLE categories (
    id      BIGINT NOT NULL AUTO_INCREMENT,
    slug    VARCHAR(50) NOT NULL,
    name    VARCHAR(100) NOT NULL,

    CONSTRAINT pk_categories
        PRIMARY KEY (id),

    CONSTRAINT uk_categories_slug
        UNIQUE (slug)
);

CREATE TABLE users (
    id            BIGINT NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role          ENUM ('ADMIN', 'USER') NOT NULL,

    CONSTRAINT pk_users
        PRIMARY KEY (id),

    CONSTRAINT uk_users_email
        UNIQUE (email)
);


-- =========================================================
-- Tours
-- =========================================================

CREATE TABLE tours (
    id             BIGINT NOT NULL AUTO_INCREMENT,
    destination_id BIGINT NOT NULL,

    title           VARCHAR(255),
    slug            VARCHAR(255),
    category_id     BIGINT NOT NULL,
    description     TEXT,
    thumbnail       VARCHAR(255),

    price           DECIMAL(19, 2) NOT NULL,
    discount_price  DECIMAL(19, 2),

    duration_days   INTEGER NOT NULL,
    max_group_size  INTEGER NOT NULL,

    rating          DECIMAL(2, 1) NOT NULL,
    review_count    INTEGER NOT NULL,

    is_featured     BOOLEAN NOT NULL DEFAULT FALSE,

    created_at      DATETIME(6) NOT NULL,

    CONSTRAINT pk_tours
        PRIMARY KEY (id),

    CONSTRAINT uk_tours_slug
        UNIQUE (slug),

    CONSTRAINT fk_tours_destination
        FOREIGN KEY (destination_id)
        REFERENCES destinations (id),

    CONSTRAINT fk_tours_category
        FOREIGN KEY (category_id)
        REFERENCES categories (id),

    CONSTRAINT chk_tours_rating
        CHECK (rating BETWEEN 0.0 AND 5.0)
);


-- =========================================================
-- Tour child tables
-- =========================================================

CREATE TABLE tour_images (
    tour_id   BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,

    CONSTRAINT fk_images_tour
        FOREIGN KEY (tour_id)
        REFERENCES tours (id),

    CONSTRAINT uk_images_tour_file
        UNIQUE (tour_id, file_name)
);


CREATE TABLE tour_itineraries (
    tour_id      BIGINT NOT NULL,
    day_number   INTEGER NOT NULL,
    title        VARCHAR(255),
    description  VARCHAR(255),

    CONSTRAINT fk_itineraries_tour
        FOREIGN KEY (tour_id)
        REFERENCES tours (id),

    CONSTRAINT uk_itineraries_tour_day
        UNIQUE (tour_id, day_number)
);


CREATE TABLE tour_departures (
    id         BIGINT NOT NULL AUTO_INCREMENT,
    tour_id    BIGINT NOT NULL,
    start_date DATE NOT NULL,
    price      DECIMAL(19, 2),

    CONSTRAINT pk_tour_departures
        PRIMARY KEY (id),

    CONSTRAINT fk_tour_departures_tour
        FOREIGN KEY (tour_id)
        REFERENCES tours (id),

    CONSTRAINT uk_tour_departures_tour_date
        UNIQUE (tour_id, start_date)
);


-- =========================================================
-- Reviews
-- =========================================================

CREATE TABLE reviews (
    id         BIGINT NOT NULL AUTO_INCREMENT,
    tour_id    BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,

    rating     INTEGER NOT NULL,
    comment    TEXT,
    created_at DATETIME(6),

    CONSTRAINT pk_reviews
        PRIMARY KEY (id),

    CONSTRAINT fk_reviews_tour
        FOREIGN KEY (tour_id)
        REFERENCES tours (id),

    CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id)
        REFERENCES users (id),

    CONSTRAINT chk_reviews_rating
        CHECK (rating BETWEEN 1 AND 5)
);


-- =========================================================
-- Bookings
-- =========================================================

CREATE TABLE bookings (
    id           BIGINT NOT NULL AUTO_INCREMENT,
    departure_id BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,

    code        VARCHAR(32) NOT NULL,
    full_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    phone       VARCHAR(255) NOT NULL,

    adults      INTEGER NOT NULL,
    children    INTEGER NOT NULL,

    total_price DECIMAL(19, 2) NOT NULL,

    status      ENUM ('CANCELLED', 'CONFIRMED', 'PENDING') NOT NULL,
    created_at  DATETIME(6) NOT NULL,

    CONSTRAINT pk_bookings
        PRIMARY KEY (id),

    CONSTRAINT uk_bookings_code
        UNIQUE (code),

    CONSTRAINT fk_bookings_tour_departure
        FOREIGN KEY (departure_id)
        REFERENCES tour_departures (id),

    CONSTRAINT fk_bookings_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
);
