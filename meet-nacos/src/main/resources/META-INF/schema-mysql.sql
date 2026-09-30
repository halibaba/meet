/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

-- MySQL translation of schema.sql (Apache Derby).
CREATE DATABASE IF NOT EXISTS `nacos` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE `nacos`;

CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `tenant_id` varchar(128) DEFAULT '',
  `app_name` varchar(128) DEFAULT NULL,
  `content` longtext,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` varchar(128) DEFAULT NULL,
  `src_ip` varchar(20) DEFAULT NULL,
  `c_desc` varchar(256) DEFAULT NULL,
  `c_use` varchar(64) DEFAULT NULL,
  `effect` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `c_schema` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE INDEX `configinfo_dataid_key_idx` ON `config_info` (`data_id`);
CREATE INDEX `configinfo_groupid_key_idx` ON `config_info` (`group_id`);
CREATE INDEX `configinfo_dataid_group_key_idx` ON `config_info` (`data_id`, `group_id`);

CREATE TABLE `his_config_info` (
  `id` bigint NOT NULL,
  `nid` bigint NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `tenant_id` varchar(128) DEFAULT '',
  `app_name` varchar(128) DEFAULT NULL,
  `content` longtext,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` varchar(128) DEFAULT NULL,
  `src_ip` varchar(20) DEFAULT NULL,
  `op_type` char(10) DEFAULT NULL,
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE INDEX `hisconfiginfo_dataid_key_idx` ON `his_config_info` (`data_id`);
CREATE INDEX `hisconfiginfo_gmt_create_idx` ON `his_config_info` (`gmt_create`);
CREATE INDEX `hisconfiginfo_gmt_modified_idx` ON `his_config_info` (`gmt_modified`);

CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `tenant_id` varchar(128) DEFAULT '',
  `app_name` varchar(128) DEFAULT NULL,
  `content` longtext,
  `beta_ips` varchar(1024) DEFAULT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` varchar(128) DEFAULT NULL,
  `src_ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `tenant_id` varchar(128) DEFAULT '',
  `tag_id` varchar(128) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL,
  `content` longtext,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` varchar(128) DEFAULT NULL,
  `src_ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`, `group_id`, `tenant_id`, `tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `tenant_id` varchar(128) DEFAULT '',
  `datum_id` varchar(255) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL,
  `content` longtext,
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`, `group_id`, `tenant_id`, `datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `app_list` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_name` varchar(128) NOT NULL,
  `is_dynamic_collect_disabled` smallint DEFAULT 0,
  `last_sub_info_collected_time` datetime DEFAULT '1970-01-01 08:00:00',
  `sub_info_lock_owner` varchar(128) DEFAULT NULL,
  `sub_info_lock_time` datetime DEFAULT '1970-01-01 08:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_appname` (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `app_configdata_relation_subs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_name` varchar(128) NOT NULL,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `gmt_modified` datetime DEFAULT '2010-05-05 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_sub_config_datagroup` (`app_name`, `data_id`, `group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `app_configdata_relation_pubs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_name` varchar(128) NOT NULL,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `gmt_modified` datetime DEFAULT '2010-05-05 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_pub_config_datagroup` (`app_name`, `data_id`, `group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL,
  `tag_name` varchar(128) NOT NULL,
  `tag_type` varchar(64) DEFAULT NULL,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `tenant_id` varchar(128) DEFAULT '',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`, `tag_name`, `tag_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE INDEX `config_tags_tenant_id_idx` ON `config_tags_relation` (`tenant_id`);

CREATE TABLE `group_capacity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` varchar(128) DEFAULT '',
  `quota` int DEFAULT 0,
  `usage` int DEFAULT 0,
  `max_size` int DEFAULT 0,
  `max_aggr_count` int DEFAULT 0,
  `max_aggr_size` int DEFAULT 0,
  `max_history_count` int DEFAULT 0,
  `gmt_create` datetime DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime DEFAULT '2010-05-05 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `tenant_capacity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(128) DEFAULT '',
  `quota` int DEFAULT 0,
  `usage` int DEFAULT 0,
  `max_size` int DEFAULT 0,
  `max_aggr_count` int DEFAULT 0,
  `max_aggr_size` int DEFAULT 0,
  `max_history_count` int DEFAULT 0,
  `gmt_create` datetime DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime DEFAULT '2010-05-05 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `kp` varchar(128) NOT NULL,
  `tenant_id` varchar(128) DEFAULT '',
  `tenant_name` varchar(128) DEFAULT '',
  `tenant_desc` varchar(256) DEFAULT NULL,
  `create_source` varchar(32) DEFAULT NULL,
  `gmt_create` bigint NOT NULL,
  `gmt_modified` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE INDEX `tenant_info_tenant_id_idx` ON `tenant_info` (`tenant_id`);

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `roles` (
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `uk_username_role` (`username`, `role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `permissions` (
  `role` varchar(50) NOT NULL,
  `resource` varchar(512) NOT NULL,
  `action` varchar(8) NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`, `resource`, `action`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO `users` (`username`, `password`, `enabled`) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

INSERT INTO `roles` (`username`, `role`) VALUES ('nacos', 'ROLE_ADMIN');
