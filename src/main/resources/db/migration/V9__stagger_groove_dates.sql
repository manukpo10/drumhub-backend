-- Stagger seed grooves' created_at across the recent past so the home "Actividad reciente"
-- feed shows a natural timeline instead of every groove sharing the migration timestamp.
-- Dates are relative to CURRENT_TIMESTAMP, so they stay fresh on every restart.
-- created_at is @Column(updatable=false) in BaseEntity, so JPA never overwrites these values.
-- Uses ANSI INTERVAL syntax so the same migration runs on both H2 (dev) and PostgreSQL (prod).

UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '2'  HOUR WHERE slug = 'bonham-levee';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '7'  HOUR WHERE slug = 'funky-drummer';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '26' HOUR WHERE slug = 'purdie-shuffle';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '2'  DAY  WHERE slug = 'trap-hihat-roll';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '4'  DAY  WHERE slug = 'half-time-shuffle';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '6'  DAY  WHERE slug = 'jazz-ride';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '9'  DAY  WHERE slug = 'trap-basic-808';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '13' DAY  WHERE slug = 'rap-boom-bap-classic';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '18' DAY  WHERE slug = 'trap-dark-double-kick';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '24' DAY  WHERE slug = 'rap-old-school-swing';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '33' DAY  WHERE slug = 'reggae-one-drop';
UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '45' DAY  WHERE slug = 'rap-aggressive-ghost';
