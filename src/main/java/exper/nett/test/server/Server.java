package exper.nett.test.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    private static ServerSocket serverSocket;
    private static Socket socket;
    public Server(){
    }
    public void start(){
        try {
            ServerExecutePool pool = new ServerExecutePool(50,10000);
            serverSocket = new ServerSocket(8080);
            System.out.println("ServerSocket start listen at ");
            while(true){
                socket = serverSocket.accept();
                System.out.println(socket.getInetAddress() + "has connect");
                pool.execute(new ThreadHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
class ThreadHandler implements  Runnable{

    private Socket socket;
    public ThreadHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream(),"utf-8"
                        )
            );
            pw = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8"
                        ),true);
            String input = null;
            while((input=br.readLine()) !=null){
                System.out.println(input);
                pw.println(socket.getInetAddress() + " send :" + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br!=null){
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                }
            }
            if (pw !=null){
                pw.close();
                pw = null;
            }
            if (socket !=null){
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
class ServerExecutePool{
    private int maxPoolSize;
    private int blockQueueSize;
    private ExecutorService executorPool;
    public ServerExecutePool(int maxPoolSize,int blockQueueSize){
        executorPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                                                maxPoolSize,120L,
                                                    TimeUnit.SECONDS,
                                                        new ArrayBlockingQueue<Runnable>(blockQueueSize));
    }
    public void execute(Runnable task){
        executorPool.execute(task);
    }
}
