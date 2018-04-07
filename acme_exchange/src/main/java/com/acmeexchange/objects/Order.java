package com.acmeexchange.objects;

public class Order {

    public enum Side {
        B("Buy"), S("Sell");

        private String side;
        Side(String code) {
            this.side = side;
        }
        public String side() {
            return side;
        }
    }

    public enum Action {
        A("Add"), X("Remove"),  M("Modify");

        private String action;
        Action(String anAction) {
            this.action = anAction;
        }
        public String getAction() {
            return action;
        }
    }

    private Action action;
    private String orderId;
    private Side side;
    private int quantity;
    private double price;

    private Order() {

    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "action='" + action + '\'' +
                ", orderId='" + orderId + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public static class OrderBuilder {
        Order tempOrder;

        private OrderBuilder() {

            tempOrder = new Order();
        }

        public static OrderBuilder createOrder() {
            return new OrderBuilder();
        }

        public OrderBuilder havingPrice(double aPrice) {
            tempOrder.setPrice(aPrice);
            return this;
        }

        public OrderBuilder withAction(Action anAction) {
            tempOrder.setAction(anAction);
            return this;
        }

        public OrderBuilder withQuantity(int aQuantity) {
            tempOrder.setQuantity(aQuantity);
            return this;
        }

        public OrderBuilder havingOrderId(String anOrderId) {
            tempOrder.setOrderId(anOrderId);
            return this;
        }

        public OrderBuilder withSide(Side aSide) {
            tempOrder.setSide(aSide);
            return this;
        }

        public Order build() {
            return tempOrder;
        }
    }
}
