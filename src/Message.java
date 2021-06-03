public class Message {

    private int fromID;
    private int toID;
    private String text;

    public Message(int fromID, int toID, String text) {
        this.fromID = fromID;
        this.toID = toID;
        this.text = text;
    }

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getToID() {
        return toID;
    }

    public void setToID(int toID) {
        this.toID = toID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
