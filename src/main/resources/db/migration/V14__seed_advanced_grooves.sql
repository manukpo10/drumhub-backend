-- V14: Seed advanced grooves — 12 grooves across multiple kits and time signatures
-- Authors: users 202 (carovillareal), 206 (martinr909), 207 (diegopercusion),
--          208 (valentinasoul), 209 (andrealm2)
-- Kits: tr909, lm2, cr78, pearl
-- Time signatures: 4/4, 3/4, 5/4, 6/8, 7/8
-- All pattern arrays validated: stepsPerBar × bars elements per key

INSERT INTO grooves (slug, title, author_id, genre_id, bpm, level, likes, plays, featured, tags, description, pattern, time_sig, bars, kit)
VALUES

-- =========================================================
-- 1. Techno Four on the Floor — TR-909, 4/4, 2 bars → 32 steps
-- kit: tr909 extra key: clap
-- =========================================================
(
  'techno-four-on-the-floor',
  'Techno Four on the Floor',
  206,
  (SELECT id FROM genres WHERE slug = 'trap'),
  138, 'Básico', 0, 0, FALSE,
  '["trap","techno","tr909","four-on-the-floor","electrónica"]',
  'El four-on-the-floor del TR-909 que definió el techno europeo. Bombo en todos los tiempos, hi-hats en corcheas.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1],"snare":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0],"clap":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0]}',
  '4/4', 2, 'tr909'
),

-- =========================================================
-- 2. Acid House 909 — TR-909, 4/4, 1 bar → 16 steps
-- kit: tr909 extra key: clap
-- =========================================================
(
  'acid-house-tr909',
  'Acid House 909',
  206,
  (SELECT id FROM genres WHERE slug = 'pop'),
  128, 'Básico', 0, 0, FALSE,
  '["pop","acid house","tr909","electrónica","chicago"]',
  'El beat de la acid house de Chicago. Hi-hats en semicorcheas, clap en 2 y 4, bombo sincopado.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1],"snare":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0],"clap":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0]}',
  '4/4', 1, 'tr909'
),

-- =========================================================
-- 3. LinnDrum 80s Pop — LM-2, 4/4, 2 bars → 32 steps
-- kit: lm2 extra keys: clap, stick, cowbell, cabasa, tamb, conga
-- =========================================================
(
  'linndrum-80s-pop',
  'LinnDrum 80s Pop',
  209,
  (SELECT id FROM genres WHERE slug = 'pop'),
  112, 'Básico', 0, 0, FALSE,
  '["pop","linndrum","lm2","80s","cowbell"]',
  'El sonido que definió los 80s. El LM-2 con su cowbell inconfundible y el clap pegando fuerte en 2 y 4.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0],"clap":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"stick":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"cowbell":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1],"cabasa":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tamb":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"conga":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}',
  '4/4', 2, 'lm2'
),

-- =========================================================
-- 4. Boom Bap LM-2 — LM-2, 4/4, 1 bar → 16 steps
-- kit: lm2 extra keys: clap, stick, cowbell, cabasa, tamb, conga
-- =========================================================
(
  'linndrum-boom-bap',
  'Boom Bap LM-2',
  209,
  (SELECT id FROM genres WHERE slug = 'hip-hop'),
  88, 'Básico', 0, 0, FALSE,
  '["hip-hop","boom bap","linndrum","lm2","clásico"]',
  'El boom bap del LinnDrum. El clap apilado con el snare le da ese punch característico del hip-hop clásico.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0],"clap":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"stick":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"cowbell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"cabasa":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tamb":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"conga":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}',
  '4/4', 1, 'lm2'
),

-- =========================================================
-- 5. Funk Groove LM-2 — LM-2, 4/4, 2 bars → 32 steps
-- kit: lm2 extra keys: clap, stick, cowbell, cabasa, tamb, conga
-- =========================================================
(
  'linndrum-funk-groove',
  'Funk Groove LM-2',
  209,
  (SELECT id FROM genres WHERE slug = 'funk'),
  96, 'Avanzado', 0, 0, FALSE,
  '["funk","linndrum","lm2","semicorcheas","conga"]',
  'El LM-2 en modo funk: semicorcheas densas, conga en el fondo y clap sincopado. Difícil de tocar, imposible de ignorar.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],"snare":[0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,1,0,0,1,0,0,0,0],"clap":[0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0],"stick":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"cowbell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"cabasa":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tamb":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"conga":[0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0]}',
  '4/4', 2, 'lm2'
),

