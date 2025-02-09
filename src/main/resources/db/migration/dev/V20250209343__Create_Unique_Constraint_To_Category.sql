alter table category
    add constraint category_name
        unique (name);
alter table category
    add constraint category_code
        unique (code);