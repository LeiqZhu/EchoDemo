package exper.nett.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>	{
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
//		Scanner scanner = new Scanner(System.in);
//    	String iString = scanner.next();
//    	ctx.writeAndFlush(iString);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",     //当被通知Channel是活跃的时候，发送一条消息
        CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println(    // 记录已接收消息的转储
            "Client received: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,     //在发生异常时，记录错误并关闭Channel 
        Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
