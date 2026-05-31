-- V13: Seed advanced users — 4 fictional drummers (IDs 206-209)
-- Two producers focused on vintage drum machines (TR-909, LinnDrum LM-2, CR-78)
-- and two drummers specializing in odd time signatures and soul/blues.
-- Password for all: "drumhub123" (BCrypt cost 10)

INSERT INTO users (id, username, name, email, password_hash, bio, avatar_seed, color, init, plan, activo)
VALUES
(206, 'martinr909',     'Martín Reyes',   'martinr909@drumhub.app',    '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Productor electrónico de Buenos Aires. El TR-909 es mi instrumento principal desde 2018.', 'lenny', '#00d4ff', 'M', 'PRO',  TRUE),
(207, 'diegopercusion', 'Diego Fernández','diegopercusion@drumhub.app', '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Metal progresivo y tiempos impares. Los 7/8 son mi hogar.',                                'orion', '#ff4444', 'D', 'FREE', TRUE),
(208, 'valentinasoul',  'Valentina Cruz', 'valentinasoul@drumhub.app', '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Soul, blues y baladas. La dinámica lo es todo.',                                            'mia',   '#ff9f43', 'V', 'FREE', TRUE),
(209, 'andrealm2',      'Andrea López',   'andrealm2@drumhub.app',     '$2a$10$slYQmyNdgTY18LGvgxPwHOIL4LTQZ7A83gdvGn1FPJ3U9qQ2jAOiK', 'Coleccionista de LinnDrum desde 2015. El LM-2 tiene un alma única.',                        'sunny', '#a29bfe', 'A', 'PRO',  TRUE);
