-- V11: Seed community grooves — 38 grooves across 10 genres (fills each genre to 5 total)
-- Authors: users 200-205 (seeded in V10)
-- Genres confirmed slugs: rock, funk, jazz, reggae, bossa, blues, metal, latin, trap, rap

INSERT INTO grooves (slug, title, author_id, genre_id, bpm, level, likes, plays, featured, tags, description, pattern, time_sig, bars)
VALUES

-- =========================================================
-- ROCK: adds 3 (existing: 2) → total 5
-- =========================================================
(
  'rock-basic-beat',
  'Beat Básico de Rock',
  200,
  (SELECT id FROM genres WHERE slug = 'rock'),
  120, 'Básico', 0, 0, FALSE,
  '["rock","básico","beat","hihat","bombo"]',
  'El padrino de todos los beats. Redoblante en 2 y 4, bombo en 1 y 3, corcheas en el charles.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'rock-open-hihat',
  'Rock con Charles Abierto',
  201,
  (SELECT id FROM genres WHERE slug = 'rock'),
  110, 'Básico', 0, 0, FALSE,
  '["rock","básico","charles abierto","hihat open","70s"]',
  'Variación con charles abierto en los contratiempos. Súper común en el rock de los 70s.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0],"hihat_open":[0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'rock-syncopated-kick',
  'Bombo Sincopado',
  205,
  (SELECT id FROM genres WHERE slug = 'rock'),
  135, 'Intermedio', 0, 0, FALSE,
  '["rock","intermedio","sincopado","bombo","moderno"]',
  'El bombo que corre para adelante y el redoblante que agrega anticipación. Clásico del rock moderno.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0]}',
  '4/4', 1
),

-- =========================================================
-- FUNK: adds 3 (existing: 2) → total 5
-- =========================================================
(
  'funk-ghost-pocket',
  'Pocket con Ghost Notes',
  201,
  (SELECT id FROM genres WHERE slug = 'funk'),
  98, 'Intermedio', 0, 0, FALSE,
  '["funk","intermedio","ghost notes","pocket","semicorcheas"]',
  'Ghost notes en semicorcheas con el bombo sincopado. El corazón del funk moderno.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,1,0,1,0,1,0,0,0,1,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0]}',
  '4/4', 1
),
(
  'funk-16th-groove',
  'Groove en Semicorcheas',
  204,
  (SELECT id FROM genres WHERE slug = 'funk'),
  95, 'Avanzado', 0, 0, FALSE,
  '["funk","avanzado","semicorcheas","groove","detalle"]',
  'Groove denso en semicorcheas con el bombo adelantado. Para los que aman el detalle.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0]}',
  '4/4', 1
),
(
  'funk-simple-pocket',
  'Pocket Sencillo',
  202,
  (SELECT id FROM genres WHERE slug = 'funk'),
  92, 'Básico', 0, 0, FALSE,
  '["funk","básico","pocket","swing","principiante"]',
  'Un groove simple pero con ese swing natural que lo hace bailar. Ideal para empezar en el funk.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1]}',
  '4/4', 1
),

