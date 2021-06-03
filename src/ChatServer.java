import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

    private final int numClients;
    private final ServerSocket serverSocket;
    static Socket[] clientSockets;
//    final PipedOutputStream writeMessages;
//    final PipedInputStream readMessages;

    public ChatServer(int numClients) throws IOException {
        this.numClients = numClients;
        // Some firewalls may block certain ports.
        // Entering 0 for port number will find the first open port to broadcast on.
        this.serverSocket = new ServerSocket(0);
        this.clientSockets = new Socket[numClients];
        // Configure message streams for message dispatcher threads
//        this.writeMessages = new PipedOutputStream();
//        this.readMessages = new PipedInputStream(writeMessages);
    }

    // Client listener
    public void run() {
        Socket clientSocket;
        MessageListener handler;
        Thread handlerThread;
        int clientCounter = 0;

        System.out.println("The server has started and is serving clients on port " + this.serverSocket.getLocalPort() + ".");

        // Continuously listen for clients and create threads to handle connections
        while (true) {
            if (clientCounter < numClients) {
                try {
                    clientSocket = this.serverSocket.accept();
                    try {
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        bw.write("hi");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    clientSockets[clientCounter] = clientSocket;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                handler = new MessageListener(clientSocket, clientCounter);
                handlerThread = new Thread(handler);
                handlerThread.start();

                System.out.println("Client #" + clientCounter + " has connected.");
                clientCounter++;
            }
        }
    }
}
