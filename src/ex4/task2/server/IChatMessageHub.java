package ex4.task2.server;

public interface IChatMessageHub {
    public void addChatListener(IChatListener listener);
    public void removeChatListener(IChatListener listener);
    public void publish(String fromUser, String message, boolean isAdmin);
}
