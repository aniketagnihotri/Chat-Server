import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Get the number of clients to allow at once.
        System.out.print("Enter the max number of clients to allow: ");
        Scanner scan = new Scanner(System.in);
        int numClients = scan.nextInt();

        // Create server instance and begin serving clients
        try {
            ChatServer newServer = new ChatServer(numClients);
            Thread listenerThread = new Thread(newServer);
            listenerThread.start();
        } catch (IOException e) {
            System.out.println("Server failed to start!");
            e.printStackTrace();
        }

    }
}
