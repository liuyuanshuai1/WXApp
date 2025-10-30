// rewards.ts
import request from '../../utils/request';

Page({
  data: {
    currentTab: 0, // 0: 可兑换, 1: 已兑换
    isParent: true, // 是否为家长角色
    availableRewards: [],
    exchangedRewards: []
  },

  onLoad() {
    // 页面加载时获取奖励列表
    this.loadRewards();
  },

  // 加载奖励列表
  loadRewards() {
    // 调用后端API获取可兑换奖励列表
    request.get('/api/reward/list', {
      familyId: 'family_001', // 这里应该从全局状态或本地存储中获取
      status: 'available'
    }).then((res: any) => {
      if (res.code === 0) {
        this.setData({
          availableRewards: res.data
        });
      } else {
        wx.showToast({
          title: res.message || '获取奖励列表失败',
          icon: 'none'
        });
      }
    }).catch((err: any) => {
      wx.showToast({
        title: '网络请求失败',
        icon: 'none'
      });
    });

    // 获取已兑换的奖励列表
    request.get('/api/reward/list', {
      familyId: 'family_001',
      status: 'exchanged'
    }).then((res: any) => {
      if (res.code === 0) {
        this.setData({
          exchangedRewards: res.data
        });
      } else {
        wx.showToast({
          title: res.message || '获取奖励列表失败',
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

  // 切换奖励标签
  switchTab(e: any) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      currentTab: tab
    });
  },

  // 兑换奖励
  exchangeReward(e: any) {
    const rewardId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '兑换奖励',
      content: '确定要兑换这个奖励吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用后端API兑换奖励
          request.post('/api/reward/exchange', {
            rewardId: rewardId,
            userId: 'user_001' // 这里应该从全局状态或本地存储中获取
          }).then((res: any) => {
            if (res.code === 0) {
              wx.showToast({
                title: '奖励兑换成功',
                icon: 'success'
              });
              // 重新加载奖励列表
              this.loadRewards();
            } else {
              wx.showToast({
                title: res.message || '奖励兑换失败',
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

  // 发布奖励（仅家长）
  createReward() {
    wx.navigateTo({
      url: '/pages/reward-create/reward-create'
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