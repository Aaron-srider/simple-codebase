#!/bin/bash

# 可变参数
host=127.0.0.1
port=33061
username=root
password=wc123456
dbname=simple-codebase
# 备份sql文件名
time=$(date "+%Y-%m-%d-%H:%M:%S")
sqlname="sb-db-backup-${time}.sql"


# 开始备份
sh -c "mysqldump --column-statistics=0 -h ${host} -P ${port} -u ${username} --password=${password} ${dbname} > ${sqlname}"