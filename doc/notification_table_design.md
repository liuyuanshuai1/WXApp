# 通知系统表结构设计

## 通知表 (notification)

```sql
-- 通知表
CREATE TABLE notification (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
    family_id UUID REFERENCES family(id) ON DELETE CASCADE,
    type VARCHAR(32) NOT NULL, -- task_submitted | task_reviewed | reward_exchanged | reward_reviewed
    title VARCHAR(128) NOT NULL,
    content TEXT,
    status VARCHAR(16) NOT NULL DEFAULT 'unread', -- unread | read
    related_id UUID, -- 关联的ID（如任务ID、奖励ID等）
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_notification_user ON notification(user_id);
CREATE INDEX idx_notification_family ON notification(family_id);
CREATE INDEX idx_notification_status ON notification(status);
CREATE INDEX idx_notification_created ON notification(created_at);
```

## 表字段说明

| 字段名 | 类型 | 说明 |
|-------|------|-----|
| id | UUID | 主键 |
| user_id | UUID | 接收通知的用户ID |
| family_id | UUID | 家庭ID（可为空） |
| type | VARCHAR | 通知类型 |
| title | VARCHAR | 通知标题 |
| content | TEXT | 通知内容 |
| status | VARCHAR | 通知状态（未读/已读） |
| related_id | UUID | 关联ID（如任务ID、奖励ID等） |
| created_at | TIMESTAMPTZ | 创建时间 |

## 通知类型枚举

1. `task_submitted` - 任务已提交（通知给家长）
2. `task_reviewed` - 任务已审核（通知给孩子）
3. `reward_exchanged` - 奖励已兑换（通知给家长）
4. `reward_reviewed` - 奖励已审核（通知给孩子）