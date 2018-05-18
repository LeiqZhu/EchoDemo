package exper.nett.test;


import java.io.IOException;
import java.nio.channels.Selector;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClient {
    public static void main(String[] args) throws IOException {
        int port ;
        String host ;
        if (args.length >0) {
            port = Integer.parseInt(args[0]);
            host = args[1];
        }else {
            port = 8080;
            host = "localhost";
        }
        new Thread(new TimeClientHandler(host,port),"TimeClient-001").start();
    }
}
class TimeClientHandler implements  Runnable{
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private boolean stop;
    public TimeClientHandler(String host,int port) throws IOException {
        this.host = host==null? "127.0.0.1":host;
        this.port = port;
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
    }
    @Override
    public void run() {
        doConnect();
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()){
                        try{
                            handlerInput(key);
                        } catch (Exception e){
                            if (key!=null){
                                key.cancel();
                            }
                            if (key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            if (selector!=null){
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private  void doConnect(){
        try {
            if (socketChannel.connect(new InetSocketAddress(host,port))){
                socketChannel.register(selector,SelectionKey.OP_READ);
                doWrite(socketChannel);
            }else {
                socketChannel.register(selector,SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handlerInput(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (key.isValid()){
            if (key.isConnectable()){
                if (socketChannel.finishConnect()){
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    doWrite(socketChannel);
                }else {
                    System.exit(1);
                }
            }
            if (key.isReadable()){
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int read = socketChannel.read(readBuffer);
                if (read > 0){
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"utf-8");
                    System.out.println("Now is " + body);
                    this.stop = true;
                }else if (read < 0){
                    key.cancel();
                    socketChannel.close();
                }else
                    ;
            }
        }
    }
    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] bytes = "QUERY TIME ORDER ".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed");
        }
    }
}
