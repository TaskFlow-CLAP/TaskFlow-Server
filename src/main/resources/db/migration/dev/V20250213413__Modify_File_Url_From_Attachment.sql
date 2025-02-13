alter table attachment
    modify file_url varchar(2048) not null;

alter table comment
    modify file_url varchar(2048) null;