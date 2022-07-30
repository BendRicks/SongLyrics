-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema song_lyr_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema song_lyr_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `song_lyr_db` DEFAULT CHARACTER SET utf8 ;
USE `song_lyr_db` ;

-- -----------------------------------------------------
-- Table `song_lyr_db`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`role` (
  `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rolename` CHAR(3) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE INDEX `rolename_UNIQUE` (`rolename` ASC) VISIBLE)
ENGINE = InnoDB;
INSERT INTO `role` (`rolename`) VALUES ('adm');
INSERT INTO `role` (`rolename`) VALUES ('mdr');
INSERT INTO `role` (`rolename`) VALUES ('usr');


-- -----------------------------------------------------
-- Table `song_lyr_db`.`acc_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`acc_status` (
  `status_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `status_descr` CHAR(6) NOT NULL,
  PRIMARY KEY (`status_id`),
  UNIQUE INDEX `acc_statuscol_UNIQUE` (`status_descr` ASC) VISIBLE)
ENGINE = InnoDB;
INSERT INTO `acc_status` (`status_descr`) VALUES ('inocnt');
INSERT INTO `acc_status` (`status_descr`) VALUES ('warned');
INSERT INTO `acc_status` (`status_descr`) VALUES ('banned');


-- -----------------------------------------------------
-- Table `song_lyr_db`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`user` (
  `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` CHAR(166) NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  `acc_status_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  INDEX `fk_user_role_idx` (`role_id` ASC) VISIBLE,
  INDEX `fk_user_acc_status1_idx` (`acc_status_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `song_lyr_db`.`role` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_acc_status1`
    FOREIGN KEY (`acc_status_id`)
    REFERENCES `song_lyr_db`.`acc_status` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`public_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`public_status` (
  `status_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `status_descr` VARCHAR(11) NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB;
INSERT INTO `public_status` (`status_descr`) VALUES ('verified');
INSERT INTO `public_status` (`status_descr`) VALUES ('nonverified');
INSERT INTO `public_status` (`status_descr`) VALUES ('limited');


-- -----------------------------------------------------
-- Table `song_lyr_db`.`performer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`performer` (
  `performer_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `performer_name` VARCHAR(100) NOT NULL,
  `descr_file_path` VARCHAR(255) NOT NULL,
  `cover_file_path` VARCHAR(255) NOT NULL,
  `status_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`performer_id`),
  UNIQUE INDEX `performer_name_UNIQUE` (`performer_name` ASC) VISIBLE,
  UNIQUE INDEX `desc_file_path_UNIQUE` (`descr_file_path` ASC) VISIBLE,
  INDEX `fk_performer_status1_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `fk_performer_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `song_lyr_db`.`public_status` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`album`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`album` (
  `album_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `album_name` VARCHAR(100) NOT NULL,
  `descr_file_path` VARCHAR(255) NOT NULL,
  `cover_file_path` VARCHAR(255) NOT NULL,
  `status_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`album_id`),
  UNIQUE INDEX `descr_file_path_UNIQUE` (`descr_file_path` ASC) VISIBLE,
  UNIQUE INDEX `cover_file_path_UNIQUE` (`cover_file_path` ASC) VISIBLE,
  INDEX `fk_album_status1_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `fk_album_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `song_lyr_db`.`public_status` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`album_performer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`album_performer` (
  `album_id` INT UNSIGNED NOT NULL,
  `performer_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`album_id`, `performer_id`),
  INDEX `fk_album_performer_performer1_idx` (`performer_id` ASC) VISIBLE,
  CONSTRAINT `fk_album_performer_album1`
    FOREIGN KEY (`album_id`)
    REFERENCES `song_lyr_db`.`album` (`album_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_album_performer_performer1`
    FOREIGN KEY (`performer_id`)
    REFERENCES `song_lyr_db`.`performer` (`performer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`track`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`track` (
  `track_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `track_name` VARCHAR(100) NOT NULL,
  `lyrics_file_path` VARCHAR(255) NOT NULL,
  `status_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`track_id`),
  UNIQUE INDEX `track_name_UNIQUE` (`track_name` ASC) VISIBLE,
  UNIQUE INDEX `lyrics_file_path_UNIQUE` (`lyrics_file_path` ASC) VISIBLE,
  INDEX `fk_track_status1_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `fk_track_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `song_lyr_db`.`public_status` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`track_performer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`track_performer` (
  `track_id` INT UNSIGNED NOT NULL,
  `performer_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`track_id`, `performer_id`),
  INDEX `fk_track_performer_performer1_idx` (`performer_id` ASC) VISIBLE,
  CONSTRAINT `fk_track_performer_track1`
    FOREIGN KEY (`track_id`)
    REFERENCES `song_lyr_db`.`track` (`track_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_track_performer_performer1`
    FOREIGN KEY (`performer_id`)
    REFERENCES `song_lyr_db`.`performer` (`performer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`track_album`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`track_album` (
  `track_id` INT UNSIGNED NOT NULL,
  `album_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`track_id`, `album_id`),
  INDEX `fk_track_album_album1_idx` (`album_id` ASC) VISIBLE,
  CONSTRAINT `fk_track_album_track1`
    FOREIGN KEY (`track_id`)
    REFERENCES `song_lyr_db`.`track` (`track_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_track_album_album1`
    FOREIGN KEY (`album_id`)
    REFERENCES `song_lyr_db`.`album` (`album_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`tracks_by_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`tracks_by_user` (
  `user_id` INT UNSIGNED NOT NULL,
  `track_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `track_id`),
  INDEX `fk_tracks_by_user_track1_idx` (`track_id` ASC) VISIBLE,
  CONSTRAINT `fk_tracks_by_user_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `song_lyr_db`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tracks_by_user_track1`
    FOREIGN KEY (`track_id`)
    REFERENCES `song_lyr_db`.`track` (`track_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`performers_by_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`performers_by_user` (
  `performer_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`performer_id`, `user_id`),
  INDEX `fk_performers_by_user_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_performers_by_user_performer1`
    FOREIGN KEY (`performer_id`)
    REFERENCES `song_lyr_db`.`performer` (`performer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_performers_by_user_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `song_lyr_db`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `song_lyr_db`.`albums_by_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `song_lyr_db`.`albums_by_user` (
  `album_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`album_id`, `user_id`),
  INDEX `fk_albums_by_user_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_albums_by_user_album1`
    FOREIGN KEY (`album_id`)
    REFERENCES `song_lyr_db`.`album` (`album_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_albums_by_user_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `song_lyr_db`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