-- =========================================================
-- 6. Disco CR-78 — CR-78, 4/4, 1 bar → 16 steps
-- kit: cr78 extra key: stick
-- =========================================================
(
  'cr78-disco-groove',
  'Disco CR-78',
  208,
  (SELECT id FROM genres WHERE slug = 'soul'),
  120, 'Básico', 0, 0, FALSE,
  '["soul","disco","cr78","70s","four-on-the-floor"]',
  'El CR-78 que se escucha en los temas de disco de los 70s. Four-on-the-floor con el open hat en los contratiempos.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1],"snare":[0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0],"stick":[0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0]}',
  '4/4', 1, 'cr78'
),

-- =========================================================
-- 7. Jazz Waltz 3/4 — pearl, 3/4, 2 bars → 24 steps
-- stepsPerBar = 12 for 3/4; 12 × 2 = 24
-- kit: pearl (standard 12 keys only)
-- =========================================================
(
  'jazz-waltz-3-4',
  'Jazz Waltz 3/4',
  202,
  (SELECT id FROM genres WHERE slug = 'jazz'),
  165, 'Avanzado', 0, 0, FALSE,
  '["jazz","waltz","3/4","swing","ride"]',
  'El vals de jazz en dos compases completos. El ride lleva el swing, el pie en el 2, y el redoblante comping libre.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,1,0,0,1,1,0,1,0,0,1,1,0,1,0,0,1,1,0,1,0,0,1],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0]}',
  '3/4', 2, 'pearl'
),

-- =========================================================
-- 8. Bossa en 3/4 — pearl, 3/4, 1 bar → 12 steps
-- stepsPerBar = 12 for 3/4; 12 × 1 = 12
-- kit: pearl (standard 12 keys only)
-- =========================================================
(
  'bossa-3-4-groove',
  'Bossa en 3/4',
  202,
  (SELECT id FROM genres WHERE slug = 'bossa'),
  125, 'Intermedio', 0, 0, FALSE,
  '["bossa","3/4","waltz","jazz","clave"]',
  'La bossa nova adaptada al 3/4. El bombo y la clave se reorganizan para crear un balanceo diferente y muy musical.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0],"ride":[1,0,0,1,0,0,1,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[0,0,0,0,1,0,0,0,0,0,0,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,0,0,1,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0]}',
  '3/4', 1, 'pearl'
),

-- =========================================================
-- 9. Progresivo 5/4 — pearl, 5/4, 1 bar → 20 steps
-- stepsPerBar = 20 for 5/4; 20 × 1 = 20
-- kit: pearl (standard 12 keys only)
-- =========================================================
(
  'prog-5-4-metal',
  'Progresivo 5/4',
  207,
  (SELECT id FROM genres WHERE slug = 'metal'),
  140, 'Avanzado', 0, 0, FALSE,
  '["metal","5/4","progresivo","odd time","técnica"]',
  'El 5/4 que no descansa. Agrupación 2+3: el bombo ancla los tiempos 1 y 4, el redoblante en 3 y 5. Requiere concentración total.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],"snare":[0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]}',
  '5/4', 1, 'pearl'
),

-- =========================================================
-- 10. 7/8 Odd Time — pearl, 7/8, 1 bar → 14 steps
-- stepsPerBar = 14 for 7/8; 14 × 1 = 14
-- kit: pearl (standard 12 keys only)
-- =========================================================
(
  'odd-time-7-8',
  '7/8 Odd Time',
  207,
  (SELECT id FROM genres WHERE slug = 'metal'),
  160, 'Avanzado', 0, 0, FALSE,
  '["metal","7/8","odd time","progresivo","técnica avanzada"]',
  'El 7/8 con agrupación 2+2+3. Cada grupo empieza con bombo, el redoblante en las "caídas" y el ride pinta el final del compás.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,1,0,0,1],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,1,0,0,0,1,0,0,0,0,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,1,0,0,0,1,0,0,0,0,0]}',
  '7/8', 1, 'pearl'
),

