# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table channel (
  id                        bigint auto_increment not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  name                      varchar(256) not null,
  constraint uq_channel_updated_by unique (updated_by),
  constraint uq_channel_created_by unique (created_by),
  constraint pk_channel primary key (id))
;

create table product (
  id                        bigint auto_increment not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  name                      varchar(256) not null,
  constraint uq_product_updated_by unique (updated_by),
  constraint uq_product_created_by unique (created_by),
  constraint pk_product primary key (id))
;

create table producto (
  id                        bigint not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  given_name                varchar(256) not null,
  description               varchar(256) not null,
  sku                       varchar(256) not null,
  constraint uq_producto_updated_by unique (updated_by),
  constraint uq_producto_created_by unique (created_by),
  constraint pk_producto primary key (id))
;

create table s_action (
  id                        varchar(255) not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  constraint uq_s_action_updated_by unique (updated_by),
  constraint uq_s_action_created_by unique (created_by),
  constraint pk_s_action primary key (id))
;

create table s_app (
  license_key               varchar(255) not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  name                      varchar(255),
  description               varchar(255),
  constraint uq_s_app_updated_by unique (updated_by),
  constraint uq_s_app_created_by unique (created_by),
  constraint pk_s_app primary key (license_key))
;

create table s_module (
  id                        varchar(255) not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  constraint uq_s_module_updated_by unique (updated_by),
  constraint uq_s_module_created_by unique (created_by),
  constraint pk_s_module primary key (id))
;

create table s_permission (
  id                        bigint not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  role_id                   bigint,
  module_id                 varchar(255),
  constraint uq_s_permission_updated_by unique (updated_by),
  constraint uq_s_permission_created_by unique (created_by),
  constraint uq_s_permission_1 unique (role_id,module_id),
  constraint pk_s_permission primary key (id))
;

create table s_role (
  id                        bigint not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  name                      varchar(255),
  constraint uq_s_role_updated_by unique (updated_by),
  constraint uq_s_role_created_by unique (created_by),
  constraint uq_s_role_1 unique (name),
  constraint pk_s_role primary key (id))
;

create table s_token (
  id                        bigint not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  user_id                   bigint,
  app_license_key           varchar(255),
  token                     varchar(255),
  constraint uq_s_token_updated_by unique (updated_by),
  constraint uq_s_token_created_by unique (created_by),
  constraint pk_s_token primary key (id))
;

create table settings (
  id                        varchar(255) not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  value                     varchar(255),
  GROUP_TYPE                varchar(7),
  type                      varchar(7),
  constraint ck_settings_GROUP_TYPE check (GROUP_TYPE in ('EMAIL','GENERAL')),
  constraint ck_settings_type check (type in ('BOOLEAN','STRING','NUMBER')),
  constraint uq_settings_updated_by unique (updated_by),
  constraint uq_settings_created_by unique (created_by),
  constraint pk_settings primary key (id))
;

create table user (
  id                        bigint not null,
  updated_by                bigint,
  created_by                bigint,
  created                   datetime,
  updated                   datetime,
  email                     varchar(256),
  given_name                varchar(256) not null,
  family_name               varchar(256) not null,
  sha_password              varbinary(64) not null,
  constraint uq_user_updated_by unique (updated_by),
  constraint uq_user_created_by unique (created_by),
  constraint pk_user primary key (id))
;


create table s_module_action (
  s_module_id                    varchar(255) not null,
  s_action_id                    varchar(255) not null,
  constraint pk_s_module_action primary key (s_module_id, s_action_id))
;

create table s_permission_action (
  s_permission_id                bigint not null,
  s_action_id                    varchar(255) not null,
  constraint pk_s_permission_action primary key (s_permission_id, s_action_id))
;

create table user_role (
  user_id                        bigint not null,
  s_role_id                      bigint not null,
  constraint pk_user_role primary key (user_id, s_role_id))
;
create sequence producto_seq;

create sequence s_action_seq;

create sequence s_app_seq;

create sequence s_module_seq;

create sequence s_permission_seq;

create sequence s_role_seq;

create sequence s_token_seq;

create sequence settings_seq;

create sequence user_seq;

