create table file_records
(
id 						INT(11) NOT NULL AUTO_INCREMENT,
project_name			varchar(128),
path					varchar(2000),
last_modified			varchar(128),
length					varchar(128),
summary					varchar(128), -- 64位
description				varchar(128),
created_at        		TIMESTAMP,
updated_at        		TIMESTAMP
);
