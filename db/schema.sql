CREATE TABLE DEMO  (
	UUID varchar(256)  NOT NULL PRIMARY KEY,
	UPDATED_AT DATETIME NULL,
	PRICE DECIMAL(12,2) NULL,
	IS_VALID BOOLEAN NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;