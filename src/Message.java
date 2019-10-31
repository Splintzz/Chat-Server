public class Message {
    private MessageType messageType;
    private String message;
    private int clientNumber;

    public Message() {
        messageType = null;
        message = "";
    }

    public Message(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }
}
