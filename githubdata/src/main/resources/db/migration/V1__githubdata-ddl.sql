create table if not exists `github_repo_info`
(
    `id`          bigint(20)         not null auto_increment comment '主键',
    `status`      int                not null default 1  comment 'status',
    `name`   varchar(200)       not null default '' comment '仓库名',
    `username`   varchar(200)       not null default '' comment '用户名',
    `description`  varchar(200)       not null default '' comment '仓库描述',
    `repo_created`  datetime        comment '仓库创建时间',
    `repo_updated`  datetime         comment '仓库更新时间',
    `repo_push`  datetime         comment '上次push时间',
    `language`  varchar(200)       not null default '' comment '仓库语言',
    `size`      integer       comment '仓库大小',
    `license`  varchar(200)       not null default '' comment '仓库license',
    `create_time` datetime           not null default current_timestamp comment '创建时间',
    `modify_time` datetime           not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`),
    unique key (`name`,`username`)
) engine = innodb
  auto_increment = 1
  default charset = utf8 comment ='github仓库信息';

create table if not exists `github_repo_contributor`
(
    `id`          bigint(20)         not null auto_increment comment '主键',
    `status`      int                not null default 1  comment 'status',
    `name`   varchar(200)       not null default '' comment '用户名',
    `repo_id`   bigint(20)  comment '仓库id',
    `contributions` int not null default 0 comment '用户贡献',
    `create_time` datetime           not null default current_timestamp comment '创建时间',
    `modify_time` datetime           not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`),
    foreign key (`repo_id`) references github_repo_info(id)
    on delete cascade
) engine = innodb
  auto_increment = 1
  default charset = utf8 comment ='github仓库贡献者信息';

create table if not exists `github_repo_comment`
(
    `id`          bigint(20)         not null auto_increment comment '主键',
    `status`      int                not null default 1  comment 'status',
    `user_id`   bigint(20)       comment '用户id',
    `repo_id`   bigint(20)  comment '仓库id',
    `commit_id` varchar(200) not null default '' comment 'commit id',
    `comment_created`  datetime        comment '仓库创建时间',
    `comment_updated`  datetime         comment '仓库更新时间',
    `line`      int     comment '行数',
    `path`      varchar(200)    comment '路径',
    `body`      longtext comment '评论内容',
    `create_time` datetime           not null default current_timestamp comment '创建时间',
    `modify_time` datetime           not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`),
    foreign key (`repo_id`) references github_repo_info(id)
        on delete cascade
) engine = innodb
  auto_increment = 1
  default charset = utf8 comment ='github仓库评论信息';

create table if not exists `github_user`
(
    `id`          bigint(20)         not null auto_increment comment '主键',
    `status`      int                not null default 1  comment 'status',
    `username`     varchar(40)      not null default '' comment '用户名',
    `joining_time`  datetime    comment '加入时间',
    `stars`     blob  comment '获得星情况',
    `repo_number` int   not null default 0 comment '仓库数',
    `followering_number`    int not null default 0 comment '用户follow的人数',
    `followers_number`  int not null default 0 comment 'follow此用户的人数',
    `language`  blob comment '用户语言情况',
    `repos_info` blob comment '用户仓库信息',
    `received_events`   blob comment '最近事件',
    `create_time` datetime           not null default current_timestamp comment '创建时间',
    `modify_time` datetime           not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`)
) engine = innodb
  auto_increment = 1
  default charset = utf8 comment ='github用户信息';

create table if not exists `github_follow`
(
    `id`          bigint(20)         not null auto_increment comment '主键',
    `status`      int                not null default 1  comment 'status',
    `user_id`     bigint(20)        not null default 0 comment '用户id',
    `following`   bigint(20)        not null default 0 comment 'follow的id',
    `create_time` datetime           not null default current_timestamp comment '创建时间',
    `modify_time` datetime           not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`),
    unique key (`user_id`,`following`),
    foreign key (`user_id`) references github_user(id)
    on delete cascade,
    foreign key (`following`) references github_user(id)
    on delete cascade
)engine = innodb
 auto_increment = 1
 default charset = utf8 comment ='github用户follow信息';