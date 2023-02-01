package ex4.task2.client;

public interface IMessageSender {
    public void openChatConnection(String username, String host, int port, IMessageGui messageGui);
    public void sendChatMessage(String message);
    public void closeChatConnection();
}
