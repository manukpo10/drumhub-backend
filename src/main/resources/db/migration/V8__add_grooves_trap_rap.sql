INSERT INTO grooves (slug, title, author_id, genre_id, bpm, level, likes, plays, featured, tags, description, pattern, time_sig, bars)
VALUES
(
  'trap-basic-808',
  'Trap Basic 808',
  100,
  (SELECT id FROM genres WHERE slug = 'trap'),
  140, 'Básico', 312, 1850, FALSE,
  '["trap","808","básico","hihat"]',
  'El patrón de trap más esencial: kick en el 1, snare en el 9, hi-hats cada dos pasos marcando el tiempo.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'trap-hihat-roll',
  'Trap Hi-Hat Roll',
  102,
  (SELECT id FROM genres WHERE slug = 'trap'),
  155, 'Intermedio', 478, 2640, FALSE,
  '["trap","hihat roll","semicorcheas","intermedio"]',
  'Hi-hat en semicorcheas continuas con kick elaborado en posiciones 1, 5 y 13. El roll que define el trap moderno.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0]}',
  '4/4', 1
),
(
  'trap-dark-double-kick',
  'Trap Dark Double Kick',
  103,
  (SELECT id FROM genres WHERE slug = 'trap'),
  145, 'Avanzado', 215, 1420, FALSE,
  '["trap","dark","double kick","avanzado","drill"]',
  'Patrón de trap oscuro con kick doble al final del bar en posiciones 15 y 16. Tensión máxima antes del uno.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1]}',
  '4/4', 1
),
(
  'rap-boom-bap-classic',
  'Boom Bap Classic',
  101,
  (SELECT id FROM genres WHERE slug = 'rap'),
  90, 'Básico', 267, 1560, FALSE,
  '["rap","boom bap","clásico","básico"]',
  'El boom-bap de manual: kick en el 1 y el 9, snare en el 5 y el 13. La columna vertebral del hip-hop clásico.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '4/4', 1
),
(
  'rap-old-school-swing',
  'Old School Hip-Hop Swing',
  103,
  (SELECT id FROM genres WHERE slug = 'rap'),
  95, 'Intermedio', 189, 1090, FALSE,
  '["rap","old school","swing","hihat","intermedio"]',
  'Hip-hop old school con kick elaborado en 1, 3 y 11, y hihat que simula el feel de swing con acentos en las corcheas.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0]}',
  '4/4', 1
),
(
  'rap-aggressive-ghost',
  'Rap Agresivo con Ghost Notes',
  102,
  (SELECT id FROM genres WHERE slug = 'rap'),
  88, 'Avanzado', 143, 820, FALSE,
  '["rap","agresivo","ghost notes","avanzado","battle"]',
  'Patrón de rap agresivo con múltiples kicks, snare en 5 y 13, y ghost notes en posiciones 7 y 15 para añadir textura.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,1,0,0,0,0,0,1,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,1,0,0,0,0,1,0,0,1,0,0,0,0]}',
  '4/4', 1
);
