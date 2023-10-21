SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE table clients;
TRUNCATE table accounts;
TRUNCATE table transaction_history;
SET REFERENTIAL_INTEGRITY TRUE;

alter sequence ACCOUNTS_SEQ restart start with 1;
alter sequence TRANSACTION_SEQ restart start with 1;