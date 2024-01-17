CREATE SCHEMA `personalapi_db` ;

USE mysql;

CREATE USER 'apidb_user'@'%' IDENTIFIED BY 'api123';

GRANT ALL PRIVILEGES ON personalapi_db.* TO 'apidb_user'@'%';
FLUSH PRIVILEGES;