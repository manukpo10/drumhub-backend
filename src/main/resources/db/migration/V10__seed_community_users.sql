-- V10: Seed community users — 6 fictional Argentine drummers
-- Password for all: "drumhub123" (BCrypt cost 10)

INSERT INTO users (id, username, name, email, password_hash, bio, avatar_seed, color, init, plan, activo)
VALUES
(200, 'matias_drum',    'Matías García',       'matias_drum@drumhub.app',    '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Baterista rockero de Córdoba. 12 años tocando.',                             'felix',   '#e8ff00', 'M', 'FREE', TRUE),
(201, 'luciana_beats',  'Luciana Torres',      'luciana_beats@drumhub.app',  '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Fanática del funk y el pop. Toco desde los 16.',                            'tiger',   '#ff6b35', 'L', 'FREE', TRUE),
(202, 'carovillareal',  'Carolina Villareal',  'carovillareal@drumhub.app',  '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Jazz y bossa nova. Estudio en el conservatorio.',                           'dean',    '#7b61ff', 'C', 'PRO',  TRUE),
(203, 'pablogroove',    'Pablo Herrera',       'pablogroove@drumhub.app',    '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Del reggae al latin, siempre con el clave adentro.',                        'octavia', '#00c9a7', 'P', 'FREE', TRUE),
(204, 'sofiabateria',   'Sofía Mendez',        'sofiabateria@drumhub.app',   '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Trap y hip-hop. Productora y baterista desde los 18.',                      'nova',    '#e040fb', 'S', 'FREE', TRUE),
(205, 'juanmabaqueta',  'Juan Manuel Ríos',    'juanmabaqueta@drumhub.app',  '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Blues, soul y todo lo que tenga groove. De Rosario.',                       'nadia',   '#f4c430', 'J', 'FREE', TRUE);
