package exper.nett.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class TimerServer {
    private ServerSocketChannel serverSocketChannel;
    private String host;
    private int port;
    public TimerServer(){

    }

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        String host = "127.0.0.1";
        int port = 8080;
        if (args != null && args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        MutiplexerTimeServer timeServer = new MutiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MutiplexerTimeServer-001").start();
    }
}
class ReactorTask implements Runnable{

        @Override
        public void run() {
        }
}
class MutiplexerTimeServer implements  Runnable{
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;
    public MutiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port),1024);
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void stop(){
        this.stop = true;
    }
    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                SelectionKey key = null;
                while(iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try{
                        handlerInput(key);

                    }catch (Exception e){
                        if (key!=null){
                            key.cancel();
                            if (key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void handlerInput(SelectionKey key){
        if (key.isValid()){
            if (key.isAcceptable()){
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (key.isReadable()){
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                try {
                    int readBytes = socketChannel.read(readBuffer);
                    if (readBytes>0){
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes,"utf-8");
                        System.out.println("The time server receive the order " + body);
                        String currentTime = "QUERY TIME ORDER ".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                        doWrite(socketChannel,currentTime);
                    } else if (readBytes<0){
                        key.cancel();
                        socketChannel.close();
                    } else
                        ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private  void doWrite(SocketChannel socketChannel,String response){
        if (response !=null && response.trim().length() > 0){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            try {
                socketChannel.write(writeBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
