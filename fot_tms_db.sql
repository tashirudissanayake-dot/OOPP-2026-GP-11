

create database if not exists fot_tms_db;

use fot_tms_db;


create table if not exists users (
    user_id int auto_increment primary key,
    username varchar(50) not null unique,
    password_hash varchar(255) not null,
    role varchar(20) not null,
    email varchar(100) not null,
    status varchar(20) not null default 'active'
) engine=innodb;                 --   rollback kranna ona unot..


create table if not exists profiles (
    profile_id int auto_increment primary key,
    user_id int not null unique,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    contact_no varchar(20) default '',
    profile_picture_path varchar(255) default '',
    constraint fk_profile_user
    foreign key (user_id) references users(user_id)
    on delete cascade
) engine=innodb;








