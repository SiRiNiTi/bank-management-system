CREATE SEQUENCE TRANSACTION_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE NOCYCLE CACHE 20;
CREATE SEQUENCE ACCOUNTS_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE NOCYCLE CACHE 20;

CREATE TABLE IF NOT EXISTS public.clients (
	pin INT NOT NULL,
	name varchar(255) NOT NULL,
	CONSTRAINT pin_pk PRIMARY KEY (pin)
);

CREATE TABLE IF NOT EXISTS public.accounts (
	account_num INT NOT NULL DEFAULT (NEXT VALUE FOR ACCOUNTS_SEQ),
	current_balance INT NOT NULL DEFAULT 0,
	client_id INT NOT NULL,
	CONSTRAINT account_num_pk PRIMARY KEY (account_num),
	CONSTRAINT clients_fk FOREIGN KEY (client_id) REFERENCES clients(pin)
);

CREATE TABLE IF NOT EXISTS public.transaction_history (
	id INT NOT NULL DEFAULT (NEXT VALUE FOR TRANSACTION_SEQ),
	balance_before INT NOT NULL,
	balance_after INT NOT NULL,
	account_num INT NOT NULL,
	transaction_time timestamp NOT NULL DEFAULT now(),
	CONSTRAINT id_pk PRIMARY KEY (id),
	CONSTRAINT accounts_fk FOREIGN KEY (account_num) REFERENCES accounts(account_num)
);

