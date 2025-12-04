-- 1. 创建 Schema (防止第一次启动报错)
CREATE SCHEMA IF NOT EXISTS public;

-- 2. 尝试创建锁表 (如果表不存在，说明是第一次运行，建个表结构，防止下面 Update 报错)
-- 注意：这里只建结构，Liquibase 稍后会自己管理它，我们只是为了能运行 UPDATE 语句
CREATE TABLE IF NOT EXISTS public.DATABASECHANGELOGLOCK (
                                                            ID INT NOT NULL,
                                                            LOCKED BOOLEAN NOT NULL,
                                                            LOCKGRANTED TIMESTAMP,
                                                            LOCKEDBY VARCHAR(255),
                                                            CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID)
);

-- 3. 【暴力解锁】
-- 无论上次怎么挂的，启动时强制把锁设为 FALSE。
-- 如果表是空的(第一次)，这句不生效也没影响；如果表里有死锁记录，这就救命了。
MERGE INTO public.DATABASECHANGELOGLOCK KEY(ID) VALUES (1, FALSE, NULL, NULL);