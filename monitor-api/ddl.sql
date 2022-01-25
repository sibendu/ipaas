DROP TABLE IF exists customer;

CREATE TABLE customer (
   customer_id INT NOT NULL,
   account_number VARCHAR ( 50 ) NOT NULL,
   first_name VARCHAR ( 50 ) NOT NULL,
   last_name VARCHAR ( 50 ) NOT NULL,
   company_name VARCHAR ( 50 ), 
   PRIMARY KEY (customer_id )
);

INSERT INTO customer (customer_id,account_number, first_name, last_name, company_name ) values (1, 'A1001', 'Sivaji', 'Saha', 'T');
INSERT INTO customer (customer_id,account_number,first_name, last_name, company_name ) values (2, 'B2001','Sibendu', 'Das', 'PH');
INSERT INTO customer (customer_id,account_number, first_name, last_name, company_name ) values (3, 'C3001', 'Snehashish', 'Chanda', 'T');

commit;

SELECT customer_id, account_number,first_name,last_name,company_name FROM customer;