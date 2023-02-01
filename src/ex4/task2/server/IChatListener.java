package ex4.task2.server;

public interface IChatListener {
    public void onMessage(String fromUser, String message, boolean isAdmin);
    public String getUsername();
}
