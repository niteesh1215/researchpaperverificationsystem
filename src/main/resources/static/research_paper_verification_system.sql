CREATE TABLE `research_paper_verification_system`.`research_details` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `group_id` BIGINT NOT NULL DEFAULT NULL,
  `date` VARCHAR(50) NULL DEFAULT NULL,
  `email_address` VARCHAR(100) NULL DEFAULT NULL,
  `salutation` VARCHAR(10) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `designation` VARCHAR(100) NULL DEFAULT NULL,
  `type_of_authorship` VARCHAR(100) NULL DEFAULT NULL,
  `department` VARCHAR(300) NULL DEFAULT NULL,
  `indexing` VARCHAR(100) NULL DEFAULT NULL,
  `orcid` VARCHAR(100) NULL DEFAULT NULL,
  `impact_factor` VARCHAR(100) NULL DEFAULT NULL,
  `manuscript_title` VARCHAR(300) NULL DEFAULT NULL,
  `journal_title` VARCHAR(300) NULL DEFAULT NULL,
  `month` VARCHAR(150) NULL DEFAULT NULL,
  `year` INT NULL DEFAULT NULL,
  `volume_number` VARCHAR(10) NULL DEFAULT NULL,
  `issue_number` VARCHAR(10) NULL DEFAULT NULL,
  `page_numbers` VARCHAR(20) NULL DEFAULT NULL,
  `issn` VARCHAR(30) NULL DEFAULT NULL,
  `h_index_given_by_scopus` VARCHAR(10) NULL DEFAULT NULL,
  `proof_of_journal_indexing` VARCHAR(255) NULL DEFAULT NULL,
  `doi_url_link` VARCHAR(255) NULL DEFAULT NULL,
  `full_article` VARCHAR(255) NULL DEFAULT NULL,
  `is_publication_mandatory_phd_requirement` VARCHAR(5) NULL DEFAULT NULL,
  `is_publication_outcome_of_shodh_pravartan_or_irg` VARCHAR(5) NULL DEFAULT NULL,
  `is_authorship_affiliated_to_kjc` VARCHAR(5) NULL DEFAULT NULL,
  `verification_status` VARCHAR(45) NULL DEFAULT 'not_verified',
  `verification_timestamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));


/*CREATE TABLE `research_paper_verification_system`.`upload_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `file_name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(300) NULL DEFAULT '',
  `date` DATE NOT NULL,
  `timestamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `file_name_UNIQUE` (`file_name` ASC) VISIBLE);
  */

  CREATE TABLE `research_paper_verification_system`.`column_map` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `column_name` VARCHAR(200) NOT NULL,
  `mapped_name` VARCHAR(300) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `column_name_UNIQUE` (`column_name` ASC) VISIBLE);

ALTER TABLE `research_paper_verification_system`.`column_map` 
ADD UNIQUE INDEX `mapped_name_UNIQUE` (`mapped_name` ASC) VISIBLE;
;

INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('1', 'date', 'Date');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('2', 'email_address', 'Email Address');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('4', 'name', 'Name [Full Name without Fr. / Dr. / Mr. / Ms. ]');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('5', 'designation', 'Designation');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('6', 'type_of_authorship', 'Type of Authorship');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('7', 'department', 'Department');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('3', 'salutation', 'Salutation');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('8', 'indexing', 'Indexing');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('9', 'orcid', 'ORCID');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('10', 'impact_factor', 'Impact Factor');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('11', 'manuscript_title', 'Manuscript Title');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('12', 'journal_title', 'Journal Title');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('13 ', 'month', 'Month');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('14', 'year', 'Year');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('15', 'volume_number', 'Volume Number');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('16', 'issue_number', 'Issue Number');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('17', 'page_numbers', 'Page Numbers');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('18', 'issn', 'ISSN');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('19', 'h_index_given_by_scopus', 'H-Index given by Scopus');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('20', 'proof_of_journal_indexing', 'Proof of Journal Indexing [Upload as a PDF file]');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('21', 'doi_url_link', 'DOI / URL Link');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('22', 'full_article', 'Full Article [Upload as a PDF file]');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('23', 'is_publication_mandatory_phd_requirement', 'Is the publication a part of the mandatory requirement for Ph.D programme?');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('24', 'is_publication_outcome_of_shodh_pravartan_or_irg', 'Is the publication an outcome of Shodh Pravartan / Institutional Research Grant?');
INSERT INTO `research_paper_verification_system`.`column_map` (`id`, `column_name`, `mapped_name`) VALUES ('25', 'Is_authorship_affiliated_to_kjc', 'is the Authorship affiliated to Kristu Jayanti College?');

