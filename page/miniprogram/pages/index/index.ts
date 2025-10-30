// index.ts
import request from '../../utils/request';

Page({
  data: {
    userInfo: {
      avatarUrl: '',
      nickName: ''
    },
    hasUserInfo: false,
    canIUseGetUserProfile: false,
    canIUseNicknameComp: false,
    familyInfo: {
      name: '我的家庭',
      points: 0,
      role: '家长'
    },
    tasks: {
      available: 0,
      completed: 0
    },
    rewards: {
      available: 0,
      redeemed: 0
    }
  },

  onLoad() {
    // 检查是否支持 wx.getUserProfile
    if (typeof wx.getUserProfile === 'function') {
      this.setData({
        canIUseGetUserProfile: true
      });
    }
    
    // 检查是否支持 wx.chooseAvatar (需要基础库版本2.21.2及以上)
    // @ts-ignore
    if (typeof wx.chooseAvatar === 'function') {
      this.setData({
        canIUseNicknameComp: true
      });
    }
    
    // 检查是否已登录
    this.checkLoginStatus();
  },

  // 检查登录状态
  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    if (token) {
      // 已登录，获取用户信息
      this.getUserInfo();
      // 获取家庭和任务信息
      this.getFamilyInfo();
      this.getTasksInfo();
      this.getRewardsInfo();
    } else {
      // 未登录，显示登录按钮
      this.setData({
        hasUserInfo: false
      });
    }
  },

  // 获取用户信息
  getUserInfo() {
    request.get('/user/info').then((res: any) => {
      if (res.code === 200) {
        this.setData({
          userInfo: res.data,
          hasUserInfo: true
        });
      } else {
        console.error('获取用户信息失败:', res.message);
      }
    }).catch((err: any) => {
      console.error('获取用户信息异常:', err);
    });
  },

  // 获取家庭信息
  getFamilyInfo() {
    request.get('/family/info').then((res: any) => {
      if (res.code === 200) {
        this.setData({
          'familyInfo.name': res.data.name,
          'familyInfo.points': res.data.points,
          'familyInfo.role': res.data.role
        });
      } else {
        console.error('获取家庭信息失败:', res.message);
      }
    }).catch((err: any) => {
      console.error('获取家庭信息异常:', err);
    });
  },

  // 获取任务信息
  getTasksInfo() {
    request.get('/task/stats').then((res: any) => {
      if (res.code === 200) {
        this.setData({
          'tasks.available': res.data.available,
          'tasks.completed': res.data.completed
        });
      } else {
        console.error('获取任务信息失败:', res.message);
      }
    }).catch((err: any) => {
      console.error('获取任务信息异常:', err);
    });
  },

  // 获取奖励信息
  getRewardsInfo() {
    request.get('/reward/stats').then((res: any) => {
      if (res.code === 200) {
        this.setData({
          'rewards.available': res.data.available,
          'rewards.redeemed': res.data.redeemed
        });
      } else {
        console.error('获取奖励信息失败:', res.message);
      }
    }).catch((err: any) => {
      console.error('获取奖励信息异常:', err);
    });
  },

  // 获取用户头像和昵称
  onGetUserProfile() {
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: (res) => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        });
        
        // 将用户信息发送到后端
        request.post('/user/update', {
          avatarUrl: res.userInfo.avatarUrl,
          nickName: res.userInfo.nickName
        }).then((response: any) => {
          if (response.code !== 200) {
            console.error('更新用户信息失败:', response.message);
          }
        }).catch((err: any) => {
          console.error('更新用户信息异常:', err);
        });
      },
      fail: (err) => {
        console.error('获取用户信息失败:', err);
      }
    });
  },

  // 跳转到家庭页面
  viewFamilies() {
    wx.navigateTo({
      url: '/pages/family/family'
    });
  },

  // 跳转到任务页面
  viewTasks() {
    wx.navigateTo({
      url: '/pages/tasks/tasks'
    });
  },

  // 跳转到奖励页面
  viewRewards() {
    wx.navigateTo({
      url: '/pages/rewards/rewards'
    });
  },

  // 创建家庭
  createFamily() {
    wx.navigateTo({
      url: '/pages/family/family?action=create'
    });
  },

  // 加入家庭
  joinFamily() {
    wx.navigateTo({
      url: '/pages/family/family?action=join'
    });
  }
});
