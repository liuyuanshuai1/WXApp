// 环境配置加载器
// 根据小程序的环境变量加载对应的配置文件

// 获取当前环境
const env = __wxConfig?.envVersion || 'develop';

let config;

// 根据环境加载对应的配置文件
switch (env) {
  case 'release': // 生产环境
    config = require('./env.prod.js');
    break;
  case 'trial': // 体验版
    config = require('./env.prod.js');
    break;
  case 'develop': // 开发环境
  default:
    config = require('./env.dev.js');
    break;
}

module.exports = config;