package exper.nett.test.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String HOST = "localhost";
    private static int PORT = 8080;
    private Socket socket;
    public Client(){
        try {
            socket = new Socket(HOST,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        Scanner scanner = new Scanner(System.in);
        PrintWriter pw = null;
        BufferedReader br = null;
        try {
            pw = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8"
                        ),true
            );
            br = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream(),"utf-8"
                        )
            );
//                String input = scanner.next();
                pw.println("I have pan");
                String read = null;
                while((read=br.readLine()) !=null){
                    System.out.println("the cors recive from server : " + read);
                }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw!=null){
                pw.close();
                pw = null;
            }
            if (socket!=null){
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}

