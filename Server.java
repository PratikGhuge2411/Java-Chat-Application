import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    ServerSocket server;
    Socket socket;

    BufferedReader br; // data read
    PrintWriter out; // data write

    public Server() {
        try {
            server = new ServerSocket(8081);
            System.out.println("Server is ready to accept conection");
            System.out.println("waiting");
           
            socket = server.accept();
     if(socket != null)
     {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            System.out.println("connection established with client");
            
     }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started..");

            try {
                while (socket != null && !socket.isClosed()) {

                    String msg = br.readLine();
                    if (msg == null || msg.equals("exit")) {
                        System.out.println("client terminated the chat");

                        socket.close();
                        break;
                    }

                    System.out.println("Client : " + msg);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started..");
            try {
                while (true) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                   while(socket != null && !socket.isClosed())
                   {
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {

        System.out.println("Server is running...");
        Server server = new Server();
        server.startReading();
        server.startWriting();
    }
}