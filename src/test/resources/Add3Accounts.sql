insert into clients (name, pin)
values
('Ivan Ivanov', 1234),
('Fedor Fedorov',4444);

insert into accounts (client_id, current_balance)
values
(1234, 50000),
(1234, 4000),
(4444, 10000);

