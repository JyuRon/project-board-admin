-- 테스트 계정
-- TODO: 테스트용이지만 비밀번호가 노출된 데이터 세팅. 개선하는 것이 좋을 지 고민해 보자.
insert into user_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by, modified_at, modified_by) values
('jyuka', '{noop}asdf1234', 'ADMIN', 'Jyuka', 'jyuka@mail.com', 'I am Jyuka.', now(), 'jyuka', now(), 'jyuka'),
('jyuka2', '{noop}asdf1234', 'MANAGER', 'Jyuka2', 'jyuka2@mail.com', 'I am Jyuka2.', now(), 'jyuka', now(), 'jyuka'),
('jyuka3', '{noop}asdf1234', 'MANAGER,DEVELOPER', 'Jyuka3', 'jyuka3@mail.com', 'I am Jyuka3.', now(), 'jyuka', now(), 'jyuka'),
('jyuka4', '{noop}asdf1234', 'USER', 'Jyuka4', 'jyuka4@mail.com', 'I am Jyuka4.', now(), 'jyuka', now(), 'jyuka')
;