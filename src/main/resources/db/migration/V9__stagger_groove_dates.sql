-- Stagger seed grooves' created_at across the recent past so the home "Actividad reciente"
-- feed shows a natural timeline instead of every groove sharing the migration timestamp.
-- Dates are relative to CURRENT_TIMESTAMP, so they stay fresh on every (in-memory) restart.
-- created_at is @Column(updatable=false) in BaseEntity, so JPA never overwrites these values.

UPDATE grooves SET created_at = DATEADD(HOUR, -2,  CURRENT_TIMESTAMP) WHERE slug = 'bonham-levee';
UPDATE grooves SET created_at = DATEADD(HOUR, -7,  CURRENT_TIMESTAMP) WHERE slug = 'funky-drummer';
UPDATE grooves SET created_at = DATEADD(HOUR, -26, CURRENT_TIMESTAMP) WHERE slug = 'purdie-shuffle';
UPDATE grooves SET created_at = DATEADD(DAY,  -2,  CURRENT_TIMESTAMP) WHERE slug = 'trap-hihat-roll';
UPDATE grooves SET created_at = DATEADD(DAY,  -4,  CURRENT_TIMESTAMP) WHERE slug = 'half-time-shuffle';
UPDATE grooves SET created_at = DATEADD(DAY,  -6,  CURRENT_TIMESTAMP) WHERE slug = 'jazz-ride';
UPDATE grooves SET created_at = DATEADD(DAY,  -9,  CURRENT_TIMESTAMP) WHERE slug = 'trap-basic-808';
UPDATE grooves SET created_at = DATEADD(DAY,  -13, CURRENT_TIMESTAMP) WHERE slug = 'rap-boom-bap-classic';
UPDATE grooves SET created_at = DATEADD(DAY,  -18, CURRENT_TIMESTAMP) WHERE slug = 'trap-dark-double-kick';
UPDATE grooves SET created_at = DATEADD(DAY,  -24, CURRENT_TIMESTAMP) WHERE slug = 'rap-old-school-swing';
UPDATE grooves SET created_at = DATEADD(DAY,  -33, CURRENT_TIMESTAMP) WHERE slug = 'reggae-one-drop';
UPDATE grooves SET created_at = DATEADD(DAY,  -45, CURRENT_TIMESTAMP) WHERE slug = 'rap-aggressive-ghost';