-- =========================================================
-- 11. Balada Blues 6/8 — pearl, 6/8, 1 bar → 12 steps
-- stepsPerBar = 12 for 6/8; 12 × 1 = 12
-- kit: pearl (standard 12 keys only)
-- =========================================================
(
  'blues-ballad-6-8',
  'Balada Blues 6/8',
  208,
  (SELECT id FROM genres WHERE slug = 'blues'),
  68, 'Básico', 0, 0, FALSE,
  '["blues","6/8","balada","slow","feeling"]',
  'El 6/8 de las baladas de blues. Seis pulsos de corchea que se sienten como dos tiempos grandes. Lento, pesado, emotivo.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0],"crash":[1,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,1,0,1,0,1,0,1,0],"hihat_open":[0,0,0,0,0,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,1,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,1,0,0,0,0,0]}',
  '6/8', 1, 'pearl'
),

-- =========================================================
-- 12. Vals CR-78 — CR-78, 3/4, 1 bar → 12 steps
-- stepsPerBar = 12 for 3/4; 12 × 1 = 12
-- kit: cr78 extra key: stick
-- =========================================================
(
  'cr78-waltz-jazz',
  'Vals CR-78',
  208,
  (SELECT id FROM genres WHERE slug = 'jazz'),
  130, 'Básico', 0, 0, FALSE,
  '["jazz","3/4","cr78","waltz","vintage"]',
  'El CR-78 en 3/4. El stick marca el "um-pa-pa" y el hihat abierto respira en los contratiempos.',
  '{"china":[0,0,0,0,0,0,0,0,0,0,0,0],"crash":[0,0,0,0,0,0,0,0,0,0,0,0],"splash":[0,0,0,0,0,0,0,0,0,0,0,0],"ride":[0,0,0,0,0,0,0,0,0,0,0,0],"ride_bell":[0,0,0,0,0,0,0,0,0,0,0,0],"hihat":[1,0,1,0,0,0,1,0,0,0,0,0],"hihat_open":[0,0,0,0,1,0,0,0,0,0,0,0],"snare":[0,0,0,0,1,0,0,0,0,0,0,0],"tom1":[0,0,0,0,0,0,0,0,0,0,0,0],"tom2":[0,0,0,0,0,0,0,0,0,0,0,0],"tom3":[0,0,0,0,0,0,0,0,0,0,0,0],"kick":[1,0,0,0,0,0,0,0,1,0,0,0],"stick":[0,0,1,0,0,0,0,0,1,0,0,0]}',
  '3/4', 1, 'cr78'
);

-- =========================================================
-- Date stagger: spread advanced grooves between 5 and 75 days ago
-- Interleaved with existing grooves for natural feed appearance
-- =========================================================

UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '75' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '75' DAY WHERE slug = 'jazz-waltz-3-4';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '62' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '62' DAY WHERE slug = 'linndrum-80s-pop';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '54' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '54' DAY WHERE slug = 'techno-four-on-the-floor';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '47' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '47' DAY WHERE slug = 'cr78-disco-groove';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '40' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '40' DAY WHERE slug = 'bossa-3-4-groove';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '34' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '34' DAY WHERE slug = 'linndrum-boom-bap';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '27' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '27' DAY WHERE slug = 'acid-house-tr909';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '21' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '21' DAY WHERE slug = 'odd-time-7-8';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '15' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '15' DAY WHERE slug = 'linndrum-funk-groove';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '10' DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '10' DAY WHERE slug = 'prog-5-4-metal';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '6'  DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '6'  DAY WHERE slug = 'blues-ballad-6-8';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '5'  DAY, updated_at = CURRENT_TIMESTAMP - INTERVAL '5'  DAY WHERE slug = 'cr78-waltz-jazz';
