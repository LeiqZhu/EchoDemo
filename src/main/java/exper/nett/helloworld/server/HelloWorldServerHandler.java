package exper.nett.helloworld.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class HelloWorldServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
//		System.out.println("server channelRead....");
//		System.out.println(ctx.channel().remoteAddress() + "->server : " + msg.toString());
//		ctx.write("server reply : " + msg + "------recived cors");
//		ctx.flush();
		ByteBuf buf = (ByteBuf) msg;
		try {
			while(buf.isReadable()){
//				System.out.println(buf.toString());
				System.out.println((char)buf.readByte());
				System.out.flush();
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
