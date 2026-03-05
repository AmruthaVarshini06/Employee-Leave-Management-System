DELIMITER $$

CREATE TRIGGER admin_to_users
AFTER INSERT ON admin
FOR EACH ROW
BEGIN
INSERT INTO users(username,password,role)
VALUES(NEW.username, NEW.password, NEW.role);
END $$

DELIMITER ;