package ex1;

public class Simulation {
    public static void main(String[] args) throws InterruptedException {
        Garage garage = new Garage(10);

        for (int i = 1; i <= 20; i++) {
            Thread t = new Thread(new Car(garage));
            t.setName("R-FH " + i);
            t.setDaemon(true);
            t.start();
        }

        Thread.sleep(30000);
        System.out.println("End of simulation");
    }
}