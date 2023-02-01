package ex6.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class Server {

    private static final Logger log = Logger.getLogger(Server.class.getCanonicalName());

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry r = LocateRegistry.createRegistry(1099);
        log.info("created Registry on port 1099");

        ChatServer server = new ChatServer();

        IChatMessageHub stub = (IChatMessageHub) UnicastRemoteObject.exportObject(server, 0);

        r.bind("ChatServer", stub);
        log.info("bound ChatServer object");
    }

}
