create table if not exists DateState(
year int,
month int,
day int,
dateState int,
weekday int default -1,
remark varchar(20) null default '无',
primary key(year, month, day)
);