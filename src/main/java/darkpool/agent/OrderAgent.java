package darkpool.agent;

import darkpool.model.Order;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderAgent implements Runnable{

    private final ConcurrentLinkedQueue<Order> queue;
    private final Random random = new Random();

    public OrderAgent(ConcurrentLinkedQueue<Order> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        while (true){
            try {
                Order novaOrdem = new Order();
                novaOrdem.orderId = random.nextInt(100000);
                novaOrdem.side = (byte) random.nextInt(2);
                novaOrdem.price = 4900 + random.nextInt(200);
                novaOrdem.quantity = 100;

                queue.offer(novaOrdem);

                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
