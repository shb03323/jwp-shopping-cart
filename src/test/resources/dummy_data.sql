truncate table product;
alter table product auto_increment = 1;
insert into product (name, image, price)
values ('pooh', 'pooh.jpg', 1000000),
       ('ditoo', 'ditoo.jpg', 1000000),
       ('pobi', 'pobi.jpg', 10);
