-- Application  database
create database persons;
use persons;

create table persons
(
    person_id  bigint auto_increment primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    birth_date date         not null,
    country    varchar(3)   not null,
    version    bigint
);

CREATE USER 'persons'@'%' IDENTIFIED BY 'persons';
GRANT ALL PRIVILEGES ON persons.* TO 'persons'@'%';
