CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order0` (
  `order_id` INT NOT NULL,
  `User_user_name` VARCHAR(45) NOT NULL);


CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order1` (
  `order_id` INT NOT NULL,
  `User_user_name` VARCHAR(45) NOT NULL);


CREATE TABLE IF NOT EXISTS `GoodsTransaction`.`Order2` (
  `order_id` INT NOT NULL,
  `User_user_name` VARCHAR(45) NOT NULL);
  
  
  
use sharding_order;
show tables;


select * from sharding_order.t_order;

insert into sharding_order.t_order  values (2,'Ben')