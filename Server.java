import java.nio.channels.MulticastChannel;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

public class Server implements Hello{

    private PriorityQueue<MessageTuple> fifoqueue;
    private Queue<String> message_log;
    private HashMap<String, Integer> ack_count;
    private int id;
    private long[] timestamp;

    private Server() {
        super();
    }

    public void setup(int id) {
        this.fifoqueue = new PriorityQueue<MessageTuple>();
        this.message_log = new LinkedList<String>();
        this.ack_count = new HashMap<String, Integer>();

        this.id = id;
        this.timestamp = new long[] {0, id};
    }

    public void broadcast(String message, String[] addressList) {
        /* broadcast the message to every process */
        long[] new_timestamp = this.timestamp;

        System.out.println("Process: " + this.id + " and Timestamp: " + new_timestamp[0] + "-" + new_timestamp[1]);
        try {
            for (int i = 1; i < addressList.length;i++) {
                if (i != this.id) {
                    Hello process = (Hello) Naming.lookup(addressList[i]);
                    MessageTuple mt = new MessageTuple(message, new_timestamp);
                    System.out.println("SENDING - Process: " + this.id + " with Timestamp: " + new_timestamp[0] + "-" + new_timestamp[1] + " to " + addressList[i]);
                    process.receiveMessage(addressList, mt);
                }
            }
            Hello process = (Hello) Naming.lookup(addressList[this.id]);
            MessageTuple mt = new MessageTuple(message, new_timestamp);
            System.out.println("SENDING - Process: " + this.id + " with Timestamp: " + new_timestamp[0] + "-" + new_timestamp[1] + " to " + addressList[this.id]);
            process.receiveMessage(addressList, mt);
        } catch(Exception e) {
            System.err.println("Process exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public long compare_timestamp(long[] ts1, long[] ts2){
        if(ts1[0] > ts2[0]){
            return ts1[0] + 1;
        }
        else if(ts1[0] < ts2[0]){
            return ts2[0] + 1;
        }
        else{
            return ts1[0] + 1;
        }
    }

    public void receiveMessage(String[] addressList, MessageTuple mt) {
        long[] message_ts = mt.getArray();
        String timestamp = "" + message_ts[0] + message_ts[1];
        System.out.println("Process: "+ this.id+" receive Timestamp " + timestamp);

        long new_time = compare_timestamp(message_ts, this.timestamp);
        this.timestamp[0] = new_time;

        /* add the message to queue and broadcast ack */
        this.fifoqueue.add(mt);
        sendAck(addressList, mt);
    }

    public void sendAck(String[] addressList, MessageTuple mt){
        long[] temp_ts = mt.getArray();
        String timestamp = "" + temp_ts[0] + temp_ts[1];
        System.out.println("Process: "+ this.id+" sent Timestamp " + timestamp);

        for (int i = 1; i < addressList.length;i++){
            try {
                //System.out.println("I am " + this.id + "and i sent it: " + i + " times.");
                Hello process = (Hello) Naming.lookup(addressList[i]);
                process.receiveAck(addressList, mt);
            } catch(Exception e) {
                System.err.println("Process exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public void receiveAck(String[] addressList, MessageTuple mt) {
        long[] temp_ts = mt.getArray();
        String timestamp = "" + temp_ts[0] + temp_ts[1];
        //System.out.println("Process: "+ this.id+" Timestamp " + timestamp);
        if (this.ack_count.containsKey(timestamp)) {

            this.ack_count.put(timestamp, this.ack_count.get(timestamp) + 1);
        } else {
            this.ack_count.put(timestamp, 1);
        }

        //System.out.println("I am " + this.id + "and i got + " + timestamp[0] + " " + timestamp[1] + ack_count.get(timestamp) + " times.");
        if (this.ack_count.get(timestamp)==addressList.length - 1) {
            this.deliverMessage();
        }
        //System.out.println("Initial Mappings are: " + this.ack_count);
    }

    public void deliverMessage() {
        /* when every process receives, deliver the message */
        MessageTuple mt = this.fifoqueue.poll();
        String message = mt.getMessage();
        message_log.offer(message);
        System.out.println("Message log: " + message);
    }

    public Queue<String> getMessageLog() {
        /* get message log in order to print locally */
        return this.message_log;
    }

    public static void main(String[] args) {
        try {
            /* register the four processes */
            Server obj1 = new Server();
            Hello stub1 = (Hello) UnicastRemoteObject.exportObject(obj1, 0);
            Registry registry1 = LocateRegistry.createRegistry(5081);
            registry1.rebind("process1", stub1);

            Server obj2 = new Server();
            Hello stub2 = (Hello) UnicastRemoteObject.exportObject(obj2, 0);
            Registry registry2 = LocateRegistry.createRegistry(5082);
            registry2.rebind("process2", stub2);

            Server obj3 = new Server();
            Hello stub3 = (Hello) UnicastRemoteObject.exportObject(obj3, 0);
            Registry registry3 = LocateRegistry.createRegistry(5083);
            registry3.rebind("process3", stub3);

            Server obj4 = new Server();
            Hello stub4 = (Hello) UnicastRemoteObject.exportObject(obj4, 0);
            Registry registry4 = LocateRegistry.createRegistry(5084);
            registry4.rebind("process4", stub4);

            /* ready */
            System.out.println("Server ready");

        } catch (Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
