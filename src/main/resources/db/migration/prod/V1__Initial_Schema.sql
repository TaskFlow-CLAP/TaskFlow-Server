create table api_log (
                         dtype varchar(31) not null,
                         log_id bigint not null auto_increment,
                         client_ip varchar(255) not null,
                         request_url varchar(4096) not null,
                         request_method enum ('DELETE','GET','PATCH','POST','PUT','UNKNOWN') not null,
                         status_code integer not null,
                         custom_status_code varchar(255) not null,
                         request_body TEXT not null,
                         response_body TEXT not null,
                         request_at datetime(6) not null,
                         log_status enum ('ASSIGNER_CHANGED','COMMENT_ADDED','COMMENT_UPDATED','LOGIN','REQUEST_APPROVED','REQUEST_CANCELLED','REQUEST_CREATED','REQUEST_UPDATED','STATUS_CHANGED','TASK_VIEWED') not null,
                         version bigint,
                         member_id bigint,
                         login_nickname varchar(255),
                         created_at datetime(6) not null,
                         updated_at datetime(6) not null,
                         primary key (log_id)
) engine=InnoDB;

create table attachment (
                            attachment_id bigint not null auto_increment,
                            original_name varchar(255) not null,
                            task_id bigint,
                            file_url varchar(255) not null,
                            file_size varchar(255) not null,
                            is_deleted bit not null,
                            created_at datetime(6) not null,
                            updated_at datetime(6) not null,
                            primary key (attachment_id)
) engine=InnoDB;

create table category (
                          category_id bigint not null auto_increment,
                          admin_id bigint not null,
                          code varchar(255) not null,
                          name varchar(255) not null,
                          main_category_id bigint,
                          is_deleted bit not null,
                          description_example varchar(255),
                          created_at datetime(6) not null,
                          updated_at datetime(6) not null,
                          primary key (category_id)
) engine=InnoDB;

create table comment (
                         comment_id bigint not null auto_increment,
                         member_id bigint not null,
                         task_id bigint not null,
                         content varchar(255),
                         original_name varchar(255),
                         file_size varchar(255),
                         file_url varchar(255),
                         is_modified bit not null,
                         is_deleted bit not null,
                         created_at datetime(6) not null,
                         updated_at datetime(6) not null,
                         primary key (comment_id)
) engine=InnoDB;

create table department (
                            department_id bigint not null auto_increment,
                            admin_id bigint,
                            name varchar(255) not null,
                            status enum ('ACTIVE','INACTIVE') not null,
                            is_manager bit not null,
                            created_at datetime(6) not null,
                            updated_at datetime(6) not null,
                            primary key (department_id)
) engine=InnoDB;

create table label (
                       label_id bigint not null auto_increment,
                       admin_id bigint not null,
                       label_name varchar(255) not null,
                       label_color enum ('BLUE','GREEN','GREY','INDIGO','ORANGE','PURPLE','RED','YELLOW') not null,
                       is_deleted bit not null,
                       created_at datetime(6) not null,
                       updated_at datetime(6) not null,
                       primary key (label_id)
) engine=InnoDB;

create table member (
                        member_id bigint not null auto_increment,
                        name varchar(255) not null,
                        nickname varchar(255) not null,
                        password varchar(255),
                        image_url varchar(255),
                        email varchar(255) not null,
                        role enum ('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER') not null,
                        status enum ('ACTIVE','APPROVAL_REQUEST','DELETED','INACTIVE','PENDING') not null,
                        is_reviewer bit not null,
                        department_role varchar(255),
                        admin_id bigint,
                        department_id bigint,
                        created_at datetime(6) not null,
                        updated_at datetime(6) not null,
                        email_notification_enabled bit,
                        kakaowork_notification_enabled bit,
                        in_progress_task_count integer,
                        in_reviewing_task_count integer,
                        primary key (member_id)
) engine=InnoDB;

create table notification (
                              notification_id bigint not null auto_increment,
                              is_read bit not null,
                              message varchar(255),
                              task_title varchar(255) not null,
                              receiver_id bigint not null,
                              task_id bigint,
                              type enum ('COMMENT','INVITATION','PROCESSOR_ASSIGNED','PROCESSOR_CHANGED','STATUS_SWITCHED','TASK_REQUESTED') not null,
                              created_at datetime(6) not null,
                              updated_at datetime(6) not null,
                              primary key (notification_id)
) engine=InnoDB;

