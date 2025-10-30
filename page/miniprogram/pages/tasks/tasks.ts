// tasks.ts
import request from '../../utils/request';

Page({
  data: {
    currentTab: 0, // 0: 可领取, 1: 进行中, 2: 已完成
    isParent: true, // 是否为家长角色
    availableTasks: [],
    ongoingTasks: [],
    completedTasks: []
  },

  onLoad() {
    // 页面加载时获取任务列表
    this.loadTasks();
  },

  // 加载任务列表
  loadTasks() {
    // 调用后端API获取任务列表
    request.get('/api/task/list', {
      familyId: 'family_001', // 这里应该从全局状态或本地存储中获取
      userId: 'user_001', // 这里应该从全局状态或本地存储中获取
      status: 'available'
    }).then((res: any) => {
      if (res.code === 0) {
        this.setData({
          availableTasks: res.data
        });
      } else {
        wx.showToast({
          title: res.message || '获取任务列表失败',
          icon: 'none'
        });
      }
    }).catch((err: any) => {
      wx.showToast({
        title: '网络请求失败',
        icon: 'none'
      });
    });

    // 获取进行中的任务
    request.get('/api/task/list', {
      familyId: 'family_001',
      userId: 'user_001',
      status: 'ongoing'
    }).then((res: any) => {
      if (res.code === 0) {
        this.setData({
          ongoingTasks: res.data
        });
      } else {
        wx.showToast({
          title: res.message || '获取任务列表失败',
          icon: 'none'
        });
      }
    }).catch((err: any) => {
      wx.showToast({
        title: '网络请求失败',
        icon: 'none'
      });
    });

    // 获取已完成的任务
    request.get('/api/task/list', {
      familyId: 'family_001',
      userId: 'user_001',
      status: 'completed'
    }).then((res: any) => {
      if (res.code === 0) {
        this.setData({
          completedTasks: res.data
        });
      } else {
        wx.showToast({
          title: res.message || '获取任务列表失败',
          icon: 'none'
        });
      }
    }).catch((err: any) => {
      wx.showToast({
        title: '网络请求失败',
        icon: 'none'
      });
    });
  },

  // 切换任务标签
  switchTab(e: any) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      currentTab: tab
    });
  },

  // 领取任务
  claimTask(e: any) {
    const taskId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '领取任务',
      content: '确定要领取这个任务吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用后端API领取任务
          request.post('/api/task/claim', {
            taskId: taskId,
            userId: 'user_001' // 这里应该从全局状态或本地存储中获取
          }).then((res: any) => {
            if (res.code === 0) {
              wx.showToast({
                title: '任务领取成功',
                icon: 'success'
              });
              // 重新加载任务列表
              this.loadTasks();
            } else {
              wx.showToast({
                title: res.message || '任务领取失败',
                icon: 'none'
              });
            }
          }).catch((err: any) => {
            wx.showToast({
              title: '网络请求失败',
              icon: 'none'
            });
          });
        }
      }
    });
  },

  // 发布任务（仅家长）
  createTask() {
    wx.navigateTo({
      url: '/pages/task-create/task-create'
    });
  },

  // 返回上一页
  onBack() {
    wx.navigateBack();
  },

  // 图片加载错误处理
  onImageError(e: any) {
    console.log('图片加载失败', e);
    // 可以在这里设置默认图片或其他处理
  }
});