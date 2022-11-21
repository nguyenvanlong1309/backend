use TuThien;

create table if not exists TuThien.CITY
(
id        int auto_increment
primary key,
name      varchar(100) charset utf8mb3 null,
region_id int                          null
);

create table if not exists TuThien.DONATE
(
    id            int auto_increment
    primary key,
    created_by    varchar(255)                 null,
    created_date  date                         null,
    project_id    varchar(255)                 null,
    money         double                       null,
    public_name   varchar(255) charset utf8mb3 null,
    full_name     varchar(255) charset utf8mb3 null,
    email         varchar(255) charset utf8mb3 null,
    phone         varchar(20)                  null,
    mode          int                          null,
    method_donate int                          null,
    comment       text                         null,
    image         text                         null,
    type          int                          null
    );

create table if not exists PROJECT
(
    id           varchar(255) not null
    primary key,
    avatar       varchar(255) null,
    city_id      int          null,
    content      longtext     null,
    created_by   varchar(255) null,
    created_date datetime(6)  null,
    end_date     datetime(6)  null,
    start_date   datetime(6)  null,
    status       int          null,
    title        varchar(255) null,
    description  text         null
    );

create table if not exists USER
(
    id        varchar(255)                 not null
    primary key,
    username  varchar(255)                 null,
    password  text                         null,
    full_name varchar(255) charset utf8mb3 null,
    role      varchar(50)                  null,
    status    int                          null,
    phone     varchar(20)                  null,
    email     varchar(100)                 null,
    address   varchar(200) charset utf8mb3 null,
    constraint username
    unique (username)
);

create table if not exists TuThien.MAP
(
    id      int auto_increment
    primary key,
    path    longtext null,
    city_id int      null
);