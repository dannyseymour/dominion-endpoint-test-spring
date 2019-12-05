create sequence hibernate_sequence start with 1 increment by 1;
create table game
(
    id            integer   not null,
    created       timestamp not null,
    current_state varchar(255),
    current_turn  integer,
    player_1_uid  varchar(255),
    player_2_uid  varchar(255),
    players       binary(255),
    stacks        binary(255),
    state_machine longvarbinary,
    primary key (id)
);
create table play
(
    play_id     bigint  not null,
    card_bought varchar(255),
    card_played varchar(255),
    gold_spent  integer,
    player_name varchar(255),
    player_id   bigint  not null,
    turn_id     integer not null,
    primary key (play_id)
);
create table player
(
    player_id                       bigint not null,
    game_order                      integer,
    has_moat                        boolean,
    key                             varchar(255),
    uid                             varchar(255),
    cards_in_discard                binary(255),
    cards_in_draw_pile              binary(255),
    firebase_fcm_registration_token varchar(255),
    cards_in_hand                   binary(255),
    player_score                    integer,
    player_state                    varchar(255),
    primary key (player_id)
);
create table turn
(
    turn_id           integer not null,
    actions_remaining integer,
    buying_power      integer,
    buys_remaining    integer,
    did_attack        boolean,
    player_id         bigint  not null,
    primary key (turn_id)
);
alter table play
    add constraint FKp4hjpa775987xpe8j51vguueg foreign key (player_id) references player;
alter table play
    add constraint FKq68hhifeaymrgmuso3bubpbkq foreign key (turn_id) references turn;
alter table turn
    add constraint FKiwx8pi6kuyhg91dosr5mpbneq foreign key (player_id) references player;
