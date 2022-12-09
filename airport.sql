create schema if not exists `airport_database`;
 use `airport_database`;
 
 create table if not exists `clients`(
 `id` int auto_increment not null,
 `login` varchar(50) not null unique,
 `password` varchar(50) not null,
 `status` int not null,
  constraint `PK_clients` primary key (`id` ASC) 
 );
 
create table if not exists `admins`(
 `id` int auto_increment not null,
 `login` varchar(50) not null unique,
 `password` varchar(50) not null,
  constraint `PK_admins` primary key (`id` ASC) 
 );
 
 create table if not exists `raises`(
 `id` int auto_increment not null,
 `sourceName` varchar(50) not null,
 `destinationName` varchar(50) not null,
 `date` datetime not null,
 `costBuisness`float not null,
 `costStandart`float not null,
  constraint `PK_raises` primary key (`id` ASC) 
 );
 
 create table if not exists `clients_raises`(
 `id` int auto_increment not null,
 `raiseId` int not null,
 `clientId` int not null,
 `isBuisness` int not null,
  constraint `PK_clients_raises` primary key (`id` ASC),
  constraint `FK_clients_raises_raises` foreign key(`raiseId`) references `raises` (`id`),
  constraint `FK_clients_raises_clients` foreign key(`clientId`) references `clients` (`id`)
 );
 
  create table if not exists `tickets`(
 `id` int auto_increment not null,
 `clientsRaisesId` int not null,
 `fullname` varchar(50) not null,
 `pasport` varchar(50) not null,
 `series` varchar(50) not null,
  constraint `PK_tickets` primary key (`id` ASC),
  constraint `FK_tickets_clients_raises` foreign key(`clientsRaisesId`) references `clients_raises` (`id`)
 );

insert into `admins` (login, password) values ('admin', 'admin');