alter table channel add constraint fk_channel_updatedBy_1 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_channel_updatedBy_1 on channel (updated_by);
alter table channel add constraint fk_channel_createdBy_2 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_channel_createdBy_2 on channel (created_by);
alter table product add constraint fk_product_updatedBy_3 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_product_updatedBy_3 on product (updated_by);
alter table product add constraint fk_product_createdBy_4 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_product_createdBy_4 on product (created_by);
alter table producto add constraint fk_producto_updatedBy_5 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_producto_updatedBy_5 on producto (updated_by);
alter table producto add constraint fk_producto_createdBy_6 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_producto_createdBy_6 on producto (created_by);
alter table s_action add constraint fk_s_action_updatedBy_7 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_s_action_updatedBy_7 on s_action (updated_by);
alter table s_action add constraint fk_s_action_createdBy_8 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_s_action_createdBy_8 on s_action (created_by);
alter table s_app add constraint fk_s_app_updatedBy_9 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_s_app_updatedBy_9 on s_app (updated_by);
alter table s_app add constraint fk_s_app_createdBy_10 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_s_app_createdBy_10 on s_app (created_by);
alter table s_module add constraint fk_s_module_updatedBy_11 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_s_module_updatedBy_11 on s_module (updated_by);
alter table s_module add constraint fk_s_module_createdBy_12 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_s_module_createdBy_12 on s_module (created_by);
alter table s_permission add constraint fk_s_permission_updatedBy_13 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_s_permission_updatedBy_13 on s_permission (updated_by);
alter table s_permission add constraint fk_s_permission_createdBy_14 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_s_permission_createdBy_14 on s_permission (created_by);
alter table s_permission add constraint fk_s_permission_role_15 foreign key (role_id) references s_role (id) on delete restrict on update restrict;
create index ix_s_permission_role_15 on s_permission (role_id);
alter table s_permission add constraint fk_s_permission_module_16 foreign key (module_id) references s_module (id) on delete restrict on update restrict;
create index ix_s_permission_module_16 on s_permission (module_id);
alter table s_role add constraint fk_s_role_updatedBy_17 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_s_role_updatedBy_17 on s_role (updated_by);
alter table s_role add constraint fk_s_role_createdBy_18 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_s_role_createdBy_18 on s_role (created_by);
alter table s_token add constraint fk_s_token_updatedBy_19 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_s_token_updatedBy_19 on s_token (updated_by);
alter table s_token add constraint fk_s_token_createdBy_20 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_s_token_createdBy_20 on s_token (created_by);
alter table s_token add constraint fk_s_token_user_21 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_s_token_user_21 on s_token (user_id);
alter table s_token add constraint fk_s_token_app_22 foreign key (app_license_key) references s_app (license_key) on delete restrict on update restrict;
create index ix_s_token_app_22 on s_token (app_license_key);
alter table settings add constraint fk_settings_updatedBy_23 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_settings_updatedBy_23 on settings (updated_by);
alter table settings add constraint fk_settings_createdBy_24 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_settings_createdBy_24 on settings (created_by);
alter table user add constraint fk_user_updatedBy_25 foreign key (updated_by) references user (id) on delete restrict on update restrict;
create index ix_user_updatedBy_25 on user (updated_by);
alter table user add constraint fk_user_createdBy_26 foreign key (created_by) references user (id) on delete restrict on update restrict;
create index ix_user_createdBy_26 on user (created_by);



alter table s_module_action add constraint fk_s_module_action_s_module_01 foreign key (s_module_id) references s_module (id) on delete restrict on update restrict;

alter table s_module_action add constraint fk_s_module_action_s_action_02 foreign key (s_action_id) references s_action (id) on delete restrict on update restrict;

alter table s_permission_action add constraint fk_s_permission_action_s_perm_01 foreign key (s_permission_id) references s_permission (id) on delete restrict on update restrict;

alter table s_permission_action add constraint fk_s_permission_action_s_acti_02 foreign key (s_action_id) references s_action (id) on delete restrict on update restrict;

alter table user_role add constraint fk_user_role_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_role add constraint fk_user_role_s_role_02 foreign key (s_role_id) references s_role (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists channel;

drop table if exists product;

drop table if exists producto;

drop table if exists s_action;

drop table if exists s_app;

drop table if exists s_module;

drop table if exists s_module_action;

drop table if exists s_permission;

drop table if exists s_permission_action;

drop table if exists s_role;

drop table if exists s_token;

drop table if exists settings;

drop table if exists user;

drop table if exists user_role;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists producto_seq;

drop sequence if exists s_action_seq;

drop sequence if exists s_app_seq;

drop sequence if exists s_module_seq;

drop sequence if exists s_permission_seq;

drop sequence if exists s_role_seq;

drop sequence if exists s_token_seq;

drop sequence if exists settings_seq;

drop sequence if exists user_seq;

