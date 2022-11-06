create table if not exists DONATE
(
    id           int auto_increment
    primary key,
    created_by   varchar(255) null,
    created_date date         null,
    post_id      varchar(255) null,
    money        double       null
    );

create table if not exists POST
(
    id           varchar(255)                 not null
    primary key,
    title        varchar(255) charset utf8mb3 null,
    content      text                         null,
    created_by   varchar(255)                 null,
    created_date date                         null,
    expect_money double                       null,
    status       int                          null
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
    constraint username
    unique (username)
    );