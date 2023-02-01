package ex6.client;

import ex6.server.IChatListener;
import ex6.server.IChatMessageHub;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientEndpoint implements IMessageSender, IChatListener {
    private String username;
    private IMessageGui gui;
    private IChatMessageHub server;

    @Override
    public void openChatConnection(String username, String registry, String bindingName, IMessageGui messageGui) {
        this.username = username;
        this.gui = messageGui;

        try {
            Registry r = LocateRegistry.getRegistry(registry, 1099);

            // remote object to call server methods
            this.server = (IChatMessageHub) r.lookup(bindingName);

            IChatListener stub = (IChatListener) UnicastRemoteObject.exportObject(this, 0);
            this.server.addChatListener(stub);

        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendChatMessage(String message) {
        try {
            this.server.publish(this.username, message, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeChatConnection() {
        try {
            this.server.removeChatListener(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String fromUser, String message, boolean isAdmin) throws RemoteException {
        if (isAdmin) {
            this.gui.showAdminMessage(message);
        } else {
            this.gui.showNewMessage(fromUser, message);
        }
    }

    @Override
    public String getUsername() throws RemoteException {
        return this.username;
    }
}
