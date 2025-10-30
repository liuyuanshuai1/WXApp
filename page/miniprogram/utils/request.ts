// 网络请求工具类
const config = require('./config.js');
const BASE_URL = config.BASE_API_URL;

// 请求封装
const request = (options: any) => {
  const {
    url,
    method = 'GET',
    data = {},
    header = {},
    success,
    fail,
    complete
  } = options;

  // 获取存储的token
  const token = wx.getStorageSync('token');

  // 设置请求头
  const defaultHeader: any = {
    'Content-Type': 'application/json',
    ...header
  };

  // 如果有token，则添加到请求头
  if (token) {
    defaultHeader['Authorization'] = `Bearer ${token}`;
  }

  // 发起请求
  return wx.request({
    url: BASE_URL + url,
    method,
    data,
    header: defaultHeader,
    success: (res: any) => {
      // 处理响应
      if (res.statusCode === 200) {
        // 成功响应
        if (success) success(res.data);
      } else if (res.statusCode === 401) {
        // 未授权，可能需要重新登录
        wx.showToast({
          title: '登录已过期，请重新登录',
          icon: 'none'
        });
        // 可以在这里添加重新登录的逻辑
        if (fail) fail(res);
      } else {
        // 其他错误
        wx.showToast({
          title: res.data.message || '请求失败',
          icon: 'none'
        });
        if (fail) fail(res);
      }
    },
    fail: (err: any) => {
      // 网络错误
      wx.showToast({
        title: '网络请求失败',
        icon: 'none'
      });
      if (fail) fail(err);
    },
    complete
  });
};

// GET请求
const get = (url: string, data = {}, header = {}) => {
  return new Promise((resolve, reject) => {
    request({
      url,
      method: 'GET',
      data,
      header,
      success: resolve,
      fail: reject
    });
  });
};

// POST请求
const post = (url: string, data = {}, header = {}) => {
  return new Promise((resolve, reject) => {
    request({
      url,
      method: 'POST',
      data,
      header,
      success: resolve,
      fail: reject
    });
  });
};

// PUT请求
const put = (url: string, data = {}, header = {}) => {
  return new Promise((resolve, reject) => {
    request({
      url,
      method: 'PUT',
      data,
      header,
      success: resolve,
      fail: reject
    });
  });
};

// DELETE请求
const del = (url: string, data = {}, header = {}) => {
  return new Promise((resolve, reject) => {
    request({
      url,
      method: 'DELETE',
      data,
      header,
      success: resolve,
      fail: reject
    });
  });
};

export default {
  request,
  get,
  post,
  put,
  delete: del
};