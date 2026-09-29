-- ============================================================
-- Seed 30 sample bookings
--
-- Status distribution:
--   PENDING   : 10
--   CONFIRMED : 12
--   CANCELLED : 8
--
-- Assumptions:
--   users 2, 3, 4 already exist
--   tour_departures contains enough seed data
-- ============================================================


-- ============================================================
-- PENDING (10)
-- ============================================================

INSERT INTO bookings (
    departure_id,
    user_id,
    code,
    full_name,
    email,
    phone,
    adults,
    children,
    total_price,
    status,
    created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00001',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000001',
    2,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'PENDING',
    NOW() - INTERVAL 1 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 0;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00002',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000002',
    2,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'PENDING',
    NOW() - INTERVAL 2 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 1;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00003',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000003',
    1,
    0,
    COALESCE(td.price, t.discount_price, t.price),
    'PENDING',
    NOW() - INTERVAL 3 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 2;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00004',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000004',
    3,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'PENDING',
    NOW() - INTERVAL 4 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 3;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00005',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000005',
    2,
    2,
    COALESCE(td.price, t.discount_price, t.price) * 4,
    'PENDING',
    NOW() - INTERVAL 5 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 4;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00006',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000006',
    2,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'PENDING',
    NOW() - INTERVAL 6 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 5;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00007',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000007',
    1,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'PENDING',
    NOW() - INTERVAL 7 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 6;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00008',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000008',
    4,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 4,
    'PENDING',
    NOW() - INTERVAL 8 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 7;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00009',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000009',
    2,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'PENDING',
    NOW() - INTERVAL 9 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 8;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00010',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000010',
    2,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'PENDING',
    NOW() - INTERVAL 10 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 9;


-- ============================================================
-- CONFIRMED (12)
-- ============================================================

INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00011',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000011',
    2,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'CONFIRMED',
    NOW() - INTERVAL 11 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 0;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00012',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000012',
    2,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'CONFIRMED',
    NOW() - INTERVAL 12 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 1;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00013',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000013',
    1,
    0,
    COALESCE(td.price, t.discount_price, t.price),
    'CONFIRMED',
    NOW() - INTERVAL 13 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 2;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00014',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000014',
    3,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 4,
    'CONFIRMED',
    NOW() - INTERVAL 14 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 3;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00015',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000015',
    2,
    2,
    COALESCE(td.price, t.discount_price, t.price) * 4,
    'CONFIRMED',
    NOW() - INTERVAL 15 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 4;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00016',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000016',
    2,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'CONFIRMED',
    NOW() - INTERVAL 16 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 5;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00017',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000017',
    1,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'CONFIRMED',
    NOW() - INTERVAL 17 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 6;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00018',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000018',
    3,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'CONFIRMED',
    NOW() - INTERVAL 18 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 7;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00019',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000019',
    2,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'CONFIRMED',
    NOW() - INTERVAL 19 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 8;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00020',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000020',
    2,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'CONFIRMED',
    NOW() - INTERVAL 20 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 9;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00021',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000021',
    4,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 4,
    'CONFIRMED',
    NOW() - INTERVAL 21 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 0;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00022',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000022',
    2,
    2,
    COALESCE(td.price, t.discount_price, t.price) * 4,
    'CONFIRMED',
    NOW() - INTERVAL 22 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 1;


-- ============================================================
-- CANCELLED (8)
-- CANCELLED bookings do NOT consume capacity.
-- ============================================================

INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00023',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000023',
    2,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'CANCELLED',
    NOW() - INTERVAL 23 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 2;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00024',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000024',
    1,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'CANCELLED',
    NOW() - INTERVAL 24 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 3;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00025',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000025',
    2,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'CANCELLED',
    NOW() - INTERVAL 25 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 4;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00026',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000026',
    3,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'CANCELLED',
    NOW() - INTERVAL 26 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 5;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00027',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000027',
    2,
    0,
    COALESCE(td.price, t.discount_price, t.price) * 2,
    'CANCELLED',
    NOW() - INTERVAL 27 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 6;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    2,
    'TG-2026-A00028',
    'Nguyễn Văn An',
    'an@example.com',
    '0901000028',
    1,
    0,
    COALESCE(td.price, t.discount_price, t.price),
    'CANCELLED',
    NOW() - INTERVAL 28 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 7;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    3,
    'TG-2026-A00029',
    'Trần Thị Bình',
    'binh@example.com',
    '0901000029',
    2,
    2,
    COALESCE(td.price, t.discount_price, t.price) * 4,
    'CANCELLED',
    NOW() - INTERVAL 29 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 8;


INSERT INTO bookings (
    departure_id, user_id, code,
    full_name, email, phone,
    adults, children,
    total_price, status, created_at
)
SELECT
    td.id,
    4,
    'TG-2026-A00030',
    'Lê Minh Châu',
    'chau@example.com',
    '0901000030',
    2,
    1,
    COALESCE(td.price, t.discount_price, t.price) * 3,
    'CANCELLED',
    NOW() - INTERVAL 30 DAY
FROM tour_departures td
    JOIN tours t ON t.id = td.tour_id
ORDER BY td.id
    LIMIT 1 OFFSET 9;
