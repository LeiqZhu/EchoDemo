package exper.nett.echo.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
	private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public static void main(String[] args) throws Exception {

        new EchoClient("127.0.0.1",8080).start();
    }

    public void start() throws Exception {
    	EventLoopGroup group = new NioEventLoopGroup();
        try {    //  创建Bootstrap
            Bootstrap b = new Bootstrap();     // 指定EventLoopGroup以处理客户端事件；需要适用于NIO的实现
            	b.group(group)
            	.channel(NioSocketChannel.class)
            	.remoteAddress(new InetSocketAddress(host,port))//设置服务器的InetSocketAddr-ess![](/api/storage/getbykey/screenshow?key=17043add7e9c14a5d3f7)                \
            	.handler(new ChannelInitializer<SocketChannel>() {//在创建Channel时，向ChannelPipeline中添加一个Echo-ClientHandler实例
                 @Override
                public void initChannel(SocketChannel ch) throws Exception {
                   ch.pipeline().addLast(
                        new EchoClientHandler());
                }
                });
            ChannelFuture future = b.connect().sync();    //  连接到远程节点，阻塞等待直到连接完成
            future.channel().closeFuture().sync();      //  阻塞，直到Channel关闭
        }finally{
            group.shutdownGracefully().sync();      //  关闭线程池并且释放所有的资源
        }
    }
}
