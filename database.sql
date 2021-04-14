drop database if exists atm;

create database atm character set utf8mb4;

use atm;

create table bank(
    idBank int not null auto_increment primary key,
    nameB varchar(50) not null
);

create table user(
    iduser varchar(15) not null primary key,
    nameUser varchar(40) not null,
    lastName varchar(40) not null,
    pin varchar(20) not null,
    idBank int not null,
    foreign key(idBank) references bank(idBank)
);

create table account(
    idAcct varchar(15) not null primary key,
    nameA varchar(45) not null,
    balance float,
    iduser varchar(15) not null,
    idBank int not null,
    foreign key(iduser) references user(iduser),
    foreign key(idBank) references bank(idBank)
);

create table transact(
    idTrans int not null auto_increment primary key,
    amount float not null,
    memo varchar(100) not null,
    datet varchar(100) not null,
    idAcct varchar(15) not null,
    foreign key(idAcct) references account(idAcct)
);


insert into bank values(1, 'Bank of Tijuana');
insert into user values('3917073', 'Cristina', 'Cazares', '1234', 1);
insert into account values('0123456789', 'savings', 0,'3917073', 1);
insert into account values('9876543210', 'university', 0,'3917073', 1);
insert into transact values(1, 17, 'dbtest', 'Wed Apr 14 15:04:53 PDT 2021', '0123456789');