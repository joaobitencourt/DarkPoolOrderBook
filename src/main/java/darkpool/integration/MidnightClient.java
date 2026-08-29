package darkpool.integration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MidnightClient implements Runnable {

    private final ConcurrentLinkedQueue<TradeMatch> matchQueue;
    private final HttpClient httpClient;

    public MidnightClient(ConcurrentLinkedQueue<TradeMatch> matchQueue) {
        this.matchQueue = matchQueue;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    @Override
    public void run() {
        while (true) {
            TradeMatch match = matchQueue.poll();
            if (match != null) {
                String jsonPayload = String.format(
                        "{\"buyOrderId\":%d, \"sellOrderId\":%d, \"price\":%d, \"quantity\":%d}",
                        match.buyOrderId, match.sellOrderId, match.executionPrice, match.quantity
                );

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:3000/settle"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> System.out.println("✅ Sidecar recebeu o Match! Status: " + response.statusCode()));
            }
        }
    }
}