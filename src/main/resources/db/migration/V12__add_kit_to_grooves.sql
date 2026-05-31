-- Add kit column to grooves: stores which drum kit the author used.
-- Existing grooves default to 'pearl' (the original default kit).
ALTER TABLE grooves ADD COLUMN kit VARCHAR(20) NOT NULL DEFAULT 'pearl';
