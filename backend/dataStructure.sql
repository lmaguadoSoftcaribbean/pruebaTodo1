--
-- basic configuration
--

DROP DATABASE IF EXISTS `hulk_store_db`;
CREATE DATABASE `hulk_store_db`;

DROP USER IF EXISTS 'hulkstore'@'localhost';
CREATE USER 'hulkstore'@'localhost' IDENTIFIED BY 'hulkstore';
GRANT EXECUTE ON `hulk_store_db`.* TO 'hulkstore'@'localhost';

--
-- creation of tables
--

CREATE TABLE `hulk_store_db`.`crl_params` (
  `key` VARCHAR(100) NOT NULL,
  `value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`key`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `hulk_store_db`.`grl_permissions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(45) NOT NULL,
  `weight` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_permissions_weight_UNIQUE` (`weight` ASC) VISIBLE,
  UNIQUE INDEX `grl_permissions_label_UNIQUE` (`label` ASC) VISIBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `hulk_store_db`.`grl_users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `permission` INT DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_users_username_UNIQUE` (`username` ASC) VISIBLE,
  CONSTRAINT `fk/grl_users/grl_permissions`
	FOREIGN KEY (`permission` ASC)
	REFERENCES `hulk_store_db`.`grl_permissions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `hulk_store_db`.`crl_token_blacklist` (
  `token` VARCHAR(255) NOT NULL,
  `token_own` BIGINT,
  `reason` VARCHAR(255),
  `blocked_by` BIGINT,
  `expired` DATE NOT NULL,
  PRIMARY KEY (`token`),
  UNIQUE KEY `key_UNIQUE` (`token`),
  CONSTRAINT `fk/crl_token_blacklist/grl_users`
	FOREIGN KEY (`blocked_by` ASC)
	REFERENCES `hulk_store_db`.`grl_users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk/crl_token_blacklist/grl_users/2`
	FOREIGN KEY (`token_own` ASC)
	REFERENCES `hulk_store_db`.`grl_users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `hulk_store_db`.`grl_products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_products_code_UNIQUE` (`code` ASC) VISIBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `hulk_store_db`.`grl_products_details` (
  `code` VARCHAR(45) NOT NULL,
  `unit` INT DEFAULT 0,
  PRIMARY KEY (`code`),
  CONSTRAINT `fk/grl_products_details/grl_products`
	FOREIGN KEY (`code` ASC)
	REFERENCES `hulk_store_db`.`grl_products` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

--
-- creation of procedure
--

DELIMITER $$

--
-- params
--
CREATE PROCEDURE `hulk_store_db`.`CREATE_PARAM` (IN _key VARCHAR(100), IN _value VARCHAR(200))
BEGIN
	INSERT INTO `hulk_store_db`.`crl_params` (`key`, `value`) VALUES (UPPER(_key), UPPER(_value));
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_PARAM` (IN _key VARCHAR(100))
BEGIN
	SELECT
		`value` AS 'PARAM_VALUE'
    FROM `hulk_store_db`.`crl_params`
    WHERE (`key` = UPPER(_key));
END$$

--
-- token balcklist
--
CREATE PROCEDURE `hulk_store_db`.`CREATE_TOKEN_BLACKLIST_MANUAL` (IN _token VARCHAR(255), IN _own BIGINT, IN _reason VARCHAR(255), IN _blockedBy BIGINT, IN _expired DATE)
BEGIN
END$$

CREATE PROCEDURE `hulk_store_db`.`CREATE_TOKEN_BLACKLIST_AUTO` (IN _token VARCHAR(255), IN _own BIGINT, IN _expired DATE)
BEGIN
	INSERT INTO `hulk_store_db`.`crl_token_blacklist` (`token`, `token_own`, `expired`) VALUES (_token, _own, _expired);
    SELECT
		COUNT(*) AS 'COUNT'
    FROM `hulk_store_db`.`crl_token_blacklist`
    WHERE `token` = _token;
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_TOKEN_BLACKLIST` (IN _token VARCHAR(255))
BEGIN
	SELECT
		`crl_token_blacklist`.`token` AS 'TOKEN_TOKEN',
        `own`.`username` AS 'USER_OWN',
        `crl_token_blacklist`.`reason` AS 'TOKEN_REASON',
        `crl_token_blacklist`.`blocked_by` AS 'TOKEN_BLOCKED_BY',
        `crl_token_blacklist`.`expired` AS 'TOKEN_EXPIRED'
	FROM `crl_token_blacklist`
		INNER JOIN `grl_users` AS `own` ON `crl_token_blacklist`.`token_own`=`own`.`id`
	WHERE `crl_token_blacklist`.`token` = _token;
END$$

--
-- permissions
--
CREATE PROCEDURE `hulk_store_db`.`CREATE_PERMISSION` (IN _label VARCHAR(45), IN _weight INT(3))
BEGIN
	INSERT INTO `hulk_store_db`.`grl_permissions` (`label`, `weight`) VALUES (UPPER(_label), _weight);
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_PERMISSIONS` ()
BEGIN
	SELECT
		`id` AS 'PERMIT_ID',
        `label` AS 'PERMIT_LABEL',
        `weight` AS 'PERMIT_WEIGHT'
    FROM `hulk_store_db`.`grl_permissions`;
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_PERMISSION` (IN _id INT)
BEGIN
	SELECT
		`id` AS 'PERMIT_ID',
        `label` AS 'PERMIT_LABEL',
        `weight` AS 'PERMIT_WEIGHT'
    FROM `hulk_store_db`.`grl_permissions`
    WHERE (`id` = _id);
END$$

--
-- users
--
CREATE PROCEDURE `hulk_store_db`.`CREATE_USER` (IN _username VARCHAR(45), IN _password VARCHAR(255), IN _email VARCHAR(255))
BEGIN
	INSERT INTO `hulk_store_db`.`grl_users` (`username`, `password`, `email`) VALUES (UPPER(_username), _password, UPPER(_email));
    SELECT
		`grl_users`.`id` AS "USER_ID"
	FROM `hulk_store_db`.`grl_users`
    WHERE `grl_users`.`username` = UPPER(_username);
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_USER_BY_ID` (IN _id BIGINT)
BEGIN
	SELECT
		`grl_users`.`id` AS 'USER_ID',
		`grl_users`.`username` AS 'USER_NAME',
        `grl_users`.`email` AS 'USER_EMAIL',
		`grl_permissions`.`label` AS 'PERM_LABEL',
		`grl_permissions`.`weight` AS 'PERM_WEIGHT'
	FROM `hulk_store_db`.`grl_users`
		INNER JOIN `grl_permissions` ON `grl_users`.`permission`=`grl_permissions`.`id`
	WHERE `grl_users`.`id`=_id;
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_USER_BY_USERNAME` (IN _username VARCHAR(45))
BEGIN
	SELECT
		`grl_users`.`id` AS 'USER_ID',
		`grl_users`.`username` AS 'USER_NAME',
        `grl_users`.`email` AS 'USER_EMAIL',
		`grl_permissions`.`label` AS 'PERM_LABEL',
		`grl_permissions`.`weight` AS 'PERM_WEIGHT'
	FROM `hulk_store_db`.`grl_users`
		INNER JOIN `grl_permissions` ON `grl_users`.`permission`=`grl_permissions`.`id`
	WHERE `grl_users`.`username`=_username;
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_USER_PASS` (IN _id BIGINT)
BEGIN
	SELECT
		`grl_users`.`password` AS 'USER_PASSWORD'
	FROM `hulk_store_db`.`grl_users`
	WHERE `grl_users`.`id`=_id;
END$$

CREATE PROCEDURE `hulk_store_db`.`SET_USER_PERMISSION` (IN _id BIGINT, IN _permission INT)
BEGIN
	UPDATE `hulk_store_db`.`grl_users` SET `permission` = _permission WHERE (`id` = _id);
    CALL `GET_USER`(_id);
END$$

CREATE PROCEDURE `hulk_store_db`.`IS_USER_EXISTS` (IN _username VARCHAR(45))
BEGIN
	SELECT
		COUNT(`grl_users`.`id`) AS 'RESULT'
	FROM `hulk_store_db`.`grl_users`
	WHERE `grl_users`.`username`=UPPER(_username);
END$$

--
-- Products
--
CREATE PROCEDURE `hulk_store_db`.`IS_PRODUCT_EXISTS` (IN _code VARCHAR(45))
BEGIN
	SELECT
		COUNT(`grl_products`.`code`) AS 'RESULT'
	FROM `hulk_store_db`.`grl_products`
	WHERE `grl_products`.`code`=UPPER(_code);
END$$

CREATE PROCEDURE `hulk_store_db`.`CREATE_PRODUCT` (IN _code VARCHAR(45), IN _name VARCHAR(255))
BEGIN
	INSERT INTO `hulk_store_db`.`grl_products` (`code`, `name`) VALUES (UPPER(_code), UPPER(_name));
    INSERT INTO `hulk_store_db`.`grl_products_details` (`code`) VALUES (UPPER(_code));
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_PRODUCT` (IN _code VARCHAR(45))
BEGIN
	SELECT
		A.`code` AS 'PRODUCT_CODE',
		A.`name` AS 'PRODUCT_NAME',
        B.`unit` AS 'PRODUCT_UNIT'
	FROM `hulk_store_db`.`grl_products` A
		INNER JOIN `hulk_store_db`.`grl_products_details` B ON A.`code` = B.`code`
	WHERE A.`code` = UPPER(_code);
END$$

CREATE PROCEDURE `hulk_store_db`.`GET_PRODUCTS` ()
BEGIN
	SELECT
		A.`code` AS 'PRODUCT_CODE',
		A.`name` AS 'PRODUCT_NAME',
        B.`unit` AS 'PRODUCT_UNIT'
	FROM `hulk_store_db`.`grl_products` A
		INNER JOIN `hulk_store_db`.`grl_products_details` B ON A.`code` = B.`code`;
END$$

CREATE PROCEDURE `hulk_store_db`.`SET_PRODUCT_UNIT` (IN _code VARCHAR(45), IN _unit INT)
BEGIN
	UPDATE `hulk_store_db`.`grl_products_details` SET `unit` = _unit WHERE `code` = UPPER(_code);
END$$

DELIMITER ;

--
-- insert initial data
--
INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('1', 'NONE', '0');
INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('2', 'BUYER', '1');
INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('3', 'WORKER', '10');
INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('4', 'ADMIN', '100');

-- usuario para pueabas
CALL hulk_store_db.CREATE_USER('usuario', '$2a$10$u.UrD39ca3munxT25ypY4emG.c5FCP5I7wgioYGnMB5MYtBnLojRq', 'email@correo.com');

CALL hulk_store_db.CREATE_PRODUCT('DC_COMIC_1', 'Batman 1');
CALL hulk_store_db.CREATE_PRODUCT('DC_COMIC_2', 'Batman 2');
CALL hulk_store_db.CREATE_PRODUCT('DC_COMIC_3', 'Batman 3');
CALL hulk_store_db.CREATE_PRODUCT('MC_COMIC_1', 'Hulk 1');
CALL hulk_store_db.CREATE_PRODUCT('MC_COMIC_2', 'Hulk 2');
CALL hulk_store_db.CREATE_PRODUCT('MC_COMIC_3', 'Hulk 3');