非常好 👍 下面是为 **PostgreSQL** 优化过的完整数据库建表脚本（适配你当前的 Spring Boot + MyBatis Plus 项目环境），保留原有的业务逻辑结构，但使用 PostgreSQL 特性进行改写，包括：

- ✅ 字段类型改为 `uuid`, `varchar`, `text`, `boolean`, `timestamp with time zone` 等；
- ✅ 默认使用 `uuid_generate_v4()` 自动生成主键（需启用扩展）；
- ✅ 外键约束 + 索引结构保持一致；
- ✅ 兼容 MyBatis Plus 常用风格；
- ✅ 所有时间字段均为 `timestamptz` 类型。

------

## 🧩 一、启用 UUID 扩展

在执行建表脚本之前，务必先启用 PostgreSQL 的 UUID 扩展：

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

------

## 🧱 二、数据库表结构（PostgreSQL 版本）

```sql
-- 切换或创建数据库
-- CREATE DATABASE family_points WITH ENCODING 'UTF8';
-- \c family_points;

-- 用户表（登录用户）
CREATE TABLE user_account (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    open_id VARCHAR(128),
    nickname VARCHAR(64),
    avatar_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (open_id)
);

-- 家庭表
CREATE TABLE family (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(128) NOT NULL,
    code VARCHAR(16) NOT NULL,
    owner_id UUID NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
    max_members INT NOT NULL DEFAULT 6,
    code_expire_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (code)
);

CREATE INDEX idx_family_owner ON family(owner_id);

-- 成员表
CREATE TABLE member (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    family_id UUID NOT NULL REFERENCES family(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
    role VARCHAR(16) NOT NULL DEFAULT 'child',
    nickname VARCHAR(64),
    joined_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_member_family ON member(family_id);
CREATE INDEX idx_member_user ON member(user_id);

-- 任务表
CREATE TABLE task (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    family_id UUID NOT NULL REFERENCES family(id) ON DELETE CASCADE,
    title VARCHAR(128) NOT NULL,
    description TEXT,
    points INT NOT NULL DEFAULT 0,
    type VARCHAR(16) DEFAULT 'normal',      -- normal | daily
    repeatable BOOLEAN DEFAULT FALSE,
    deadline TIMESTAMPTZ,
    status VARCHAR(16) DEFAULT 'active',    -- active | inactive
    created_by UUID REFERENCES user_account(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_task_family ON task(family_id);

-- 任务提交表
CREATE TABLE task_submission (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    task_id UUID NOT NULL REFERENCES task(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
    proof_text TEXT,
    proof_image_urls JSONB,   -- 存 JSON 数组
    status VARCHAR(16) NOT NULL DEFAULT 'pending', -- pending|approved|rejected
    remark VARCHAR(255),
    submitted_at TIMESTAMPTZ DEFAULT NOW(),
    reviewed_at TIMESTAMPTZ,
    reviewer_id UUID REFERENCES user_account(id)
);

CREATE INDEX idx_submission_task ON task_submission(task_id);
CREATE INDEX idx_submission_user ON task_submission(user_id);

-- 奖励表
CREATE TABLE reward (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    family_id UUID NOT NULL REFERENCES family(id) ON DELETE CASCADE,
    title VARCHAR(128) NOT NULL,
    description TEXT,
    type VARCHAR(32) DEFAULT 'physical', -- physical|virtual|privilege
    points_required INT NOT NULL DEFAULT 0,
    stock INT NOT NULL DEFAULT 0,
    status VARCHAR(16) DEFAULT 'on_shelf', -- on_shelf|off_shelf
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_reward_family ON reward(family_id);

-- 兑换申请表
CREATE TABLE exchange (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    reward_id UUID NOT NULL REFERENCES reward(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
    family_id UUID NOT NULL REFERENCES family(id) ON DELETE CASCADE,
    status VARCHAR(16) NOT NULL DEFAULT 'pending', -- pending|approved|rejected
    remark VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    reviewed_at TIMESTAMPTZ,
    reviewer_id UUID REFERENCES user_account(id)
);

CREATE INDEX idx_exchange_user ON exchange(user_id);
CREATE INDEX idx_exchange_reward ON exchange(reward_id);

-- 积分流水表
CREATE TABLE points_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
    family_id UUID REFERENCES family(id) ON DELETE CASCADE,
    type VARCHAR(16) NOT NULL, -- income | expense
    source VARCHAR(32) NOT NULL, -- task | reward | manual
    amount INT NOT NULL,
    remark VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_points_user ON points_log(user_id);
CREATE INDEX idx_points_family ON points_log(family_id);

-- 用户积分余额表（可做视图或物化表）
CREATE TABLE user_balance (
    user_id UUID PRIMARY KEY REFERENCES user_account(id) ON DELETE CASCADE,
    balance INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

------

## ⚙️ 三、PostgreSQL 特性说明

| 特性           | 用法说明                                                     |
| -------------- | ------------------------------------------------------------ |
| **UUID 生成**  | 通过 `uuid_generate_v4()` 自动生成唯一主键                   |
| **时间字段**   | 全部用 `TIMESTAMPTZ`，自动存储 UTC 时间并带时区信息          |
| **JSONB**      | 用于 `proof_image_urls` 等字段，更方便查询与索引             |
| **索引**       | 已对高频字段（family_id, user_id, reward_id 等）建立索引     |
| **事务安全性** | 建议在兑换与任务审核时使用 `SELECT ... FOR UPDATE` 锁行操作  |
| **兼容性**     | 结构完全兼容 MyBatis Plus（UUID 主键类型推荐使用 `@TableId(type = IdType.ASSIGN_UUID)`） |

------

## 🔒 四、积分扣减事务（PostgreSQL 示例）

```sql
BEGIN;

-- 1. 锁定用户余额行
SELECT balance FROM user_balance WHERE user_id = 'user-uuid' FOR UPDATE;

-- 2. 检查积分是否足够
-- 3. 检查库存
UPDATE reward SET stock = stock - 1 WHERE id = 'reward-uuid' AND stock > 0;
-- 若行数为 0 表示库存不足，应 ROLLBACK

-- 4. 写入兑换记录
INSERT INTO exchange (reward_id, user_id, family_id, status) VALUES ('reward-uuid', 'user-uuid', 'family-uuid', 'pending');

-- 5. 扣减积分（或审核后再扣）
UPDATE user_balance SET balance = balance - 50, updated_at = NOW() WHERE user_id = 'user-uuid';

-- 6. 写入积分流水
INSERT INTO points_log (user_id, family_id, type, source, amount, remark)
VALUES ('user-uuid', 'family-uuid', 'expense', 'reward', 50, '兑换奖励');

COMMIT;
```

------

是否希望我继续帮你生成：

1. ✅ **对应的实体类（Java + MyBatis Plus 注解版）**，或
2. ✅ **建表后初始化测试数据 SQL（方便调试用）**？

你告诉我选哪个（或都要），我马上为 PostgreSQL 版本继续生成。