DELIMITER $$

CREATE TRIGGER employee_to_users
AFTER INSERT ON employees
FOR EACH ROW
BEGIN
INSERT INTO users(username,password,role)
VALUES(NEW.name,'emp123','employee');
END $$

DELIMITER ;