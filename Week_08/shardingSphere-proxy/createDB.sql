USE `GoodsTransaction`;
DROP TABLE IF EXISTS `GoodsTransaction`.`Order0`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order0` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order1`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order1` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order2`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order2` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order3`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order3` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order4`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order4` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order5`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order5` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order6`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order6` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order7`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order7` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order8`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order8` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order9`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order9` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order10`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order10` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order11`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order11` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order12`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order12` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order13`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order13` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order14`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order14` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);
DROP TABLE IF EXISTS `GoodsTransaction`.`Order15`;
CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order15` (
`order_id` INT NOT NULL,
`user_name` VARCHAR(45) NOT NULL);

  
  
use sharding_order;
show tables;


select * from sharding_order.t_order;

insert into sharding_order.t_order  values (2,'Ben')