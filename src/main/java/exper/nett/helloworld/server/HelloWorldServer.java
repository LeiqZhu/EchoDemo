package exper.nett.helloworld.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloWorldServer {

	private int port;
	public HelloWorldServer(int port){
		this.port = port;
	}
	public static void main(String[] args) {
		int port;
		if (args.length>0) {
			port = Integer.valueOf(args[0]);
		}else{
			port = 8080;
		}
		new HelloWorldServer(port).start();
	}
	public void start(){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap sbs = new ServerBootstrap()
					.group(bossGroup,workGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
//							ch.pipeline().addLast("decoder",new StringDecoder());
//							ch.pipeline().addLast("encoder",new StringEncoder());
							ch.pipeline().addLast(new HelloWorldServerHandler());
						}
						
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = sbs.bind(port).sync();
			System.out.println("Server start listen at :" + port);
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
