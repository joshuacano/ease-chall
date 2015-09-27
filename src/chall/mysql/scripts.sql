create database reddit;
create user reddit_app@localhost Identified by 'reddit';
GRANT ALL PRIVILEGES ON reddit.* to reddit_app; 

-- Create customer table 
create table reddit.customer 
(
	cus_cd int not null AUTO_INCREMENT,
	name VARCHAR(30) NOT NULL,
	password VARCHAR(30) NOT NULL,
	PRIMARY KEY (cus_cd)
);

-- Create table storing likes per customer 
create table reddit.customer_likes ( 
	id int not null AUTO_INCREMENT, 
	cus_cd int not null, 
	reddit_id varchar(30) not null, 
	url varchar(4000) not null, 
	title varchar(4000) not null, 
	num_comments int, 
	img varchar(4000), 
	primary key (id)
);


