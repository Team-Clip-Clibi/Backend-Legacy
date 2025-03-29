CREATE TABLE IF NOT EXISTS job
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    job_name VARCHAR(255) NULL,
    job_id   BIGINT NULL,
    CONSTRAINT pk_job PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS one_thing_matching
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_onethingmatching PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS one_thing_order
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    user_id     BIGINT NULL,
    status      VARCHAR(255) NULL,
    onething_id BIGINT NULL,
    CONSTRAINT pk_onethingorder PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS random_matching
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    city            VARCHAR(255) NULL,
    location        VARCHAR(255) NULL,
    restaurant_name VARCHAR(255) NULL,
    meeting_time    datetime NULL,
    CONSTRAINT pk_randommatching PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS random_order
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    user_id    BIGINT NULL,
    status     VARCHAR(255) NULL,
    random_id  BIGINT NULL,
    CONSTRAINT pk_randomorder PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS report
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    created_at      datetime NULL,
    updated_at      datetime NULL,
    user_id         BIGINT NULL,
    content         VARCHAR(500) NULL,
    report_category VARCHAR(255) NULL,
    is_confirmed    BIT(1) NULL,
    CONSTRAINT pk_report PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS terms_acceptance
(
    user_id              BIGINT NOT NULL,
    created_at           datetime NULL,
    updated_at           datetime NULL,
    service_permission   BIT(1) NULL,
    private_permission   BIT(1) NULL,
    marketing_permission BIT(1) NULL,
    CONSTRAINT pk_termsacceptance PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS token
(
    user_id       BIGINT NOT NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    refresh_token VARCHAR(255) NULL,
    CONSTRAINT pk_token PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS toss_payment
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    created_at            datetime NULL,
    updated_at            datetime NULL,
    payment_id            VARCHAR(255) NULL,
    order_id              BINARY(16)            NULL,
    amount                INT NULL,
    refund_amount         INT NULL,
    toss_payment_status   VARCHAR(255) NULL,
    currency              VARCHAR(255) NULL,
    method                VARCHAR(255) NULL,
    requested_at          datetime NULL,
    approved_at           datetime NULL,
    receipt_url           VARCHAR(255) NULL,
    json_response_payload VARCHAR(7000) NULL,
    tosspayment           BIGINT NULL,
    CONSTRAINT pk_tosspayment PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user
(
    id                              BIGINT AUTO_INCREMENT NOT NULL,
    created_at                      datetime NULL,
    updated_at                      datetime NULL,
    username                        VARCHAR(30) NULL,
    phone_number                    VARCHAR(255) NULL,
    nickname                        VARCHAR(255) NULL,
    birth                           date NULL,
    city                            VARCHAR(255) NULL,
    county                          VARCHAR(255) NULL,
    gender                          VARCHAR(255) NULL,
    platform                        VARCHAR(255) NULL,
    social_id                       VARCHAR(255) NULL,
    device_type                     VARCHAR(255) NULL,
    firebase_token                  VARCHAR(255) NULL,
    os_version                      VARCHAR(255) NULL,
    is_phone_num_verified           BIT(1) NULL,
    language                        VARCHAR(255) NULL,
    dietary_option                  VARCHAR(255) NULL,
    relationship_status             VARCHAR(255) NULL,
    is_same_relationship_considered BIT(1) NULL,
    is_allow_notify                 BIT(1) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_random_matching
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    user_id      BIGINT NULL,
    community_id BIGINT NULL,
    question     VARCHAR(255) NULL,
    CONSTRAINT pk_userrandommatching PRIMARY KEY (id)
);

ALTER TABLE random_order
    ADD CONSTRAINT uc_3882e5b66fb675690b349d180 UNIQUE (random_id, user_id);

ALTER TABLE one_thing_order
    ADD CONSTRAINT uc_fa4740598fdb3ead329df5f49 UNIQUE (onething_id, user_id);

ALTER TABLE user
    ADD CONSTRAINT uc_user_nickname UNIQUE (nickname);

ALTER TABLE user
    ADD CONSTRAINT uc_user_phonenumber UNIQUE (phone_number);

ALTER TABLE job
    ADD CONSTRAINT FK_JOB_ON_JOB FOREIGN KEY (job_id) REFERENCES user (id);

ALTER TABLE one_thing_order
    ADD CONSTRAINT FK_ONETHINGORDER_ON_ONETHING FOREIGN KEY (onething_id) REFERENCES one_thing_matching (id);

ALTER TABLE random_order
    ADD CONSTRAINT FK_RANDOMORDER_ON_RANDOM FOREIGN KEY (random_id) REFERENCES random_matching (id);

ALTER TABLE toss_payment
    ADD CONSTRAINT FK_TOSSPAYMENT_ON_TOSSPAYMENT FOREIGN KEY (tosspayment) REFERENCES one_thing_order (id);

ALTER TABLE toss_payment
    ADD CONSTRAINT FK_TOSSPAYMENT_ON_TOSSPAYMENTsRRsNC FOREIGN KEY (tosspayment) REFERENCES random_order (id);

ALTER TABLE user_random_matching
    ADD CONSTRAINT FK_USERRANDOMMATCHING_ON_COMMUNITY FOREIGN KEY (community_id) REFERENCES random_matching (id);

ALTER TABLE user_random_matching
    ADD CONSTRAINT FK_USERRANDOMMATCHING_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);