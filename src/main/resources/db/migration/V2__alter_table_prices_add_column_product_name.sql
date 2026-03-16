ALTER TABLE prices ADD COLUMN product_name TEXT;
UPDATE prices SET product_name = 'Неизвестно' WHERE product_name IS NULL;
ALTER TABLE prices ALTER COLUMN product_name SET NOT NULL;