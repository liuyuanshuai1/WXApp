// family.ts
import request from '../../utils/request';

Page({
  data: {
    familyInfo: null, // 家庭信息
    familyMembers: [], // 家庭成员列表
    isParent: true // 是否为家长角色
  },

  onLoad() {
    // 页面加载时获取家庭信息
    this.loadFamilyInfo();
  },

  // 加载家庭信息
  loadFamilyInfo() {
    // 这里应该调用后端API获取家庭信息
    // 暂时使用模拟数据
    const mockFamilyInfo = {
      id: 'family_001',
      name: '元帅之家',
      memberCount: 3,
      totalPoints: 1500,
      myRole: '家长'
    };

    const mockMembers = [
      {
        id: 'member_001',
        nickname: '元帅爸爸',
        role: '家长',
        points: 800,
        avatarUrl: ''
      },
      {
        id: 'member_002',
        nickname: '元帅妈妈',
        role: '家长',
        points: 700,
        avatarUrl: ''
      },
      {
        id: 'member_003',
        nickname: '小元帅',
        role: '孩子',
        points: 0,
        avatarUrl: ''
      }
    ];

    this.setData({
      familyInfo: mockFamilyInfo,
      familyMembers: mockMembers
    });
  },

  // 创建家庭
  createFamily() {
    wx.showModal({
      title: '创建家庭',
      editable: true,
      placeholderText: '请输入家庭名称',
      success: (res) => {
        if (res.confirm) {
          const familyName = res.content;
          if (familyName) {
            // 调用后端API创建家庭
            request.post('/api/family/create', {
              familyName: familyName
            }).then((res: any) => {
              if (res.code === 0) {
                wx.showToast({
                  title: '创建家庭成功',
                  icon: 'success'
                });
                // 重新加载家庭信息
                this.loadFamilyInfo();
              } else {
                wx.showToast({
                  title: res.message || '创建家庭失败',
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
      }
    });
  },

  // 加入家庭
  joinFamily() {
    wx.showModal({
      title: '加入家庭',
      editable: true,
      placeholderText: '请输入家庭邀请码',
      success: (res) => {
        if (res.confirm) {
          const familyCode = res.content;
          if (familyCode) {
            // 调用后端API加入家庭
            request.post('/api/family/join', {
              familyCode: familyCode
            }).then((res: any) => {
              if (res.code === 0) {
                wx.showToast({
                  title: '加入家庭成功',
                  icon: 'success'
                });
                // 重新加载家庭信息
                this.loadFamilyInfo();
              } else {
                wx.showToast({
                  title: res.message || '加入家庭失败',
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
      }
    });
  },

  // 查看家庭成员
  viewMembers() {
    wx.showToast({
      title: '查看成员功能待实现',
      icon: 'none'
    });
  },

  // 退出家庭
  leaveFamily() {
    wx.showModal({
      title: '退出家庭',
      content: '确定要退出当前家庭吗？',
      success: (res) => {
        if (res.confirm) {
          // 这里应该调用后端API退出家庭
          wx.showToast({
            title: '退出家庭功能待实现',
            icon: 'none'
          });
        }
      }
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