CREATE TABLE `research_paper_verification_system`.`upload` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `file_name` VARCHAR(255) NOT NULL,
  `date` DATE NOT NULL,
  `description` VARCHAR(300) NULL DEFAULT '',
  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `file_name_UNIQUE` (`file_name` ASC) VISIBLE);

# adds foreign key to research_details table
ALTER TABLE `research_paper_verification_system`.`research_details` 
ADD INDEX `id_idx` (`group_id` ASC) VISIBLE;
;
ALTER TABLE `research_paper_verification_system`.`research_details` 
ADD CONSTRAINT `group_id_foreign_key`
  FOREIGN KEY (`group_id`)
  REFERENCES `research_paper_verification_system`.`upload` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
  CREATE TABLE `research_paper_verification_system`.`verification_details` (
  `research_details_row_id` BIGINT NOT NULL,
  `fetched_full_name` VARCHAR(255) NULL,
  `full_name_match` TINYINT(1) NULL DEFAULT 0,
  `fetched_manuscript_title` VARCHAR(300) NULL,
  `manuscript_title_match` TINYINT(1) NULL DEFAULT 0,
  `fetched_journal_title` VARCHAR(300) NULL,
  `journal_title_match` TINYINT(1) NULL DEFAULT 0,
  `fetched_volume_number` VARCHAR(30) NULL,
  `volume_number_match` TINYINT(1) NULL DEFAULT 0,
  `fetched_issn` VARCHAR(30) NULL,
  `issn_match` TINYINT(1) NULL DEFAULT 0,
  `verification_match_percentage` DECIMAL(4,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`research_details_row_id`),
  CONSTRAINT `verification_details_fk`
    FOREIGN KEY (`research_details_row_id`)
    REFERENCES `research_paper_verification_system`.`research_details` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
    
    
ALTER TABLE `research_paper_verification_system`.`column_map` 
ADD COLUMN `data_length` INT NOT NULL DEFAULT 300 AFTER `mapped_name`;

UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '50' WHERE (`id` = '1');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '100' WHERE (`id` = '2');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '10' WHERE (`id` = '3');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '255' WHERE (`id` = '4');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '100' WHERE (`id` = '5');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '100' WHERE (`id` = '6');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '100' WHERE (`id` = '8');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '100' WHERE (`id` = '9');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '100' WHERE (`id` = '10');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '150' WHERE (`id` = '13');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '10' WHERE (`id` = '15');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '10' WHERE (`id` = '14');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '10' WHERE (`id` = '16');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '20' WHERE (`id` = '17');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '30' WHERE (`id` = '18');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '10' WHERE (`id` = '19');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '255' WHERE (`id` = '20');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '255' WHERE (`id` = '21');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '255' WHERE (`id` = '22');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '5' WHERE (`id` = '23');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '5' WHERE (`id` = '24');
UPDATE `research_paper_verification_system`.`column_map` SET `data_length` = '5' WHERE (`id` = '25');


ALTER TABLE `research_paper_verification_system`.`verification_details` 
ADD COLUMN `found_url_1` VARCHAR(255) NULL DEFAULT '' AFTER `verification_match_percentage`,
ADD COLUMN `found_url_2` VARCHAR(255) NULL DEFAULT '' AFTER `found_url_1`;

CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `active` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roles` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


/* isFormatted*/
ALTER TABLE `research_paper_verification_system`.`research_details` 
ADD COLUMN `isFormatted` TINYINT NULL DEFAULT 0 AFTER `verification_timestamp`;



/*
CREATE TABLE `research_paper_verification_system`.`peer_reviewed_url` (
  `id` INT NOT NULL,
  `url` VARCHAR(300) NULL,
  `verification_details_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `peer_reviewed_url_fk_idx` (`verification_details_id` ASC) VISIBLE,
  CONSTRAINT `peer_reviewed_url_fk`
    FOREIGN KEY (`verification_details_id`)
    REFERENCES `research_paper_verification_system`.`verification_details` (`research_details_row_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
    

ALTER TABLE `research_paper_verification_system`.`peer_reviewed_url` 
DROP FOREIGN KEY `peer_reviewed_url_fk`;
ALTER TABLE `research_paper_verification_system`.`peer_reviewed_url` 
CHANGE COLUMN `verification_details_id` `verification_details_id` INT NOT NULL ;
ALTER TABLE `research_paper_verification_system`.`peer_reviewed_url` 
ADD CONSTRAINT `peer_reviewed_url_fk`
  FOREIGN KEY (`verification_details_id`)
  REFERENCES `research_paper_verification_system`.`verification_details` (`research_details_row_id`)
  ON DELETE CASCADE;
  */
  
  
  

