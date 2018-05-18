package exper.nett.base.client;

import exper.nett.base.client.BaseClientHandler;
import io.netty.channel.EventLoopGroup;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class BaseClient {

	final static String HOST = System.getProperty("host","127.0.0.1");
	final static int PORT = Integer.parseInt(System.getProperty("port","8080"));
	final static int SIZE = Integer.parseInt(System.getProperty("size","256"));
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap()
				.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(2048));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast("encoder",new StringEncoder());
						ch.pipeline().addLast(new BaseClientHandler());
					}
				});
			
			ChannelFuture future = b.connect(HOST,PORT).sync();
			future.channel().writeAndFlush("Hello Netty Server ,I am a common Client");
			future.channel().closeFuture().sync();
		} 
//		catch (Exception e) {
//			group.shutdownGracefully();
//		}
		finally {
			group.shutdownGracefully();
		}
	}
}
