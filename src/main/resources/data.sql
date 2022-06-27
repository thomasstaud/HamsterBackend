INSERT INTO roles (role_id, role) VALUES (1, 'USER');
INSERT INTO roles (role_id, role) VALUES (2, 'TEACHER');
INSERT INTO roles (role_id, role) VALUES (3, 'DEV');
INSERT INTO roles (role_id, role) VALUES (4, 'ADMIN');
INSERT INTO users (username, password, active) VALUES ("admin", "$2a$10$rT/P78i/z1CYvzxdBrs.R.zMsin6HLvMwue0OQhKwihkPB9gS9OUa", true); 
INSERT into user_role (user_id, role_id) VALUES (1, 4); 