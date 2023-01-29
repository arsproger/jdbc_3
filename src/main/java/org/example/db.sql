CREATE TABLE Users(
    id SERIAL PRIMARY KEY,
    login VARCHAR(25) NOT NULL UNIQUE,
    email VARCHAR(25) NOT NULL UNIQUE,
    password VARCHAR(25) NOT NULL,
    date_of_registration TIMESTAMP
);

CREATE TABLE User_logs(
    login VARCHAR NOT NULL,
    entry_time TIMESTAMP,
    fail VARCHAR NOT NULL
);

select * from users;
select * from user_logs;