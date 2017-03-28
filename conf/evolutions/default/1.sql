# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_company primary key (id)
);

create table computer (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  introduced                    datetime(6),
  discontinued                  datetime(6),
  company_id                    bigint,
  constraint pk_computer primary key (id)
);

create table profile (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  research                      varchar(255),
  firstname                     varchar(255),
  lastname                      varchar(255),
  position                      varchar(255),
  affiliation                   varchar(255),
  email                         varchar(255),
  phone                         varchar(255),
  fax                           varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  country                       varchar(255),
  region                        varchar(255),
  zipcode                       bigint,
  comment                       varchar(255),
  constraint pk_profile primary key (id)
);

alter table computer add constraint fk_computer_company_id foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_computer_company_id on computer (company_id);


# --- !Downs

alter table computer drop foreign key fk_computer_company_id;
drop index ix_computer_company_id on computer;

drop table if exists company;

drop table if exists computer;

drop table if exists profile;

