-- noinspection SqlNoDataSourceInspectionForFile

drop table if exists product;
CREATE TABLE `product` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`version` INT(11) NULL DEFAULT NULL,
	`available` TINYINT(1) NULL DEFAULT NULL,
	`name` VARCHAR(255) NOT NULL COLLATE 'utf8_unicode_ci',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;