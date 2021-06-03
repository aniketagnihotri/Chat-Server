import java.io.*;

class MessageDispatcher implements Runnable {

    private final int connectionNumber;
    private final String message;

    public MessageDispatcher(int connectionNumber, String message) {
        this.connectionNumber = connectionNumber;
        this.message = message;
    }

    public void run() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ChatServer.clientSockets[this.connectionNumber].getOutputStream()));
            bw.write(message + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}