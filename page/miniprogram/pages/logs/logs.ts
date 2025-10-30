// logs.ts
import { formatTime } from '../../utils/util'
import request from '../../utils/request'

interface LogItem {
  id: string;
  userId: string;
  type: string;
  title: string;
  content: string;
  createdAt: string;
  typeText?: string;
}

Component({
  data: {
    logs: [] as LogItem[],
  },
  
  lifetimes: {
    attached() {
      // 页面加载时获取用户操作日志
      this.loadUserLogs();
    }
  },
  
  methods: {
    loadUserLogs() {
      // 从本地存储获取用户ID（实际项目中可能从全局状态获取）
      const userId = wx.getStorageSync('userId') || 'test-user-id';
      
      // 调用后端API获取用户操作日志
      request.get('/api/logs/user', {
        userId: userId
      }).then((res: any) => {
        if (res.code === 200) {
          const logs = res.data.map((item: any) => ({
            ...item,
            typeText: this.getLogTypeText(item.type),
            date: formatTime(new Date(item.createdAt))
          }));
          this.setData({ logs });
        } else {
          console.error('获取日志失败', res);
          wx.showToast({
            title: '获取日志失败',
            icon: 'none'
          });
        }
      }).catch((err) => {
        console.error('请求日志数据失败', err);
        // 如果请求失败，使用本地存储的日志作为备选
        this.loadLocalLogs();
      });
    },
    
    loadLocalLogs() {
      // 备选方案：使用本地存储的日志
      const localLogs = (wx.getStorageSync('logs') || []).map((log: string, index: number) => {
        return {
          id: `local-${index}`,
          userId: 'local-user',
          type: 'system',
          title: '本地日志',
          content: log,
          createdAt: log,
          date: formatTime(new Date(log)),
          typeText: '系统'
        }
      });
      this.setData({ logs: localLogs });
    },
    
    getLogTypeText(type: string): string {
      const typeMap: { [key: string]: string } = {
        'login': '登录',
        'logout': '登出',
        'create_task': '创建任务',
        'submit_task': '提交任务',
        'review_task': '审核任务',
        'create_reward': '创建奖励',
        'exchange_reward': '兑换奖励',
        'review_reward': '审核奖励',
        'create_family': '创建家庭',
        'join_family': '加入家庭',
        'leave_family': '退出家庭'
      };
      return typeMap[type] || '其他操作';
    }
  }
})
