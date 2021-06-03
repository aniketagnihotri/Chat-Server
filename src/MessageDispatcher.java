import java.io.*;

class MessageDispatcher implements Runnable {

    private final Message message;

    public MessageDispatcher(Message message) {
        this.message = message;
    }

    public void run() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ChatServer.clientSockets[this.message.getToID()].getOutputStream()));
            bw.write(this.message.getFromID() + message.getText() + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}