-- =========================================================
-- JAZZ: adds 4 (existing: 1) → total 5
-- =========================================================
(
  'jazz-swing-basic',
  'Swing Básico',
  202,
  (SELECT id FROM genres WHERE slug = 'jazz'),
  120, 'Básico', 0, 0, FALSE,
  '["jazz","básico","swing","ride","hihat"]',
  'El swing más básico. Ride en el patrón de jazz, charles de pie en 2 y 4.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,1,0,0,1,1,0,1,0,0,1,1,0,1,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'jazz-bebop-ride',
  'Ride de Bebop',
  202,
  (SELECT id FROM genres WHERE slug = 'jazz'),
  180, 'Avanzado', 0, 0, FALSE,
  '["jazz","avanzado","bebop","ride","velocidad"]',
  'Patrón de bebop con ride en corcheas y comping de redoblante. Velocidad y precisión.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'jazz-brushes-groove',
  'Groove de Escobillas',
  205,
  (SELECT id FROM genres WHERE slug = 'jazz'),
  100, 'Básico', 0, 0, FALSE,
  '["jazz","básico","escobillas","acústico","íntimo"]',
  'Simulando el barrido de escobillas. Ideal para sets acústicos íntimos.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'jazz-latin-fusion',
  'Fusión Jazz-Latin',
  203,
  (SELECT id FROM genres WHERE slug = 'jazz'),
  130, 'Intermedio', 0, 0, FALSE,
  '["jazz","intermedio","latin","fusion","clave"]',
  'El clave del latin mezclado con el swing del jazz. Fusion pura.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,0,1,0,0,1,0,0,0,1,0,1,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),

-- =========================================================
-- REGGAE: adds 4 (existing: 1) → total 5
-- =========================================================
(
  'reggae-roots',
  'Reggae Roots',
  203,
  (SELECT id FROM genres WHERE slug = 'reggae'),
  70, 'Básico', 0, 0, FALSE,
  '["reggae","básico","roots","contratiempos","paz"]',
  'Roots reggae puro. El bombo en el 3, el charles en los contratiempos. Paz y groove.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'reggae-rockers',
  'Rockers Style',
  203,
  (SELECT id FROM genres WHERE slug = 'reggae'),
  80, 'Intermedio', 0, 0, FALSE,
  '["reggae","intermedio","rockers","bombo","energía"]',
  'Estilo rockers con bombo en todos los tiempos. Más energía que el roots clásico.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'reggae-steppers',
  'Steppers',
  200,
  (SELECT id FROM genres WHERE slug = 'reggae'),
  75, 'Intermedio', 0, 0, FALSE,
  '["reggae","intermedio","steppers","bailable","bombo"]',
  'Steppers reggae: bombo en cada tiempo, corcheas en el charles. Muy bailable.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'reggae-skank-feel',
  'Skank Feel',
  201,
  (SELECT id FROM genres WHERE slug = 'reggae'),
  72, 'Básico', 0, 0, FALSE,
  '["reggae","básico","skank","charles abierto","off-beat"]',
  'El skank del reggae con charles abierto en los offs. Simple y efectivo.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0],"hihat_open":[0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),

-- =========================================================
-- BOSSA: 5 total (all new — check: slug is 'bossa')
-- =========================================================
(
  'bossa-basic-groove',
  'Bossa Básica',
  202,
  (SELECT id FROM genres WHERE slug = 'bossa'),
  130, 'Básico', 0, 0, FALSE,
  '["bossa","básico","bossa nova","bombo","groove"]',
  'La bossa nova más básica. Bombo en 1 y la "y" del 3. El groove que define el género.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),
(
  'bossa-samba-feel',
  'Samba Feel',
  203,
  (SELECT id FROM genres WHERE slug = 'bossa'),
  140, 'Intermedio', 0, 0, FALSE,
  '["bossa","intermedio","samba","energía","charles"]',
  'Bossa con influencia de samba. Más energía, el charles más activo.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,1,0,1,0,1,0,1,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),
(
  'bossa-clave-ride',
  'Clave en el Ride',
  202,
  (SELECT id FROM genres WHERE slug = 'bossa'),
  120, 'Intermedio', 0, 0, FALSE,
  '["bossa","intermedio","clave","ride","son"]',
  'La clave del son en el ride con el patrón bossa en el bombo. Fusión sublime.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,0,1,0,0,1,0,0,0,1,0,1,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),
(
  'bossa-jazz-fusion',
  'Fusión Bossa-Jazz',
  205,
  (SELECT id FROM genres WHERE slug = 'bossa'),
  115, 'Avanzado', 0, 0, FALSE,
  '["bossa","avanzado","jazz","ride","independencia"]',
  'Bossa nova con comping de jazz en el ride. Para músicos con sólida base en ambos géneros.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0]}',
  '4/4', 1
),
(
  'bossa-gentle',
  'Bossa Suave',
  201,
  (SELECT id FROM genres WHERE slug = 'bossa'),
  110, 'Básico', 0, 0, FALSE,
  '["bossa","básico","suave","independencia","principiante"]',
  'Bossa suave y reposada. Ideal para practicar la independencia entre manos y bombo.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),

-- =========================================================
-- BLUES: 5 total (all new)
-- =========================================================
(
  'blues-shuffle-classic',
  'Shuffle Clásico',
  205,
  (SELECT id FROM genres WHERE slug = 'blues'),
  95, 'Básico', 0, 0, FALSE,
  '["blues","básico","shuffle","swing","clásico"]',
  'El shuffle más clásico del blues. El swing en el charles lo dice todo.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'blues-12bar-feel',
  'Feel de 12 Compases',
  205,
  (SELECT id FROM genres WHERE slug = 'blues'),
  88, 'Básico', 0, 0, FALSE,
  '["blues","básico","12 bar","feeling","groove"]',
  'El groove pensado para el blues de 12 compases. Simple, sólido y con feeling.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'blues-slow-burn',
  'Slow Burn',
  203,
  (SELECT id FROM genres WHERE slug = 'blues'),
  60, 'Básico', 0, 0, FALSE,
  '["blues","básico","slow","ride","pesado"]',
  'Blues lento y poderoso. El ride en negras le da esa pesadez hipnótica.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'blues-chicago-style',
  'Estilo Chicago',
  205,
  (SELECT id FROM genres WHERE slug = 'blues'),
  100, 'Intermedio', 0, 0, FALSE,
  '["blues","intermedio","chicago","garra","anticipado"]',
  'El estilo Chicago con más garra. Redoblante anticipado y bombo con variaciones.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,1,0,0,0,0,1,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0]}',
  '4/4', 1
),
(
  'blues-texas-shuffle',
  'Texas Shuffle',
  201,
  (SELECT id FROM genres WHERE slug = 'blues'),
  108, 'Intermedio', 0, 0, FALSE,
  '["blues","intermedio","texas","shuffle","energético"]',
  'El shuffle de Texas: más veloz y con más kick. Perfecto para bandas de blues energéticas.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),

-- =========================================================
-- METAL: 5 total (all new)
-- =========================================================
(
  'metal-double-kick',
  'Double Kick Groove',
  200,
  (SELECT id FROM genres WHERE slug = 'metal'),
  160, 'Avanzado', 0, 0, FALSE,
  '["metal","avanzado","doble bombo","groove","moderno"]',
  'El doble bombo en acción. El patrón base de cualquier baterista de metal moderno.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,1,0,0,0,1,0,1,0,1,0,0,0,1,0]}',
  '4/4', 1
),
(
  'metal-blast-beat',
  'Blast Beat',
  200,
  (SELECT id FROM genres WHERE slug = 'metal'),
  180, 'Avanzado', 0, 0, FALSE,
  '["metal","avanzado","blast beat","extremo","velocidad"]',
  'El blast beat en su forma más pura. Redoblante y bombo en corcheas. Solo para valientes.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0]}',
  '4/4', 1
),
(
  'metal-groove-riff',
  'Groove Metal',
  205,
  (SELECT id FROM genres WHERE slug = 'metal'),
  145, 'Intermedio', 0, 0, FALSE,
  '["metal","intermedio","groove","sincopado","headbang"]',
  'Groove metal con bombo sincopado. El tipo de beat que hace mover la cabeza.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,1,0,0,1,0,1,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'metal-thrash-beat',
  'Thrash Beat',
  200,
  (SELECT id FROM genres WHERE slug = 'metal'),
  175, 'Avanzado', 0, 0, FALSE,
  '["metal","avanzado","thrash","semicorcheas","velocidad"]',
  'Thrash puro. Charles en semicorcheas, bombo en corcheas. Velocidad extrema.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0]}',
  '4/4', 1
),
(
  'metal-half-time-feel',
  'Half Time Metal',
  200,
  (SELECT id FROM genres WHERE slug = 'metal'),
  140, 'Intermedio', 0, 0, FALSE,
  '["metal","intermedio","half time","peso","lentitud"]',
  'Half-time en metal: el redoblante en el 3 le da esa sensación de peso y lentitud.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1]}',
  '4/4', 1
),

-- =========================================================
-- LATIN: 5 total (all new)
-- =========================================================
(
  'latin-son-clave',
  'Son Clave',
  203,
  (SELECT id FROM genres WHERE slug = 'latin'),
  100, 'Básico', 0, 0, FALSE,
  '["latin","básico","son","clave","cubano"]',
  'La clave del son en el ride. La base rítmica de la música cubana.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,0,1,0,0,1,0,0,0,1,0,1,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),
(
  'latin-mambo-pattern',
  'Patrón de Mambo',
  203,
  (SELECT id FROM genres WHERE slug = 'latin'),
  110, 'Intermedio', 0, 0, FALSE,
  '["latin","intermedio","mambo","campana","ride bell"]',
  'El mambo con campana en el ride. La base que hace mover los pies.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"ride_bell":[0,0,0,0,1,0,0,1,0,0,0,0,1,0,0,1],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'latin-songo-groove',
  'Songo',
  202,
  (SELECT id FROM genres WHERE slug = 'latin'),
  120, 'Avanzado', 0, 0, FALSE,
  '["latin","avanzado","songo","jazz","independencia"]',
  'El songo: una fusión compleja de jazz, funk y música cubana. Requiere independencia total.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,1,0,1,0,1,1,0,1,0,1,1,0,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,1,0,0,0,0,1,0,0,0,0,0,1,0]}',
  '4/4', 1
),
(
  'latin-cumbia-beat',
  'Beat de Cumbia',
  201,
  (SELECT id FROM genres WHERE slug = 'latin'),
  95, 'Básico', 0, 0, FALSE,
  '["latin","básico","cumbia","colombiana","bailable"]',
  'La cumbia colombiana en la batería. Bombo cuadrado y redoblante en los contratiempos.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'latin-guaguanco',
  'Guaguancó',
  203,
  (SELECT id FROM genres WHERE slug = 'latin'),
  105, 'Avanzado', 0, 0, FALSE,
  '["latin","avanzado","guaguancó","rumba","afrocaribeño"]',
  'El guaguancó adaptado a la batería. Patrón de la rumba cubana con sabor afrocaribeño.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0]}',
  '4/4', 1
),

-- =========================================================
-- TRAP: adds 2 (existing: 3) → total 5
-- =========================================================
(
  'trap-triplet-hats',
  'Hi-Hats en Trillizos',
  204,
  (SELECT id FROM genres WHERE slug = 'trap'),
  140, 'Avanzado', 0, 0, FALSE,
  '["trap","avanzado","trillizos","hi-hats","flow"]',
  'El flow en trillizos que define el trap moderno. Los hi-hats que cuentan la historia.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,0,1,1,0,1,0,1,1,0,1,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'trap-minimal-808',
  '808 Minimal',
  204,
  (SELECT id FROM genres WHERE slug = 'trap'),
  135, 'Básico', 0, 0, FALSE,
  '["trap","básico","808","minimal","espacio"]',
  'Trap minimalista. El 808 lo dice todo, el resto respeta el espacio.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0]}',
  '4/4', 1
),

-- =========================================================
-- RAP: adds 2 (existing: 3) → total 5
-- =========================================================
(
  'rap-dirty-south',
  'Dirty South',
  204,
  (SELECT id FROM genres WHERE slug = 'rap'),
  75, 'Básico', 0, 0, FALSE,
  '["rap","básico","dirty south","bombo","sur"]',
  'El sonido del rap del sur de Estados Unidos. Bombo anticipado y snare en 2 y 4.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,1,0,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'rap-trap-fusion',
  'Trap-Rap Fusión',
  204,
  (SELECT id FROM genres WHERE slug = 'rap'),
  85, 'Intermedio', 0, 0, FALSE,
  '["rap","intermedio","trap","fusión","hi-hats"]',
  'Cuando el trap y el rap se fusionan. Hi-hats en trillizos sobre un groove de rap clásico.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,0,1,1,0,1,0,1,1,0,1,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0]}',
  '4/4', 1
);

-- =========================================================
-- Date stagger: spread community grooves over the last 60 days
-- Mixed in with existing grooves to appear naturally in feeds
-- =========================================================

-- ROCK community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '57 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '57 days' WHERE slug = 'rock-basic-beat';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '41 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '41 days' WHERE slug = 'rock-open-hihat';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '18 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '18 days' WHERE slug = 'rock-syncopated-kick';

-- FUNK community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '52 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '52 days' WHERE slug = 'funk-ghost-pocket';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '33 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '33 days' WHERE slug = 'funk-16th-groove';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '9 days',  updated_at = CURRENT_TIMESTAMP - INTERVAL '9 days'  WHERE slug = 'funk-simple-pocket';

-- JAZZ community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '59 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '59 days' WHERE slug = 'jazz-swing-basic';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '46 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '46 days' WHERE slug = 'jazz-bebop-ride';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '28 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '28 days' WHERE slug = 'jazz-brushes-groove';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '11 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '11 days' WHERE slug = 'jazz-latin-fusion';

-- REGGAE community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '55 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '55 days' WHERE slug = 'reggae-roots';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '38 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '38 days' WHERE slug = 'reggae-rockers';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '22 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '22 days' WHERE slug = 'reggae-steppers';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '5 days',  updated_at = CURRENT_TIMESTAMP - INTERVAL '5 days'  WHERE slug = 'reggae-skank-feel';

-- BOSSA community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '60 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '60 days' WHERE slug = 'bossa-basic-groove';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '48 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '48 days' WHERE slug = 'bossa-samba-feel';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '35 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '35 days' WHERE slug = 'bossa-clave-ride';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '19 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '19 days' WHERE slug = 'bossa-jazz-fusion';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '7 days',  updated_at = CURRENT_TIMESTAMP - INTERVAL '7 days'  WHERE slug = 'bossa-gentle';

-- BLUES community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '56 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '56 days' WHERE slug = 'blues-shuffle-classic';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '44 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '44 days' WHERE slug = 'blues-12bar-feel';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '30 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '30 days' WHERE slug = 'blues-slow-burn';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '16 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '16 days' WHERE slug = 'blues-chicago-style';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '3 days',  updated_at = CURRENT_TIMESTAMP - INTERVAL '3 days'  WHERE slug = 'blues-texas-shuffle';

-- METAL community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '53 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '53 days' WHERE slug = 'metal-double-kick';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '39 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '39 days' WHERE slug = 'metal-blast-beat';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '25 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '25 days' WHERE slug = 'metal-groove-riff';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '13 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '13 days' WHERE slug = 'metal-thrash-beat';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '2 days',  updated_at = CURRENT_TIMESTAMP - INTERVAL '2 days'  WHERE slug = 'metal-half-time-feel';

-- LATIN community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '58 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '58 days' WHERE slug = 'latin-son-clave';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '43 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '43 days' WHERE slug = 'latin-mambo-pattern';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '29 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '29 days' WHERE slug = 'latin-songo-groove';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '14 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '14 days' WHERE slug = 'latin-cumbia-beat';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '4 days',  updated_at = CURRENT_TIMESTAMP - INTERVAL '4 days'  WHERE slug = 'latin-guaguanco';

-- TRAP community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '36 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '36 days' WHERE slug = 'trap-triplet-hats';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '10 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '10 days' WHERE slug = 'trap-minimal-808';

-- RAP community grooves
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '31 days', updated_at = CURRENT_TIMESTAMP - INTERVAL '31 days' WHERE slug = 'rap-dirty-south';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '8 days',  updated_at = CURRENT_TIMESTAMP - INTERVAL '8 days'  WHERE slug = 'rap-trap-fusion';
