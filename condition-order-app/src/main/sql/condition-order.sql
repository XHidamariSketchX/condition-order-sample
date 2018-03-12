DROP TABLE IF EXISTS `condition_order`;
CREATE TABLE `condition_order` (
  `order_id`             BIGINT PRIMARY KEY,
  `user_id`              INT            NOT NULL,
  `customer_no`          VARCHAR(12)    NOT NULL,
  `is_deleted`           BIT            NOT NULL,
  `order_state`          INT            NOT NULL,
  `security_type`        INT            NOT NULL,
  `security_code`        CHAR(6)        NOT NULL,
  `security_exchange`    CHAR(2)        NOT NULL,
  `security_name`        VARCHAR(4)     NOT NULL,
  `strategy_type`        INT            NOT NULL,
  `condition_properties` TEXT,
  `dynamic_properties`   TEXT,
  `expire_time`          DATETIME,
  `exchange_type`        INT            NOT NULL,
  `entrust_strategy`     INT            NOT NULL,
  `entrust_method`       INT            NOT NULL,
  `entrust_amount`       DECIMAL(13, 2) NOT NULL,
  `delay_confirm_option` INT            NOT NULL,
  `delay_confirm_times`  INT            NOT NULL,
  `create_time`          TIMESTAMP DEFAULT current_timestamp(),
  `update_time`          TIMESTAMP DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  INDEX `index_customer_no` (customer_no)
);

DROP TABLE IF EXISTS `entrust_order`;
CREATE TABLE `entrust_order` (
  `entrust_id`        BIGINT PRIMARY KEY,
  `order_id`          BIGINT,
  `user_id`           INT           NOT NULL,
  `customer_no`       VARCHAR(12)   NOT NULL,
  `security_type`     INT           NOT NULL,
  `security_code`     CHAR(6)       NOT NULL,
  `security_exchange` CHAR(2)       NOT NULL,
  `security_name`     VARCHAR(4)    NOT NULL,
  `exchange_type`     INT           NOT NULL,
  `entrust_price`     DECIMAL(6, 2) NOT NULL,
  `entrust_number`    INT           NOT NULL,
  `order_type`        INT           NOT NULL,
  `entrust_state`     INT           NOT NULL,
  `entrust_message`   VARCHAR(64)   NOT NULL,
  `entrust_code`      INT           NOT NULL,
  `create_time`       TIMESTAMP DEFAULT current_timestamp(),
  `update_time`       TIMESTAMP DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  INDEX `index_order_id` (order_id),
  INDEX `index_customer_no` (customer_no)
);

