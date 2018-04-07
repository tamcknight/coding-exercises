package com.acmeexchange.objects;

import java.util.ArrayList;
import java.util.List;

public class Trade {

    private String action;
    private int quantity;
    private double price;
    private int tradeID;
    private List<String> orderInfo;


    private Trade() {
        this.orderInfo = new ArrayList<>();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public int getTradeID() {
        return tradeID;
    }

    public void setTradeID(int tradeID) {
        this.tradeID = tradeID;
    }

    public List<String> getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(List<String> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public void addOrderInfo(String orderInfo){
        this.orderInfo.add(orderInfo);
    }

    @Override
    public String toString() {
        return "Trade:" +
                ", qty=" + quantity +
                ", price=" + price +
                ", tradeID=" + tradeID +
                " ---> "+quantity+"@"+price;
    }

    public static class TradeBuilder {

        Trade tempTrade;

        private TradeBuilder() {
            tempTrade = new Trade();
        }

        public static TradeBuilder createTrade() {
            return new TradeBuilder();
        }

        public TradeBuilder withAction(String anAction) {
            tempTrade.setAction(anAction);
            return this;
        }

        public TradeBuilder havingPrice(double price) {
            tempTrade.setPrice(price);
            return this;
        }

        public TradeBuilder withQuantity(int aQuantity) {
            tempTrade.setQuantity(aQuantity);
            return this;
        }

        public TradeBuilder havingId(int anId) {
            tempTrade.setTradeID(anId);
            return this;
        }

        public Trade build() {
            return tempTrade;
        }


    }

}
