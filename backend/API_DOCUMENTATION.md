# FamilyPoints 后端 API 文档

## 1. 用户相关接口 (UserAccountController)

### 1.1 测试接口
- **接口地址**: `GET /api/user/test`
- **接口描述**: 测试端点，不需要认证
- **请求参数**: 无
- **返回结果**: 测试成功信息

### 1.2 微信登录接口
- **接口地址**: `POST /api/user/login/wechat`
- **接口描述**: 微信登录接口
- **请求参数**: 
  - `code` (String): 微信登录code
- **返回结果**: 登录结果

### 1.3 获取用户信息接口
- **接口地址**: `GET /api/user/info`
- **接口描述**: 获取用户信息接口
- **请求参数**: 无（从JWT中获取用户ID）
- **返回结果**: 用户信息

### 1.4 更新用户信息接口
- **接口地址**: `PUT /api/user/info`
- **接口描述**: 更新用户信息接口
- **请求参数**: 
  - `UserAccount` (JSON): 用户信息对象
- **返回结果**: 更新后的用户信息

## 2. 家庭相关接口 (FamilyController)

### 2.1 创建家庭接口
- **接口地址**: `POST /api/family`
- **接口描述**: 创建家庭接口
- **请求参数**: 
  - `CreateFamilyRequest` (JSON): 创建家庭请求对象
- **返回结果**: 创建结果

### 2.2 加入家庭接口
- **接口地址**: `POST /api/family/join`
- **接口描述**: 加入家庭接口
- **请求参数**: 
  - `JoinFamilyRequest` (JSON): 加入家庭请求对象
- **返回结果**: 加入结果

### 2.3 获取家庭信息接口
- **接口地址**: `GET /api/family/{familyId}`
- **接口描述**: 获取家庭信息接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（路径参数）
- **返回结果**: 家庭信息

### 2.4 获取用户的家庭列表接口
- **接口地址**: `GET /api/family/user/list`
- **接口描述**: 获取用户的家庭列表接口
- **请求参数**: 无（从JWT中获取用户ID）
- **返回结果**: 家庭列表

### 2.5 获取家庭成员列表接口
- **接口地址**: `GET /api/family/{familyId}/members`
- **接口描述**: 获取家庭成员列表接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（路径参数）
- **返回结果**: 成员列表

### 2.6 退出家庭接口
- **接口地址**: `DELETE /api/family/{familyId}/leave`
- **接口描述**: 退出家庭接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（路径参数）
- **返回结果**: 退出结果

### 2.7 生成新家庭码接口
- **接口地址**: `POST /api/family/{familyId}/code/generate`
- **接口描述**: 生成新家庭码接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（路径参数）
- **返回结果**: 新家庭码

## 3. 任务相关接口 (TaskController)

### 3.1 创建任务接口
- **接口地址**: `POST /api/task`
- **接口描述**: 创建任务接口
- **请求参数**: 
  - `CreateTaskRequest` (JSON): 创建任务请求对象
  - `familyId` (Long): 家庭ID（查询参数）
- **返回结果**: 创建的任务

### 3.2 获取任务详情接口
- **接口地址**: `GET /api/task/{taskId}`
- **接口描述**: 获取任务详情接口
- **请求参数**: 
  - `taskId` (Long): 任务ID（路径参数）
- **返回结果**: 任务详情

### 3.3 获取家庭任务列表接口
- **接口地址**: `GET /api/task/family/list`
- **接口描述**: 获取家庭任务列表接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
  - `status` (Integer, 可选): 任务状态（查询参数）
- **返回结果**: 任务列表

### 3.4 获取用户可执行任务列表接口
- **接口地址**: `GET /api/task/available/list`
- **接口描述**: 获取用户可执行任务列表接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
- **返回结果**: 可执行任务列表

### 3.5 提交任务接口
- **接口地址**: `POST /api/task/submit`
- **接口描述**: 提交任务接口
- **请求参数**: 
  - `SubmitTaskRequest` (JSON): 提交任务请求对象
- **返回结果**: 提交记录

### 3.6 审核任务提交接口
- **接口地址**: `POST /api/task/review`
- **接口描述**: 审核任务提交接口
- **请求参数**: 
  - `ReviewTaskRequest` (JSON): 审核任务请求对象
- **返回结果**: 审核结果

### 3.7 获取任务提交记录接口
- **接口地址**: `GET /api/task/submission/{submissionId}`
- **接口描述**: 获取任务提交记录接口
- **请求参数**: 
  - `submissionId` (Long): 提交记录ID（路径参数）
- **返回结果**: 提交记录详情

### 3.8 获取任务的所有提交记录接口
- **接口地址**: `GET /api/task/{taskId}/submissions`
- **接口描述**: 获取任务的所有提交记录接口
- **请求参数**: 
  - `taskId` (Long): 任务ID（路径参数）
- **返回结果**: 提交记录列表

### 3.9 获取用户的任务提交记录接口
- **接口地址**: `GET /api/task/user/submissions`
- **接口描述**: 获取用户的任务提交记录接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
- **返回结果**: 用户的任务提交记录列表

### 3.10 更新任务接口
- **接口地址**: `PUT /api/task/{taskId}`
- **接口描述**: 更新任务接口
- **请求参数**: 
  - `taskId` (Long): 任务ID（路径参数）
  - `CreateTaskRequest` (JSON): 更新任务请求对象
- **返回结果**: 更新后的任务

### 3.11 删除任务接口
- **接口地址**: `DELETE /api/task/{taskId}`
- **接口描述**: 删除任务接口
- **请求参数**: 
  - `taskId` (Long): 任务ID（路径参数）
- **返回结果**: 删除结果

## 4. 奖励相关接口 (RewardController)

