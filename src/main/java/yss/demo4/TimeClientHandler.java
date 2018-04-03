package yss.demo4;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter {
	private static Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

	private int counter;
	
	private byte[] req;
	
	public TimeClientHandler() {
		req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
		/*firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);*/
		
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx)   {
		// TODO Auto-generated method stub
		ByteBuf message = null;
		for(int i=0;i<100;i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
		
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = new String(req,"UTF-8");
		System.out.println("Now is : "+body+" ; the counter is "+ ++counter);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warning("Unexpected exception from downstream:"+cause.getMessage());
		ctx.close();
	}


}
