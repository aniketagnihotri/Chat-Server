import java.io.*;
import java.net.Socket;
import java.util.Objects;

class MessageListener implements Runnable {

    private final Socket clientSocket;
    private final int CONNECTION_NUMBER;
    private final StringBuilder CLIENT_EXIT_STRING = new StringBuilder("close");

    public MessageListener(Socket clientSocket, int connectionNumber) {
        Objects.requireNonNull(clientSocket, "The provided client socket is NULL.");

        this.clientSocket = clientSocket;
        this.CONNECTION_NUMBER = connectionNumber;
    }

    public void run() {
        int maxMessageLength = 256;
        try {
            // Create BufferedReader, StringBuilder for message, and messageLength counter to monitor total message length
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder receivedMessage = new StringBuilder(256);
            int messageLength = 0;
            // While loop to continuously read messages from client
            while (true) {
                char readCharacter = (char) br.read();
                if (readCharacter == '\r') {
                    break;
                } else if (readCharacter == '\n' || messageLength == maxMessageLength) {
                    if (messageLength != 0) {
                        // Create object and thread to dispatch client message
                        MessageDispatcher  messageDispatcher = new MessageDispatcher(Character.getNumericValue(receivedMessage.charAt(0)),
                                receivedMessage.substring(1, messageLength));
                        Thread dispatcherThread = new Thread(messageDispatcher);
                        dispatcherThread.start();
                    }
                    // Reset variables for next message
                    messageLength = 0;
                    // Create new message, allow GC to recover unused memory
                    receivedMessage = new StringBuilder(256);
                    continue;
                }
                // Default case: insert character into StringBuilder message object
                receivedMessage.insert(messageLength, readCharacter);
                messageLength++;
            }
            br.close();
            closeConnection();
            System.out.println("Client #" + CONNECTION_NUMBER + " has disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() throws IOException {
        ChatServer.clientSockets[this.CONNECTION_NUMBER] = null;
        this.clientSocket.close();
    }

}