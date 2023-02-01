package ex1;

public class Car implements Runnable {
    Garage garage;
    public Car(Garage garage) {
        this.garage = garage;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (true) {

            // drive around for up to 10 seconds
            try {
                Thread.sleep(Garage.rand.nextInt(10) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // try and park
            this.garage.enter();

            // park for up to 10 seconds
            try {
                Thread.sleep(Garage.rand.nextInt(2) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // leave parking garage
            this.garage.exit();
        }
    }
}
