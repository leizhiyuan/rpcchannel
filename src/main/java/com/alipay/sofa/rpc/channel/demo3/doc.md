1. 启动服务端
2. 设置客户端启动参数 -XX:MaxDirectMemorySize=100M
3. debug客户端。断点打在 io.netty.buffer.AbstractByteBufAllocator.ioBuffer(int) 即可
4. 如果要测试Unpool,客户端的代码中取消注释掉 .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)