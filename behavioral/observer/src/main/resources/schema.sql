create table orders (id varchar(64) primary key, paid boolean not null);
create table points (order_id varchar(64) primary key);
