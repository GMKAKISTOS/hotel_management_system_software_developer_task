CREATE DATABASE IF NOT EXISTS hotel_management_system;
--
USE hotel_management_system;
--
CREATE TABLE IF NOT EXISTS room_type
(
room_type_id INTEGER UNSIGNED AUTO_INCREMENT,
name_type_room VARCHAR(50) NOT NULL,
room_cost DECIMAL(10, 2),
PRIMARY KEY (room_type_id ASC),
CONSTRAINT name_type_room_unique UNIQUE (name_type_room),
CONSTRAINT name_type_room_check CHECK (name_type_room IN ("single", "double", "twin", "triple", 
"superior double", "family room"))
);
--
CREATE TABLE IF NOT EXISTS rooms
(
room_id INTEGER UNSIGNED AUTO_INCREMENT,
room_number INTEGER UNSIGNED NOT NULL,
room_type_id INTEGER UNSIGNED NOT NULL,
PRIMARY KEY (room_id ASC),
CONSTRAINT room_number_unique UNIQUE (room_number), 
CONSTRAINT room_number_check CHECK (room_number BETWEEN 100 AND 200),
CONSTRAINT room_type_id_foreign_key FOREIGN KEY (room_type_id) REFERENCES room_type (room_type_id) ON UPDATE CASCADE ON DELETE RESTRICT
);
--
CREATE TABLE IF NOT EXISTS customers
(
customer_id INTEGER UNSIGNED AUTO_INCREMENT,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
sex CHARACTER(1) DEFAULT "N",
date_birth DATE,
card_number_id VARCHAR(50) NOT NULL,
phone VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
PRIMARY KEY (customer_id ASC),
CONSTRAINT first_name_last_name_check CHECK (first_name REGEXP "^[A-Za-z]+$" AND last_name REGEXP "^[A-Za-z]+$"),
CONSTRAINT sex_check CHECK (sex IN ("M", "F", "N")),
CONSTRAINT date_birth_check CHECK (date_birth BETWEEN "1936-01-01" AND "2008-12-31"),
CONSTRAINT card_number_id_unique UNIQUE(card_number_id),
CONSTRAINT card_number_id_check CHECK (card_number_id REGEXP "^[A-Z]{2}[0-9]{6}$"), -- constraint παλιάς ταυτότητας.
CONSTRAINT phone_unique UNIQUE(phone),
CONSTRAINT phone_check CHECK (phone REGEXP "^69[0-9]{8}$"),
CONSTRAINT email_unique UNIQUE(email),
CONSTRAINT email_check CHECK (email REGEXP "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
);
--
CREATE TABLE IF NOT EXISTS reservations
(
reservation_id INTEGER UNSIGNED AUTO_INCREMENT,
start_date DATE NOT NULL,
end_date DATE NOT NULL,
total_room_cost DECIMAL(10, 2),
create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
room_id INTEGER UNSIGNED NOT NULL,
customer_id INTEGER UNSIGNED NOT NULL,
PRIMARY KEY (reservation_id ASC),
CONSTRAINT start_date_end_date_check CHECK ( end_date >= start_date AND (start_date >= "2026-05-01" AND end_date <= "9999-12-31")),
CONSTRAINT room_id_foreign_key FOREIGN KEY (room_id) REFERENCES rooms (room_id) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT customer_id_foreign_key FOREIGN KEY (customer_id) REFERENCES customers (customer_id) ON UPDATE CASCADE ON DELETE CASCADE
);
--
DELIMITER //
CREATE TRIGGER name_type_room_room_cost_trigger 
BEFORE INSERT 
ON room_type
FOR EACH ROW
BEGIN
SET NEW.name_type_room = LOWER(NEW.name_type_room);
IF NEW.name_type_room = "single" THEN
SET NEW.room_cost = 55.00;
ELSEIF NEW.name_type_room = "double" THEN
SET NEW.room_cost = 80.00;
ELSEIF NEW.name_type_room = "twin" THEN
SET NEW.room_cost = 80.00;
ELSEIF NEW.name_type_room = "triple" THEN
SET NEW.room_cost = 105.00;
ELSEIF NEW.name_type_room = "superior double" THEN
SET NEW.room_cost = 110.00;
ELSEIF NEW.name_type_room = "family room" THEN
SET NEW.room_cost = 140.00;
END IF;
END;
// DELIMITER ;
--
DELIMITER //
CREATE TRIGGER first_name_last_name_trigger 
BEFORE INSERT 
ON customers
FOR EACH ROW
BEGIN
SET NEW.first_name = CONCAT(UPPER(SUBSTRING(NEW.first_name, 1, 1)), LOWER(SUBSTRING(NEW.first_name, 2, LENGTH(NEW.first_name) - 1)));
SET NEW.last_name = CONCAT(UPPER(SUBSTRING(NEW.last_name, 1, 1)), LOWER(SUBSTRING(NEW.last_name, 2, LENGTH(NEW.last_name) - 1)));
END;
// DELIMITER ;
--
DELIMITER //
CREATE TRIGGER total_room_cost_room_available_trigger 
BEFORE INSERT 
ON reservations
FOR EACH ROW
BEGIN
DECLARE room_type_id_value INTEGER UNSIGNED;
DECLARE name_type_room_value VARCHAR(50);
DECLARE counter_for_dates INTEGER UNSIGNED;

SELECT room_type_id INTO room_type_id_value FROM rooms WHERE room_id = NEW.room_id LIMIT 1; -- Χρησιμοποίησα LIMIT αν και δεν χρειάζεται λόγω PRIMARY KEY.
SELECT name_type_room INTO name_type_room_value FROM room_type WHERE room_type_id = room_type_id_value LIMIT 1; -- Χρησιμοποίησα LIMIT αν και δεν χρειάζεται λόγω PRIMARY KEY.

IF name_type_room_value = "single" THEN
SET NEW.total_room_cost = 55.00 * DATEDIFF(NEW.end_date, NEW.start_date);
ELSEIF name_type_room_value = "double" THEN
SET NEW.total_room_cost = 80.00 * DATEDIFF(NEW.end_date, NEW.start_date);
ELSEIF name_type_room_value = "twin" THEN
SET NEW.total_room_cost = 80.00 * DATEDIFF(NEW.end_date, NEW.start_date);
ELSEIF name_type_room_value = "triple" THEN
SET NEW.total_room_cost = 105.00 * DATEDIFF(NEW.end_date, NEW.start_date);
ELSEIF name_type_room_value = "superior double" THEN
SET NEW.total_room_cost = 110.00 * DATEDIFF(NEW.end_date, NEW.start_date);
ELSEIF name_type_room_value = "family room" THEN
SET NEW.total_room_cost = 140.00 * DATEDIFF(NEW.end_date, NEW.start_date);
ELSE
SET NEW.total_room_cost = 0.00;
END IF;

SELECT COUNT(*) INTO counter_for_dates FROM reservations WHERE room_id = NEW.room_id 
AND NEW.start_date <= end_date
AND NEW.end_date >= start_date;

IF counter_for_dates > 0 THEN
SIGNAL SQLSTATE "45000"
SET MESSAGE_TEXT = "Room is already taken in this date range!";
END IF;

END;
// DELIMITER ;
--
CREATE INDEX room_id_index ON reservations(room_id);
CREATE INDEX customer_id_index ON reservations(customer_id);
CREATE INDEX start_date_end_date_index ON reservations(start_date, end_date);
CREATE INDEX end_date_index ON reservations(end_date);
--
SET GLOBAL event_scheduler = ON;
--
CREATE EVENT IF NOT EXISTS delete_expired_reservations
ON SCHEDULE EVERY 2 WEEK
STARTS CURRENT_TIMESTAMP
DO
DELETE FROM reservations WHERE end_date < CURRENT_DATE;
--
ALTER EVENT delete_expired_reservations ENABLE;
--
INSERT INTO room_type (name_type_room) VALUES 
("SiNgLe"),
("DoUbLe"),
("tWiN"),
("TrIpLe"),
("SuPeRiOr DoUbLe"),
("FaMiLy RoOm");
COMMIT;
--
INSERT INTO rooms (room_number, room_type_id) VALUES 
(100, 1),
(101, 2),
(102, 3),
(103, 4),
(104, 5),
(105, 6);
COMMIT;
--
INSERT INTO customers (first_name, last_name, card_number_id, phone, email) VALUES
("PANAGIotis", "chronoPOULOS", "AH714532", "6970691054", "panos_chron@hotmail.com");
INSERT INTO customers VALUES
(2, "nikos", "stergiou", "M", "1997-03-27", "AH620412", "6940104354", "nikos_str@gmail.com"),
(3, "giwrgos", "alexopoulos", "M", "1999-04-19", "AH234567", "6923567854", "giwrgos_alex@hotmail.com");
COMMIT;
--
INSERT INTO reservations (start_date, end_date, room_id, customer_id) VALUES
("2026-05-01", "2026-05-12", 1, 1),
("2026-05-12", "2026-05-23", 2, 2);
INSERT INTO reservations (start_date, end_date, room_id, customer_id) VALUES
("2026-05-13", "2026-05-20", 1, 3);
INSERT INTO reservations (start_date, end_date, room_id, customer_id) VALUES
("2026-05-21", "2026-05-21", 1, 3);
COMMIT;
--
SELECT * FROM room_type;
SELECT * FROM rooms;
SELECT * FROM customers;
SELECT * FROM reservations;
--
SELECT * FROM reservations r WHERE r.customer_id IN (1 , 2, 3); -- Indexed Range Scan
SELECT * FROM reservations r WHERE r.customer_id = 3; -- Non-Unique Key Lookup
SELECT * FROM reservations r WHERE r.start_date >= "2026-05-01" AND r.end_date <= "2026-05-20"; -- Indexed Range Scan
SELECT * FROM reservations r WHERE r.start_date >= "2026-05-01"; -- Indexed Range Scan
SELECT * FROM reservations r WHERE r.start_date = "2026-05-01"; -- Non-Unique Key Lookup
SELECT * FROM rooms r LEFT JOIN reservations res ON r.room_id = res.room_id WHERE res.room_id IS NULL; -- Full Table Scan
SELECT * FROM rooms r WHERE NOT EXISTS (SELECT NULL FROM reservations res WHERE r.room_id = res.room_id); -- Full Table Scan -- Non-Unique Key Lookup
--
SELECT * FROM reservations WHERE reservation_id IN (1, 2) FOR UPDATE;
DELETE FROM reservations WHERE reservation_id IN (1, 2);
COMMIT;
SELECT * FROM reservations;
--
SELECT * FROM reservations WHERE reservation_id = 3 FOR UPDATE;
DELETE FROM reservations WHERE reservation_id = 3;
COMMIT;
SELECT * FROM reservations;
--
USE hotel_management_system;
--
ALTER EVENT delete_expired_reservations DISABLE;
--
DROP EVENT IF EXISTS delete_expired_reservations;
--
SET GLOBAL event_scheduler = OFF;
--
DROP TRIGGER IF EXISTS total_room_cost_room_available_trigger;
--
DROP TRIGGER IF EXISTS first_name_last_name_trigger;
--
DROP TRIGGER IF EXISTS name_type_room_room_cost_trigger;
-- 
DELETE FROM reservations;
COMMIT;
--
TRUNCATE TABLE reservations;
--
ALTER TABLE reservations DROP CONSTRAINT customer_id_foreign_key,
DROP CONSTRAINT room_id_foreign_key,
DROP INDEX end_date_index,
DROP INDEX start_date_end_date_index,
DROP INDEX customer_id_index,
DROP INDEX room_id_index,
DROP CONSTRAINT start_date_end_date_check,
MODIFY reservation_id INTEGER UNSIGNED NOT NULL,
DROP PRIMARY KEY;
--
DROP TABLE IF EXISTS reservations;
--
DELETE FROM customers;
COMMIT;
--
TRUNCATE TABLE customers;
--
ALTER TABLE customers DROP CONSTRAINT email_check,
DROP CONSTRAINT email_unique,
DROP CONSTRAINT phone_check,
DROP CONSTRAINT phone_unique,
DROP CONSTRAINT card_number_id_check,
DROP CONSTRAINT card_number_id_unique,
DROP CONSTRAINT date_birth_check,
DROP CONSTRAINT sex_check,
DROP CONSTRAINT first_name_last_name_check,
MODIFY customer_id INTEGER UNSIGNED NOT NULL,
DROP PRIMARY KEY;
--
DROP TABLE IF EXISTS customers;
-- 
DELETE FROM rooms;
COMMIT;
--
TRUNCATE TABLE rooms;
--
ALTER TABLE rooms DROP CONSTRAINT room_type_id_foreign_key,
DROP CONSTRAINT room_number_check,
DROP CONSTRAINT room_number_unique,
MODIFY room_id INTEGER UNSIGNED NOT NULL,
DROP PRIMARY KEY;
--
DROP TABLE IF EXISTS rooms;
-- 
DELETE FROM room_type;
COMMIT;
--
TRUNCATE TABLE room_type;
--
ALTER TABLE room_type DROP CONSTRAINT name_type_room_check,
DROP CONSTRAINT name_type_room_unique,
MODIFY room_type_id INTEGER UNSIGNED NOT NULL,
DROP PRIMARY KEY;
--
DROP TABLE IF EXISTS room_type;
--
DROP DATABASE IF EXISTS hotel_management_system;