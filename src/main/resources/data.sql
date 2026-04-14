

-- INSERT INTO 없는테이블 VALUES (1);
INSERT INTO roles (role_name) VALUES ('ROLE_USER');
INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');



INSERT INTO users(login_id ,user_name, password, email, birth_day, created_at, role_id)
VALUES ( 'hong123','홍두깨', 'qwe123', 'test@naver.com', '1994-02-09', CURRENT_TIMESTAMP, 1);
