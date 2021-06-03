import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    private final Socket serverSocket;
    private boolean alive;

    public Client(Socket serverSocket, boolean alive) {
        this.serverSocket = serverSocket;
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            while (true) {
                if (br.ready()) {
                    System.out.println("Server says " + br.readLine());
                }
                if (!alive) {
                    br.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] Args) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Server hostname: ");
        String hostname = inputReader.readLine();
        System.out.print ("Server port number: ");
        String portNum = inputReader.readLine();
        int port = Integer.parseInt(portNum);
        Socket serverSocket;

        try {
            serverSocket = new Socket(hostname, port);
            System.out.println("\nClient-side socket has been initialized and a connection has been established!");

            // Create Server Reader
            Client client = new Client(serverSocket, true);
            Thread listenerThread = new Thread(client);
            listenerThread.start();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));

            System.out.println("You can now enter requests to the server. Type \"close\" to close the connection.");
            while (true) {
                try {
                    String request = inputReader.readLine();
                    if (request.equalsIgnoreCase("close")) {
                        bw.write("\r\n");
                        break;
                    }
                    bw.write("0" + request + "\n");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            client.setAlive(false);
            bw.close();
            serverSocket.close();
            listenerThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
