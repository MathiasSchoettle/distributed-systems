package ex6.client;

public interface IMessageSender {
    void openChatConnection(String username, String registry, String bindingName, IMessageGui messageGui);
    void sendChatMessage(String message);
    void closeChatConnection();
}
