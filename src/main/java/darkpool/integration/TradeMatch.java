package darkpool.integration;

public class TradeMatch {
    public final int buyOrderId;
    public final int sellOrderId;
    public final long executionPrice;
    public final int quantity;

    public TradeMatch(int buyOrderId, int sellOrderId, long executionPrice, int quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.executionPrice = executionPrice;
        this.quantity = quantity;
    }
}