import java.io.*;
import java.net.Socket;
import java.util.Objects;

class MessageListener implements Runnable {

    private final Socket clientSocket;
    private final int connectionNumber;

    public MessageListener(Socket clientSocket, int connectionNumber) {
        Objects.requireNonNull(clientSocket, "The provided client socket is NULL.");

        this.clientSocket = clientSocket;
        this.connectionNumber = connectionNumber;
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
                        // Organize received StringBuilder message into message object
                        Message message = new Message(this.connectionNumber, Character.getNumericValue(receivedMessage.charAt(0)),
                                receivedMessage.substring(1, messageLength));
                        // Create object and thread to dispatch client message
                        MessageDispatcher  messageDispatcher = new MessageDispatcher(message);
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
            System.out.println("Client #" + connectionNumber + " has disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() throws IOException {
        ChatServer.clientSockets[this.connectionNumber] = null;
        this.clientSocket.close();
    }

}