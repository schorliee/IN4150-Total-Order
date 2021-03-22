import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client  {
    private Client() {}

    public static String[] addressList = {"placeholder","rmi://localhost:5081/process1", "rmi://localhost:5082/process2", "rmi://localhost:5083/process3", "rmi://localhost:5084/process4"};

    public static class MyRunnable_1 implements Runnable {
        public int id;
        public MyRunnable_1(int id) {
            this.id = id;
        }
        public void run() {
            System.out.println("Process " + id + " Running");
            try {
                Hello my_hello = (Hello) Naming.lookup(addressList[id]);
                my_hello.setup(1);
                long start = System.currentTimeMillis();
                Thread.sleep(2000);
                long time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message1-1", addressList);

                Thread.sleep(4000);
                time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message1-2", addressList);

                Thread.sleep(3000);

                System.out.print("Message log for process" + id + ":");
                for(String q : my_hello.getMessageLog()){
                    System.out.print(q + " ");
                }
                System.out.print("\n");
            } catch(Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static class MyRunnable_2 implements Runnable {
        public int id;
        public MyRunnable_2(int id) {
            this.id = id;
        }
        public void run() {
            System.out.println("Process " + id + " Running");
            try {
                Hello my_hello = (Hello) Naming.lookup(addressList[id]);
                my_hello.setup(2);
                long start = System.currentTimeMillis();
                Thread.sleep(3000);
                long time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message2-1", addressList);

                Thread.sleep(2000);
                time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message2-2", addressList);

                Thread.sleep(5000);

                System.out.print("Message log for process" + id + ":");
                for(String q : my_hello.getMessageLog()){
                    System.out.print(q + " ");
                }
                System.out.print("\n");
            } catch(Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static class MyRunnable_3 implements Runnable {
        public int id;
        public MyRunnable_3(int id) {
            this.id = id;
        }
        public void run() {
            System.out.println("Process " + id + " Running");
            try {
                Hello my_hello = (Hello) Naming.lookup(addressList[id]);
                my_hello.setup(3);
                long start = System.currentTimeMillis();
                Thread.sleep(1000);
                long time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message3-1", addressList);

                Thread.sleep(6000);
                time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message3-2", addressList);

                Thread.sleep(4000);

                System.out.print("Message log for process" + id + ":");
                for(String q : my_hello.getMessageLog()){
                    System.out.print(q + " ");
                }
                System.out.print("\n");
            } catch(Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static class MyRunnable_4 implements Runnable {
        public int id;
        public MyRunnable_4(int id) {
            this.id = id;
        }
        public void run() {
            System.out.println("Process " + id + " Running");
            try {
                Hello my_hello = (Hello) Naming.lookup(addressList[id]);
                my_hello.setup(4);
                long start = System.currentTimeMillis();
                Thread.sleep(4000);
                long time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message4-1", addressList);

                Thread.sleep(4000);
                time = (System.currentTimeMillis()-start)/1000;
                my_hello.broadcast("message4-2", addressList);

                Thread.sleep(4000);

                System.out.print("Message log for process" + id + ":");
                for(String q : my_hello.getMessageLog()){
                    System.out.print(q + " ");
                }
                System.out.print("\n");
            } catch(Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {

        Thread t1 = new Thread(new MyRunnable_1(1));
        t1.start();
        Thread t2 = new Thread(new MyRunnable_2(2));
        t2.start();
        Thread t3 = new Thread(new MyRunnable_3(3));
        t3.start();
        Thread t4 = new Thread(new MyRunnable_4(4));
        t4.start();
    }
}