create table task (
                      task_id bigint not null auto_increment,
                      attachment_count integer not null,
                      description varchar(255),
                      task_code varchar(255) not null,
                      title varchar(255) not null,
                      category_id bigint not null,
                      task_status enum ('COMPLETED','IN_PROGRESS','IN_REVIEWING','REQUESTED','TERMINATED') not null,
                      due_date datetime(6),
                      finished_at datetime(6),
                      label_id bigint,
                      processor_id bigint,
                      processor_order bigint,
                      requester_id bigint not null,
                      reviewer_id bigint,
                      agit_post_id bigint,
                      is_deleted bit not null,
                      created_at datetime(6) not null,
                      updated_at datetime(6) not null,
                      primary key (task_id)
) engine=InnoDB;

create table task_history (
                              task_history_id bigint not null auto_increment,
                              is_deleted bit not null,
                              comment_id bigint,
                              modified_member_id bigint,
                              task_id bigint,
                              modified_status varchar(255),
                              type enum ('COMMENT','COMMENT_FILE','PROCESSOR_ASSIGNED','PROCESSOR_CHANGED','STATUS_SWITCHED','TASK_TERMINATED') not null,
                              created_at datetime(6) not null,
                              updated_at datetime(6) not null,
                              primary key (task_history_id)
) engine=InnoDB;

alter table member
    add constraint UKmbmcqelty0fbrvxp1q58dn57t unique (email),
    add constraint UKhh9kg6jti4n1eoiertn2k6qsc unique (nickname);
alter table task_history
    add constraint UK4wh19gakb7pv1u0cseyw1yjmw unique (comment_id);
alter table api_log
    add constraint FKftyva6u4tm4iarfrjpbxyf4c9
        foreign key (member_id)
            references member (member_id);
alter table attachment
    add constraint FKliwb3s1jmhbcrq2upsyo2cftn
        foreign key (task_id)
            references task (task_id);
alter table category
    add constraint FKrny90rsn1w2b1ik1on39ucu7f
        foreign key (admin_id)
            references member (member_id),
    add constraint FKtrd7kl5dwdxbvra30i22nrpod
        foreign key (main_category_id)
            references category (category_id);
alter table comment
    add constraint FKmrrrpi513ssu63i2783jyiv9m
        foreign key (member_id)
            references member (member_id),
    add constraint FKfknte4fhjhet3l1802m1yqa50
        foreign key (task_id)
            references task (task_id);
alter table department
    add constraint FKetxumrlg416i5l73fd31axwi1
        foreign key (admin_id)
            references member (member_id);
alter table label
    add constraint FKgoxqkxje84e1jhc8weu7tdkee
        foreign key (admin_id)
            references member (member_id);
alter table member
    add constraint FKai3spm9ctynftc6y5u1ycq7po
        foreign key (admin_id)
            references member (member_id),
    add constraint FKlmd4h7lh9acdyvi0xxbvsqrmk
        foreign key (department_id)
            references department (department_id);
alter table notification
    add constraint FK1jpw68rbaxvu8u5l1dniain1l
        foreign key (receiver_id)
            references member (member_id),
    add constraint FKg6e8dcyvu9qdcfds2o3pj9qen
        foreign key (task_id)
            references task (task_id);
alter table task
    add constraint FKkjb4pwpo8oqc8fvkgbmiitsu9
        foreign key (category_id)
            references category (category_id),
    add constraint FKcvxhsvaa4b0eqvoknwdjoqb8e
        foreign key (label_id)
            references label (label_id),
    add constraint FK7h14q3t26nc05voash0c85a98
        foreign key (processor_id)
            references member (member_id),
    add constraint FKhmhrmkyhc8fnprgehf2tnqgxv
        foreign key (requester_id)
            references member (member_id),
    add constraint FK85w35u60hn4o1mpa8lo9ef2ae
        foreign key (reviewer_id)
            references member (member_id);
alter table task_history
    add constraint FK2ud4b2im20aa3smlseuca0br5
        foreign key (comment_id)
            references comment (comment_id),
    add constraint FK3rh6bjds4lcdwd25ya6dnvxwu
        foreign key (modified_member_id)
            references member (member_id),
    add constraint FKer57q2libi1e9njpj6faoxd2i
        foreign key (task_id)
            references task (task_id);