use TuThien;

create table if not exists REGION
(
    region_id   int auto_increment
    primary key,
    name varchar(255) charset utf8mb3 null
    );

create table if not exists CITY
(
    city_id        int auto_increment
    primary key,
    name      varchar(100) charset utf8mb3 null,
    region_id int                          null,
    constraint CITY_REGION_id_fk
    foreign key (region_id) references REGION (region_id)
    );

create table if not exists MAP
(
    id      int auto_increment
    primary key,
    path    longtext null,
    city_id int      null,
    constraint MAP_CITY_id_fk
    foreign key (city_id) references CITY (city_id)
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

create table if not exists PROJECT
(
    project_id           varchar(255) not null
    primary key,
    avatar       varchar(255) null,
    city_id      int          null,
    content      longtext     null,
    username   varchar(255) null,
    created_date datetime(6)  null,
    end_date     date  null,
    start_date   date  null,
    status       int          null,
    title        varchar(255) null,
    description  text         null,
    type         int          null,
    money        int          null,
    constraint PROJECT_CITY_id_fk
    foreign key (city_id) references CITY (city_id),
    constraint PROJECT_USER_username_fk
    foreign key (username) references USER (username)
    );

create table if not exists DONATE
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
    type          int                          null,
    constraint DONATE_PROJECT_id_fk
    foreign key (project_id) references PROJECT (project_id)
);