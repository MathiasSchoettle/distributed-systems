package ex4.task2.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

public class ChatServer implements IChatMessageHub {
    private final Map<String, IChatListener> listeners = new HashMap<>();
    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public synchronized void addChatListener(IChatListener listener) {
        if (this.listeners.containsKey(listener.getUsername())) {
            System.out.println("Duplicate username " + listener.getUsername());
            listener.onMessage(null, "Duplicate username " + listener.getUsername() + ".", true);
            listener.onMessage(null, "Please restart and try again!", true);
            listener.onMessage(null, "EXIT", true);
            return;
        }
        this.listeners.put(listener.getUsername(), listener);

        String message = "Welcome " + listener.getUsername() + "!";
        this.publishPrivate(listener.getUsername(), message, true);

        Set<String> usernames = this.listeners.keySet();
        if (usernames.size() <= 1)
            this.publishPrivate(listener.getUsername(), "Chat room is empty, you are the first member.", true);
        else {
            StringBuilder members = new StringBuilder();
            members.append("Users online: ");
            int count = 2;
            for (String user : usernames) {
                if (user.equals(listener.getUsername()))
                    continue;
                members.append(user);
                if (usernames.size() > count++)
                    members.append(", ");
            }
            this.publishPrivate(listener.getUsername(), members.toString(), true);
        }
        this.publish(listener.getUsername(), listener.getUsername() + " has entered the chat. Welcome!", true);
        this.log.info("added chat listener for username " + listener.getUsername());
    }

    @Override
    public synchronized void removeChatListener(IChatListener listener) {
        this.listeners.remove(listener.getUsername());
        this.log.info("removed chat listener for username " + listener.getUsername());
        this.publish(listener.getUsername(), listener.getUsername() + " has left the chat. Good Bye!", true);
    }

    @Override
    public synchronized void publish(String fromUser, String message, boolean isAdmin) {
        for (Entry<String, IChatListener> l : this.listeners.entrySet()) {
            if (!(l.getKey().equals(fromUser))) {
                l.getValue().onMessage(fromUser, message, isAdmin);
            }
        }
    }

    private synchronized void publishPrivate(String toUser, String message, boolean isAdmin) {
        IChatListener client = this.listeners.get(toUser);
        if (client == null)
            return;
        client.onMessage(null, message, isAdmin);
    }
}
