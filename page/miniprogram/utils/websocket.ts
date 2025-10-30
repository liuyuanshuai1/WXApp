// WebSocket 工具类
const config = require('./config.js');

class WebSocketManager {
  private static instance: WebSocketManager;
  private socketTask: any = null;
  private isConnect = false;
  private connectUrl = '';
  private messageCallback: Function | null = null;
  private reconnectTimer: number | null = null;
  private reconnectCount = 0;
  private maxReconnectCount = 5;
  private heartbeatTimer: number | null = null;
  private heartbeatInterval = 30000; // 30秒心跳间隔

  // 单例模式
  public static getInstance(): WebSocketManager {
    if (!this.instance) {
      this.instance = new WebSocketManager();
    }
    return this.instance;
  }

  // 初始化WebSocket连接
  public init(url: string, callback: Function): void {
    this.connectUrl = url;
    this.messageCallback = callback;
    this.connect();
  }

  // 建立连接
  private connect(): void {
    if (this.isConnect) return;

    // 清除之前的连接
    if (this.socketTask) {
      this.socketTask.close();
    }

    this.socketTask = wx.connectSocket({
      url: this.connectUrl,
      success: () => {
        console.log('WebSocket连接成功');
      },
      fail: (err) => {
        console.error('WebSocket连接失败', err);
        this.reconnect();
      }
    });

    this.bindEvents();
  }

  // 绑定事件
  private bindEvents(): void {
    this.socketTask.onOpen(() => {
      console.log('WebSocket连接已打开');
      this.isConnect = true;
      this.reconnectCount = 0;
      
      // 启动心跳机制
      this.startHeartbeat();
      
      // 发送连接成功事件
      if (this.messageCallback) {
        this.messageCallback({ type: 'connection', status: 'open' });
      }
    });

    this.socketTask.onMessage((res: any) => {
      console.log('收到服务器消息', res);
      if (this.messageCallback) {
        try {
          const message = typeof res.data === 'string' ? JSON.parse(res.data) : res.data;
          this.messageCallback(message);
        } catch (error) {
          console.error('解析WebSocket消息失败', error);
        }
      }
    });

    this.socketTask.onError((err: any) => {
      console.error('WebSocket连接错误', err);
      this.isConnect = false;
      
      // 发送错误事件
      if (this.messageCallback) {
        this.messageCallback({ type: 'connection', status: 'error', error: err });
      }
      
      this.reconnect();
    });

    this.socketTask.onClose(() => {
      console.log('WebSocket连接已关闭');
      this.isConnect = false;
      
      // 停止心跳
      this.stopHeartbeat();
      
      // 发送关闭事件
      if (this.messageCallback) {
        this.messageCallback({ type: 'connection', status: 'close' });
      }
      
      this.reconnect();
    });
  }

  // 发送消息
  public send(data: any): boolean {
    if (!this.isConnect) {
      console.warn('WebSocket未连接，无法发送消息');
      return false;
    }

    const message = typeof data === 'string' ? data : JSON.stringify(data);
    this.socketTask.send({
      data: message,
      success: () => {
        console.log('消息发送成功', message);
      },
      fail: (err: any) => {
        console.error('消息发送失败', err);
      }
    });
    
    return true;
  }

  // 发送心跳
  private sendHeartbeat(): void {
    this.send({ type: 'heartbeat', timestamp: Date.now() });
  }

  // 启动心跳
  private startHeartbeat(): void {
    this.stopHeartbeat();
    this.heartbeatTimer = setInterval(() => {
      this.sendHeartbeat();
    }, this.heartbeatInterval);
  }

  // 停止心跳
  private stopHeartbeat(): void {
    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer);
      this.heartbeatTimer = null;
    }
  }

  // 重新连接
  private reconnect(): void {
    if (this.reconnectCount >= this.maxReconnectCount) {
      console.error('WebSocket重连次数已达上限');
      // 发送重连失败事件
      if (this.messageCallback) {
        this.messageCallback({ type: 'connection', status: 'reconnect_failed' });
      }
      return;
    }

    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
    }

    this.reconnectTimer = setTimeout(() => {
      this.reconnectCount++;
      console.log(`WebSocket尝试重连(${this.reconnectCount}/${this.maxReconnectCount})`);
      this.connect();
    }, 3000);
  }

  // 关闭连接
  public close(): void {
    if (this.socketTask) {
      this.socketTask.close();
    }
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
    }
    this.stopHeartbeat();
    this.isConnect = false;
    this.reconnectCount = 0;
  }

  // 获取连接状态
  public isConnected(): boolean {
    return this.isConnect;
  }
}

export default WebSocketManager.getInstance();