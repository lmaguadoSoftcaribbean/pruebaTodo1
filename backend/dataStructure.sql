--
-- basic configuration
--

DROP DATABASE IF EXISTS `hulk_store_db`;
CREATE DATABASE `hulk_store_db`;

DROP USER IF EXISTS 'hulkstore'@'localhost';
CREATE USER 'hulkstore'@'localhost' IDENTIFIED BY 'hulkstore';
GRANT EXECUTE ON `hulk_store_db`.* TO 'hulkstore'@'localhost';

USE `hulk_store_db`;

--
-- creation of tables
--

CREATE TABLE `crl_params` (
  `key` VARCHAR(100) NOT NULL,
  `value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`key`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB;

CREATE TABLE `grl_permissions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(45) NOT NULL,
  `weight` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_permissions_weight_UNIQUE` (`weight` ASC) VISIBLE,
  UNIQUE INDEX `grl_permissions_label_UNIQUE` (`label` ASC) VISIBLE
) ENGINE=InnoDB;

CREATE TABLE `grl_users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `permission` INT DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `grl_users_email_UNIQUE` (`email` ASC) VISIBLE,
  CONSTRAINT `fk/grl_users/grl_permissions`
	FOREIGN KEY (`permission` ASC)
	REFERENCES `hulk_store_db`.`grl_permissions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `crl_token_blacklist` (
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
) ENGINE=InnoDB;

CREATE TABLE `grl_products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45),
  `name` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_UNIQUE` (`code`)
) ENGINE=InnoDB;

--
-- insert initial data
--

INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('1', 'NONE', '0');
INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('2', 'WORKER', '1');
INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('3', 'BUYER', '5');
INSERT INTO `hulk_store_db`.`grl_permissions` (`id`, `label`, `weight`) VALUES ('4', 'ADMIN', '10');

--
-- creation of procedure
--

DELIMITER $$

--
-- params
--
CREATE PROCEDURE `CREATE_PARAM` (IN _key VARCHAR(100), IN _value VARCHAR(200))
BEGIN
	INSERT INTO `hulk_store_db`.`crl_params` (`key`, `value`) VALUES (UPPER(_key), UPPER(_value));
    CALL `GET_USER`(_key);
END$$

CREATE PROCEDURE `GET_PARAM` (IN _key VARCHAR(100))
BEGIN
	SELECT
		`value` AS 'PARAM_VALUE'
    FROM `hulk_store_db`.`crl_params`
    WHERE (`key` = UPPER(_key));
END$$

--
-- token balcklist
--
CREATE PROCEDURE `CREATE_TOKEN_BLACKLIST_MANUAL` (IN _token VARCHAR(255), IN _own BIGINT, IN _reason VARCHAR(255), IN _blockedBy BIGINT, IN _expired DATE)
BEGIN
END$$

CREATE PROCEDURE `CREATE_TOKEN_BLACKLIST_AUTO` (IN _token VARCHAR(255), IN _own BIGINT, IN _expired DATE)
BEGIN
	INSERT INTO `hulk_store_db`.`crl_token_blacklist` (`token`, `token_own`, `expired`) VALUES (_token, _own, _expired);
    SELECT
		COUNT(*) AS 'COUNT'
    FROM `hulk_store_db`.`crl_token_blacklist`
    WHERE `token` = _token;
END$$

CREATE PROCEDURE `GET_TOKEN_BLACKLIST` (IN _token VARCHAR(255))
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
CREATE PROCEDURE `GET_PERMISSIONS` ()
BEGIN
	SELECT
		`id` AS 'PERMIT_ID',
        `label` AS 'PERMIT_LABEL',
        `weight` AS 'PERMIT_WEIGHT'
    FROM `hulk_store_db`.`grl_permissions`;
END$$

CREATE PROCEDURE `GET_PERMISSION` (IN _id INT)
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
CREATE PROCEDURE `CREATE_USER` (IN _email VARCHAR(255), IN _password VARCHAR(255))
BEGIN
	INSERT INTO `hulk_store_db`.`grl_users` (`password`, `email`) VALUES (_password, UPPER(_email));
    SELECT
		`grl_users`.`id` AS "USER_ID"
	FROM `hulk_store_db`.`grl_users`
    WHERE `grl_users`.`email` = UPPER(_email);
END$$

CREATE PROCEDURE `GET_USER_BY_ID` (IN _id BIGINT)
BEGIN
	SELECT
		`grl_users`.`id` AS 'USER_ID',
		`grl_users`.`username` AS 'USER_NAME',
		`grl_permissions`.`label` AS 'PERM_LABEL',
		`grl_permissions`.`weight` AS 'PERM_WEIGHT'
	FROM `hulk_store_db`.`grl_users`
		INNER JOIN `grl_permissions` ON `grl_users`.`permission`=`grl_permissions`.`id`
	WHERE `grl_users`.`id`=_id;
END$$

CREATE PROCEDURE `GET_USER_BY_Email` (IN _email VARCHAR(255))
BEGIN
	SELECT
		`grl_users`.`id` AS 'USER_ID',
        `grl_users`.`email` AS 'USER_EMAIL',
		`grl_permissions`.`label` AS 'PERM_LABEL',
		`grl_permissions`.`weight` AS 'PERM_WEIGHT'
	FROM `hulk_store_db`.`grl_users`
		INNER JOIN `grl_permissions` ON `grl_users`.`permission`=`grl_permissions`.`id`
	WHERE `grl_users`.`email`=_email;
END$$

CREATE PROCEDURE `GET_USER_PASS` (IN _id BIGINT)
BEGIN
	SELECT
		`grl_users`.`password` AS 'USER_PASSWORD'
	FROM `legion_latinoamericana_db`.`grl_users`
	WHERE `grl_users`.`id`=_id;
END$$

CREATE PROCEDURE `SET_USER_PERMISSION` (IN _id BIGINT, IN _permission INT)
BEGIN
	UPDATE `hulk_store_db`.`grl_users` SET `permission` = _permission WHERE (`id` = _id);
    CALL `GET_USER`(_id);
END$$

CREATE PROCEDURE `IS_USER_EXISTS` (IN _email VARCHAR(255))
BEGIN
	SELECT
		COUNT(`grl_users`.`id`) AS 'RESULT'
	FROM `hulk_store_db`.`grl_users`
	WHERE `grl_users`.`email`=UPPER(_email);
END$$

--
-- products
--
CREATE PROCEDURE `CREATE_PRODUCT` (IN _code VARCHAR(45), IN _name VARCHAR(255))
BEGIN
	INSERT INTO `hulk_store_db`.`grl_products` (`code`, `name`) VALUES (UPPER(_name), UPPER(_name));
    SELECT
		`grl_products`.`id` AS "PRODUCT_ID",
        `grl_products`.`code` AS "PRODUCT_ID",
        `grl_products`.`name` AS "PRODUCT_ID"
	FROM `hulk_store_db`.`grl_products`
    WHERE `grl_products`.`code` = UPPER(_code);
END$$

CREATE PROCEDURE `GET_PRODUCT` (IN _code VARCHAR(45))
BEGIN
END$$

DELIMITER ;
