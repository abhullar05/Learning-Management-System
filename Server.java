import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    public Server(Socket socket){
        this.socket = socket;
    }
    Socket socket; // Socket field
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000); // the server waits on port number : 8000 for clients to make connections
        System.out.println("Waiting for the client to connect on port number : 8000");
        // infinite server loop which accepts connection and spawns thread for each new client connected
        while(true) {
            Socket socket = serverSocket.accept(); // a socket is formed when the client connects
            System.out.println("Client connected!");
            Thread t = new Thread(new Server(socket)); // a thread is spawned
            t.start(); // thread starts execution
        }
    }
    // the run method contains everything to be implemented by the Threads
    public void run(){
        System.out.printf("connection received from %s\n", socket);

        try{
            PrintWriter pw = new PrintWriter(socket.getOutputStream()); // open print writer to write to client
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream())); // open buffered reader to read from client
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); // open object output stream to write objects to client
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); // open object input stream to read objects from client
            Login login = Login.loginServer(pw, bfr, oos, ois); // to handle login and create account server side stuff

            bfr.close(); // close buffered reader
            pw.close(); // close print writer
            oos.close(); // close object output stream
            ois.close(); // close object input stream

        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }






}
