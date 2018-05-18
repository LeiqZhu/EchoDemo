package exper.nett.base.server;

import java.net.InetSocketAddress;


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
import io.netty.handler.codec.string.StringEncoder;

public class BaseServer {

	private int port;
	public BaseServer(int port) {
		this.port = port;
	}
	public void start(){
		EventLoopGroup workgroup = new NioEventLoopGroup();
		EventLoopGroup bossgroup = new NioEventLoopGroup();
		try {
			ServerBootstrap sbs = new ServerBootstrap()
				.group(workgroup,bossgroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast("encoder",new StringEncoder());
						ch.pipeline().addLast(new BaseServerHandler());
					}
				}).option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = sbs.bind(port).sync();
			System.out.println("Server start listen at : " + port);
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			workgroup.shutdownGracefully();
			bossgroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}else {
			port = 8080;
		}
		new BaseServer(port).start();
	}
}
