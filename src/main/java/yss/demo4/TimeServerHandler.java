package yss.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerHandler extends ChannelHandlerAdapter {

	private int counter;
	/**
	 * 继承ChannelHandlerAdapter，用于对网络事件的读写操作
	 * 通常我们只需要关注channelRead、exceptionCaught
	 * 
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		/*//将msg转换为netty的ByteBuf对象
		ByteBuf buf = (ByteBuf) msg;
		*//**
		 * 获取缓冲区可读的字节数buf.readableBytes()
		 * 根据可读字节数创建byte数组
		 *//*
		byte[] req = new byte[buf.readableBytes()];
		*//**
		 * 通过buf的readBytes将缓冲区的字节数组复制到新建的byte数组中
		 *//*
		buf.readBytes(req);
		//通过String的构造函数获取请求消息
		String body = new String(req,"UTF-8").substring(0,req.length-System.getProperty("line.separator").length());*/
		String body =String.valueOf(msg);
		System.out.println("the time server receive order : "+body
				+"; the counter is :"+ ++counter);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?
				new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		//通过ChannelHandlerContext的write方法异步应答消息给客户端
		ctx.writeAndFlush(resp);
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		/**
		 * 当发生异常时，关闭ChannelHandlerContext,释放ChannelHanlderContext相关联的句柄等资源
		 */
		ctx.close();
	}

/*
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		*//**
		 * ChannelHandlerContext的flush方法，它的作用是将消息发送队列中的消息写入到SocketChannel中发送给对方
		 * 为了防止频繁的唤醒seleteor进行消息发送，netty的write方法不直接将消息写入SocketChannel，
		 * 调用write只是把待发送的的消息放入发生缓冲数组中，再调用flush方法，将发送缓冲区的的消息全部写到SocketChannel中。
		 *//*
		ctx.flush();
	}*/
		
}
