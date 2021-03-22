import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.LinkedList;

public interface Hello extends Remote {
    public void setup(int id) throws RemoteException;
    public void broadcast(String message, String[] addressList) throws RemoteException;
    public long compare_timestamp(long[] ts1, long[] ts2) throws RemoteException;
    public void receiveMessage(String[] addressList, MessageTuple mt) throws RemoteException;
    public void deliverMessage() throws RemoteException;
    public void sendAck(String[] addressList, MessageTuple mt) throws RemoteException;
    public void receiveAck(String[] addressList, MessageTuple mt) throws RemoteException;
    public Queue<String> getMessageLog() throws RemoteException;
}