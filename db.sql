create table students
(
    id     integer primary key auto_increment,
    fio    varchar(100) not null,
    age    integer,
    num    integer      not null unique,
    salary double       not null
);

insert into students(fio, age, num, salary)
VALUES ('Igor', 22, 100, 1200);
insert into students(fio, age, num, salary)
VALUES ('Ivan', 21, 101, 1300);
insert into students(fio, age, num, salary)
values ('Ila', 23, 102, 1400);
insert into students(fio, age, num, salary)
VALUES ('Dima', 24, 103, 1500);

select *
from students;
select *
from students
where salary > 1400;

create table auto
(
    id    integer primary key auto_increment,
    brand varchar(10) not null,
    power integer,
    year  integer     not null,
    id_s  integer     not null,
    foreign key (id_s) references students (id)
        on delete cascade on update cascade
);

insert into auto(brand, power, year, id_s)
VALUES ('Vaz', 140, 1988, 1);
insert into auto(brand, power, year, id_s)
VALUES ('Vaz', 120, 1992, 1);
insert into auto(brand, power, year, id_s)
VALUES ('Vaz', 130, 1990, 2);
insert into auto(brand, power, year, id_s)
VALUES ('audi', 150, 1995, 2);
insert into auto(brand, power, year, id_s)
VALUES ('kia', 145, 2004, 3);

select * from auto;








