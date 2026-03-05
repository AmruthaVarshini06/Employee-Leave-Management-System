CREATE TABLE employees (
    emp_id VARCHAR(20) NOT NULL,
    name VARCHAR(50) NOT NULL,
    department VARCHAR(50),
    email VARCHAR(50),
    mobile VARCHAR(15),
    salary DOUBLE,
    date_of_joining DATE,
    PRIMARY KEY (emp_id)
);