-- 测试，删除表避免重复数据
DROP TABLE IF EXISTS users;
-- 创建表
CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, age INTEGER);
-- 插入数据<增>
INSERT INTO users (name, age) VALUES ('Tom', 25);
INSERT INTO users (name, age) VALUES ('Mike', 30);
INSERT INTO users (name, age) VALUES ('Mary', 35);
-- 删除数据<删>
DELETE FROM users WHERE id = 3;
-- 修改数据<改>
UPDATE users SET age = 40 WHERE id = 2;
-- 显示控制
.mode column
.headers on
.width 10 20 20
-- 查询数据<查>
SELECT * FROM users;
SELECT * FROM users WHERE id = 1;
SELECT * FROM users WHERE name = 'Mike';
-- 查看数据库信息
.databases
-- 查看表信息
.tables
-- 显示表结构
.schema
--退出
.quit
