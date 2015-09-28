-- Create table section
create table customer
(
	cus_cd int not null AUTO_INCREMENT,
	name VARCHAR(30) NOT NULL,
	password VARCHAR(30) NOT NULL,PRIMARY KEY (cus_cd)
);


create table address (
	id int,
	cus_cd int,
	address_1 varchar(3000),
	address_2 varchar(3000),
	city varchar(1000),
	state varchar(1000),
	postcode varchar(20),
	date_entered datetime,
	active bit
);

create table phone ( 
	id int, 
	cus_cd int, 
	country_code varchar(5), 
	area_code varchar(10), 
	phone_number varchar(20), 
	date_entered datetime, 
	active bit
);

-- Show Customers and addresses
select * from customer cus inner join address addr on cus.cus_cd = addr.cus_cd;

-- Show Customers and phones
select * from customer cus inner join phones phn on cus.cus_cd = phn.cus_cd;

-- Show Customers in California
select * from customer cus inner join address addr on cus.cus_cd = addr.cus_cd WHERE addr.state = 'CA';

-- Show Addresses by State
SELECT state, count(*) as people_count from address group by state order by count(*);

-- Show % of people have multiple_addresses;
SELECT 1.0*(SUM(CASE WHEN addr_count > 1 THEN 1 ELSE 0 END)/ count(*) ) as multiple_percent FROM (SELECT cus_cd, COUNT(*) as addr_count from address GROUP by cus_cd) a;
