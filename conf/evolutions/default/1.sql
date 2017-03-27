# --- First database schema

# --- !Ups

create table company (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_company primary key (id))
;

create table computer (
  id                        bigint not null,
  name                      varchar(255),
  introduced                timestamp,
  discontinued              timestamp,
  company_id                bigint,
  constraint pk_computer primary key (id))
;

create table profile (
  id                        bigint not null,
  title                      varchar(255),
  research                   varchar(255),
  firstName                  varchar(255),
  lastName                   varchar(255),
  position                   varchar(255),
  affiliation                varchar(255),
  email                      varchar(255),
  phone                      varchar(255),
  fax                        varchar(255),
  address                    varchar(500),
  city                       varchar(255),
  country                    varchar(255),
  region                     varchar(255),
  zipcode                       bigint,
  comment                   varchar(500),
  constraint pk_profile primary key (id))
;

create sequence company_seq start with 1000;

create sequence computer_seq start with 1000;

create sequence profile_seq start with 1000;

alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_computer_company_1 on computer (company_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists company;

drop table if exists computer;

drop table if exists profile;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists company_seq;

drop sequence if exists computer_seq;

drop sequence if exists profile_seq;

