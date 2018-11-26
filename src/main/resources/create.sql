DROP TABLE IF EXISTS part;
CREATE TABLE part
(
  part_number VARCHAR(9) PRIMARY KEY NOT NULL,
  part_name VARCHAR(50) NOT NULL,
  vendor VARCHAR(100),
  qty INT,
  shipped TIMESTAMP,
  receive TIMESTAMP
);

CREATE UNIQUE index part_part_number_uindex
ON part (part_number);