### 4.1 创建奖励接口
- **接口地址**: `POST /api/reward`
- **接口描述**: 创建奖励接口
- **请求参数**: 
  - `CreateRewardRequest` (JSON): 创建奖励请求对象
  - `familyId` (Long): 家庭ID（查询参数）
- **返回结果**: 创建的奖励

### 4.2 获取奖励详情接口
- **接口地址**: `GET /api/reward/{rewardId}`
- **接口描述**: 获取奖励详情接口
- **请求参数**: 
  - `rewardId` (Long): 奖励ID（路径参数）
- **返回结果**: 奖励详情

### 4.3 获取家庭奖励列表接口
- **接口地址**: `GET /api/reward/family/list`
- **接口描述**: 获取家庭奖励列表接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
  - `status` (Integer, 可选): 奖励状态（查询参数）
- **返回结果**: 奖励列表

### 4.4 获取用户可兑换奖励列表接口
- **接口地址**: `GET /api/reward/available/list`
- **接口描述**: 获取用户可兑换奖励列表接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
- **返回结果**: 可兑换奖励列表

### 4.5 兑换奖励接口
- **接口地址**: `POST /api/reward/exchange`
- **接口描述**: 兑换奖励接口
- **请求参数**: 
  - `ExchangeRewardRequest` (JSON): 兑换奖励请求对象
- **返回结果**: 兑换记录

### 4.6 审核奖励兑换接口
- **接口地址**: `POST /api/reward/review`
- **接口描述**: 审核奖励兑换接口
- **请求参数**: 
  - `ReviewExchangeRequest` (JSON): 审核兑换请求对象
- **返回结果**: 审核结果

### 4.7 获取奖励兑换记录接口
- **接口地址**: `GET /api/reward/exchange/{exchangeId}`
- **接口描述**: 获取奖励兑换记录接口
- **请求参数**: 
  - `exchangeId` (Long): 兑换记录ID（路径参数）
- **返回结果**: 兑换记录详情

### 4.8 获取奖励的所有兑换记录接口
- **接口地址**: `GET /api/reward/{rewardId}/exchanges`
- **接口描述**: 获取奖励的所有兑换记录接口
- **请求参数**: 
  - `rewardId` (Long): 奖励ID（路径参数）
- **返回结果**: 兑换记录列表

### 4.9 获取用户的奖励兑换记录接口
- **接口地址**: `GET /api/reward/user/exchanges`
- **接口描述**: 获取用户的奖励兑换记录接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
- **返回结果**: 用户的奖励兑换记录列表

### 4.10 获取用户积分余额接口
- **接口地址**: `GET /api/reward/points/balance`
- **接口描述**: 获取用户积分余额接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
- **返回结果**: 积分余额

### 4.11 更新奖励接口
- **接口地址**: `PUT /api/reward/{rewardId}`
- **接口描述**: 更新奖励接口
- **请求参数**: 
  - `rewardId` (Long): 奖励ID（路径参数）
  - `CreateRewardRequest` (JSON): 更新奖励请求对象
- **返回结果**: 更新后的奖励

### 4.12 删除奖励接口
- **接口地址**: `DELETE /api/reward/{rewardId}`
- **接口描述**: 删除奖励接口
- **请求参数**: 
  - `rewardId` (Long): 奖励ID（路径参数）
- **返回结果**: 删除结果

### 4.13 获取用户积分历史记录接口
- **接口地址**: `GET /api/reward/points/logs`
- **接口描述**: 获取用户积分历史记录接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
  - `page` (Integer, 默认1): 页码（查询参数）
  - `size` (Integer, 默认10): 每页大小（查询参数）
- **返回结果**: 积分历史记录列表

### 4.14 根据类型获取用户积分历史记录接口
- **接口地址**: `GET /api/reward/points/logs/type`
- **接口描述**: 根据类型获取用户积分历史记录接口
- **请求参数**: 
  - `familyId` (Long): 家庭ID（查询参数）
  - `type` (Integer): 积分类型（查询参数）
  - `page` (Integer, 默认1): 页码（查询参数）
  - `size` (Integer, 默认10): 每页大小（查询参数）
- **返回结果**: 积分历史记录列表

## 5. 积分历史相关接口 (PointsHistoryController)

### 5.1 获取用户积分历史记录接口
- **接口地址**: `GET /api/points/logs`
- **接口描述**: 获取用户积分历史记录接口
- **请求参数**: 
  - `userId` (Long): 用户ID（查询参数）
  - `familyId` (Long): 家庭ID（查询参数）
  - `page` (Integer, 默认1): 页码（查询参数）
  - `size` (Integer, 默认20): 每页大小（查询参数）
- **返回结果**: 积分历史记录列表

### 5.2 根据积分类型获取用户积分历史记录接口
- **接口地址**: `GET /api/points/logs/type`
- **接口描述**: 根据积分类型获取用户积分历史记录接口
- **请求参数**: 
  - `userId` (Long): 用户ID（查询参数）
  - `familyId` (Long): 家庭ID（查询参数）
  - `type` (Integer): 积分类型（查询参数）
  - `page` (Integer, 默认1): 页码（查询参数）
  - `size` (Integer, 默认20): 每页大小（查询参数）
- **返回结果**: 积分历史记录列表

## 通用响应格式

所有API接口都返回统一的JSON格式响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

其中：
- `code`: 状态码，200表示成功，其他值表示失败
- `message`: 响应消息
- `data`: 实际数据内容

## 认证说明

除以下公开接口外，所有接口都需要在请求头中包含JWT Token：

- `GET /api/user/test`
- `POST /api/user/login/wechat`

Token应放在请求头的Authorization字段中，格式为：
```
Authorization: Bearer <token>
```