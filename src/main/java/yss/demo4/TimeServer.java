package yss.demo4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {
		public void bind(int port) throws Exception {
			/**
			 * EventLoopGroup 是个线程组，它包含了一组NIO线程，专门用于网络事件的处理
			 * reactor线程组
			 */
			//配置服务端的NIO线程组
			//用于服务端接收客户端的连接
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			//用于进行SocketChannel的网络读写
			EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//Netty 用于启动NIO服务端的辅助启动类
			ServerBootstrap b =new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			//设置创建的Channel为NioServerSocketChannel,它的功能对应JDK NIO类库中的ServcerSocketChannel
				.channel(NioServerSocketChannel.class)
				//配置NioServerSocketChannel 的TCP参数
				//此处将他的backlog设置为1024
				.option(ChannelOption.SO_BACKLOG, 1024)
				//绑定I/O事件的处理类，例如对消息进行解码，记录日志
				.childHandler(new ChildChannelHandler());
			/**
			 * 调用bind方法，绑定监听端口
			 * sync调用同步阻塞方法，等待绑定操作完成
			 * 绑定完成之后返回ChannelFuture，用于异步操作的通知回调
			 */
			//绑定端口，同步等待成功
			ChannelFuture f=b.bind(port).sync();
			/**
			 * 阻塞方法，等待服务器链路关闭之后mian函数才会退出
			 */
			//等待服务端监听端口关闭
			f.channel().closeFuture().sync();
			
			
		}finally {
			//优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
		
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// TODO Auto-generated method stub
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
			arg0.pipeline().addLast(new StringDecoder());
			arg0.pipeline().addLast(new TimeServerHandler());
		}
	}
	
	public static void main(String[] args)   throws Exception {
		int port=8081;
		if(args !=null && args.length>0) {
			try{
				port = Integer.valueOf(args[0]);
			}catch (NumberFormatException e) {
				//采用默认值
				// TODO: handle exception
			}
		}
		
		new TimeServer().bind(port);
	}
}
