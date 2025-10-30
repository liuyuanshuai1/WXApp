import WebSocketManager from '../../utils/websocket';
import request from '../../utils/request';
const config = require('../../utils/config.js');

Page({
  data: {
    notifications: [] as any[],
    userId: '',
    socketTask: null as any
  },

  onLoad() {
    // 获取用户ID（实际项目中可能从全局状态或本地存储获取）
    const userId = wx.getStorageSync('userId') || 'test-user-id';
    this.setData({ userId });
    
    // 初始化WebSocket连接
    this.initWebSocket();
    
    // 加载通知数据
    this.loadNotifications();
  },

  initWebSocket() {
    const that = this;
    // 使用后端WebSocket地址
    const socketUrl = `${config.WEBSOCKET_URL}?userId=${this.data.userId}`;
    
    // 使用WebSocket工具类
    WebSocketManager.init(socketUrl, (message: any) => {
      console.log('收到WebSocket消息', message);
      
      // 处理连接状态消息
      if (message.type === 'connection') {
        switch (message.status) {
          case 'open':
            wx.showToast({
              title: '通知服务连接成功',
              icon: 'success'
            });
            break;
          case 'error':
            wx.showToast({
              title: '通知服务连接错误',
              icon: 'none'
            });
            break;
          case 'close':
            wx.showToast({
              title: '通知服务连接已关闭',
              icon: 'none'
            });
            break;
          case 'reconnect_failed':
            wx.showToast({
              title: '通知服务重连失败',
              icon: 'none'
            });
            break;
        }
        return;
      }
      
      // 处理心跳消息
      if (message.type === 'heartbeat') {
        console.log('收到心跳响应', message);
        return;
      }
      
      // 处理通知消息
      that.handleNotification(message);
    });
  },

  loadNotifications() {
    // 从后端API加载通知数据
    request.get('/notifications').then((res: any) => {
      if (res.code === 200) {
        // 格式化日期
        const notifications = res.data.map((item: any) => ({
          ...item,
          formattedDate: this.formatDate(item.createdAt)
        }));
        this.setData({ notifications });
      } else {
        console.error('加载通知数据失败', res);
        wx.showToast({
          title: '加载通知数据失败',
          icon: 'none'
        });
      }
    }).catch((err: any) => {
      console.error('加载通知数据请求失败', err);
      wx.showToast({
        title: '网络请求失败',
        icon: 'none'
      });
    });
  },

  formatDate(timestamp: string) {
    const date = new Date(timestamp);
    const now = new Date();
    
    // 如果是今天，显示时间
    if (date.toDateString() === now.toDateString()) {
      return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    }
    
    // 如果是今年，显示月日
    if (date.getFullYear() === now.getFullYear()) {
      return `${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
    }
    
    // 其他情况显示完整日期
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
  },

  handleNotification(message: any) {
    // 将新通知添加到列表顶部
    const newNotification = {
      ...message,
      formattedDate: this.formatDate(message.createdAt)
    };
    
    const notifications = [newNotification, ...this.data.notifications];
    this.setData({ notifications });
    
    // 显示通知提醒
    wx.showToast({
      title: '收到新通知',
      icon: 'none'
    });
  },

  markAsRead(e: any) {
    const id = e.currentTarget.dataset.id;
    const notifications = this.data.notifications.map(item => {
      if (item.id === id) {
        return { ...item, status: 'read' };
      }
      return item;
    });
    
    this.setData({ notifications });
    
    // 调用后端API更新状态
    request.put(`/notifications/${id}/read`).then((res: any) => {
      if (res.code === 200) {
        console.log('标记为已读成功', id);
      } else {
        console.error('标记为已读失败', res);
      }
    }).catch((err: any) => {
      console.error('标记为已读请求失败', err);
    });
  },

  onUnload() {
    // 页面卸载时关闭WebSocket连接
    WebSocketManager.close();
  }
});