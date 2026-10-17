INSERT INTO games (name, slug, image_url, user_id_label) VALUES
    ('Mobile Legends', 'mobile-legends', NULL, 'User ID + Zone ID'),
    ('Free Fire', 'free-fire', NULL, 'Player ID');

INSERT INTO products (game_id, name, price)
SELECT g.id, p.name, p.price
FROM games g
JOIN (VALUES
    ('mobile-legends', '86 Diamonds', 22000),
    ('mobile-legends', '172 Diamonds', 44000),
    ('mobile-legends', '257 Diamonds', 66000),
    ('free-fire', '70 Diamonds', 10000),
    ('free-fire', '140 Diamonds', 20000)
) AS p (slug, name, price) ON p.slug = g.slug;