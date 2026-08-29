package darkpool.engine;

import darkpool.integration.TradeMatch;
import darkpool.model.Order;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MatchingEngine {

    public final ConcurrentLinkedQueue<Order> orderQueue = new ConcurrentLinkedQueue<>();

    private final PriorityQueue<Order> buyBook = new PriorityQueue<>((o1, o2) -> Long.compare(o2.price, o1.price));
    private final PriorityQueue<Order> sellBook = new PriorityQueue<>((o1, o2) -> Long.compare(o1.price, o2.price));
    public final ConcurrentLinkedQueue<TradeMatch> matchQueue = new ConcurrentLinkedQueue<>();

    public void start() {
        while (true) {
            Order order = orderQueue.poll();

            if (order != null) {
                if (order.side == 0) {
                    processBuy(order);
                } else {
                    processSell(order);
                }
            }
        }
    }

    private void processBuy(Order buyOrder) {
        Order topSell = sellBook.peek();

        if (topSell != null && topSell.price <= buyOrder.price) {
            sellBook.poll();
            System.out.println("MATCH ON DARK POOL! buy ID " + buyOrder.orderId + " matching with sell ID " + topSell.orderId + " on price " + topSell.price);
        } else {
            buyBook.add(buyOrder);
            System.out.println("buy ID " + buyOrder.orderId + " add to the book. Price: " + buyOrder.price);
        }
    }

    private void processSell(Order sellOrder) {
        Order topBuy = buyBook.peek();

        if (topBuy != null && topBuy.price >= sellOrder.price) {
            buyBook.poll();
            System.out.println("MATCH ON DARK POOL! Sell ID " + sellOrder.orderId + " matching with buy ID " + topBuy.orderId + " on price " + topBuy.price);
            matchQueue.offer(new TradeMatch(topBuy.orderId, sellOrder.orderId, topBuy.price, 100));

        } else {
            sellBook.add(sellOrder);
            System.out.println("Sell ID " + sellOrder.orderId + " add to the book. Price: " + sellOrder.price);

        }
    }

    public static void main(String[] args) {
        MatchingEngine engine = new MatchingEngine();

        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(3);
        executor.submit(new darkpool.agent.OrderAgent(engine.orderQueue));
        executor.submit(new darkpool.agent.OrderAgent(engine.orderQueue));
        executor.submit(new darkpool.integration.MidnightClient(engine.matchQueue));

        System.out.println("Engine L2 of Dark Pool Initializing. Waiting MatchingsÍ...");
        engine.start();
    }
}