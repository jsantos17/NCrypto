SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `securemessage` ;
CREATE SCHEMA IF NOT EXISTS `securemessage` DEFAULT CHARACTER SET utf8 ;
USE `securemessage` ;

-- -----------------------------------------------------
-- Table `securemessage`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `securemessage`.`user` ;

CREATE  TABLE IF NOT EXISTS `securemessage`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_hash` TEXT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `securemessage`.`message`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `securemessage`.`message` ;

CREATE  TABLE IF NOT EXISTS `securemessage`.`message` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `origin_id` INT NOT NULL ,
  `destination_id` INT NOT NULL ,
  `message` TEXT NULL ,
  `key` TEXT NULL ,
  `vector` TEXT NULL ,
  `date` DATETIME NULL ,
  `read` TINYINT(1) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_message_user` (`origin_id` ASC) ,
  INDEX `fk_message_user1` (`destination_id` ASC) ,
  CONSTRAINT `fk_message_user`
    FOREIGN KEY (`origin_id` )
    REFERENCES `securemessage`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_message_user1`
    FOREIGN KEY (`destination_id` )
    REFERENCES `securemessage`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
