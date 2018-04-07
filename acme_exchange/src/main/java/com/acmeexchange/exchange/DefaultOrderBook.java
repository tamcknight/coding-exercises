package com.acmeexchange.exchange;


import com.acmeexchange.exceptions.*;
import com.acmeexchange.objects.Order;
import com.acmeexchange.objects.Order.Action;
import com.acmeexchange.objects.Order.Side;
import com.acmeexchange.objects.Trade;
import com.acmeexchange.records.TradeLog;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


public class DefaultOrderBook implements OrderBook {

    private TradeLog tradeLog;
    private String marketInstrument;
    private NavigableMap<Double, List<Order>> bid;
    private NavigableMap<Double, List<Order>> ask;
    private Map<String, Order> orderMap;

    private int orderCounter = 0;
    private int tradeId = 0;
    static Logger log = Logger.getLogger(DefaultOrderBook.class.getName());


    public DefaultOrderBook(String anInstrument) {
        tradeLog = new TradeLog();
        marketInstrument = anInstrument;
        bid = new TreeMap<Double, List<Order>>();
        ask = new TreeMap<Double, List<Order>>();
        orderMap = new HashMap<String, Order>();
    }

    public static void main(String[] args) {

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            List<String> list = readInOrderFile(args[0]);
            list.forEach(order -> book.processOrderFromString(order));
        } catch (ArrayIndexOutOfBoundsException ioe){
            log.error("No file passed on command line.",ioe);

        } catch (Exception e) {
            log.error("Error running DefaultOrderBook", e);
        }
    }

    @Override
    public void processOrderFromString(String orderString) {

        try {
            Order order = createOrderFromString(orderString);
            process(order);

        } catch (Exception e) {
            log.warn(e);
        }
    }

    /**
     * Entry point for all orders to be processed.
     *
     * @param anOrder
     */

    @Override
    public void process(Order anOrder) {

        if (anOrder.getQuantity() > 0) {
            switch (anOrder.getAction()) {
                case A:
                    try {
                        processOrderAdd(anOrder);
                        break;
                    } catch (Exception e) {
                        log.error(e);
                    }

                case M:
                    processOrderUpate(anOrder);
                    break;
                case X:
                    processOrderCancel(anOrder);
                    break;
            }
        }

        orderCounter++;
        System.out.println("Mid Price is: " + getMidQuote());
        if (orderCounter % 10 == 0) {
            printOrderBook();
        }
    }

    /**
     * Processes order add messages.
     *
     * @param addedOrder
     * @throws DuplicateOrderException
     */
    @Override
    public void processOrderAdd(Order addedOrder) throws DuplicateOrderException {

        //ensure that the order add message is not a dupe.
        if (orderMap.containsKey(addedOrder.getOrderId())) {
            log.warn(addedOrder.getOrderId() + " order id is a duplicate.");
            throw new DuplicateOrderException("Order Id has already been added");
        } else {

            match(addedOrder);
            //add any unmatched part of the order and unmatched orders
            if (addedOrder.getQuantity() > 0) {
                Map<Double, List<Order>> marketSide = getMarketSide(addedOrder);

                //add it to the map of orders to track them
                orderMap.put(addedOrder.getOrderId(), addedOrder);

                //add a reference to the depth of the book if the market exists at that price
                if (marketSide.containsKey(addedOrder.getPrice())) {
                    marketSide.get(addedOrder.getPrice()).add(addedOrder);
                } else {
                    //if not market exists at the prices create the market at the price and
                    //add the order to the market depth for that price
                    List<Order> depth = new ArrayList<Order>();
                    depth.add(addedOrder);
                    marketSide.put(addedOrder.getPrice(), depth);
                }
            }
        }

    }

    /**
     * Processes the order update
     *
     * @param updatedOrder
     * @return
     */
    @Override
    public Order processOrderUpate(Order updatedOrder) {

        //update the in memory order
        Order oldOrder = null;
        if (orderMap.containsKey(updatedOrder.getOrderId())) {
            oldOrder = orderMap.put(updatedOrder.getOrderId(), updatedOrder);
        }
        //after the order is updated see if it can be matched up in the market
        match(updatedOrder);

        return oldOrder;
    }

    /**
     * Processes a Order Cancel message.
     *
     * @param removedOrder
     * @return
     */
    @Override
    public Order processOrderCancel(Order removedOrder) {

        //an assumption is made that orders cannot change side and that an order cancel will have
        // the same details of the order that inserted it into the order book.
        Map<Double, List<Order>> marketSide = getMarketSide(removedOrder);

        Order removed = null;
        //check to see if the order exists
        if (marketSide.containsKey(removedOrder.getPrice())) {
            //get the list where the order is in.
            List<Order> tmpList = marketSide.get(removedOrder.getPrice());

            //iterate through the list and find the order to cancel.  the order sent in is a representation
            //of the order to cancel, but not the same order in the data structure.
            //the actual order needs to be found to be removed.  match on ORder ID.
            for (Order order : tmpList) {
                if (order.getOrderId().equals(removedOrder.getOrderId())) {
                    removed = order;
                    break;
                }
            }
            //remove it from the list.
            tmpList.remove(removed);

            //if there is no market depth at this price remove it from the book as there
            //is no market at that price.

            if (tmpList.isEmpty()) {
                marketSide.remove(removedOrder.getPrice());
            }
            //remove the order from the map that is used for quick access.
            orderMap.remove(removed.getOrderId());
        } else {
            log.error(removedOrder.getOrderId() + " is not a valid orderID in the book.");
        }
        return removed;

    }

    /**
     * This method is used to determine what side of the market and order will be on.
     * It determines what side of the order book an order should go in basd on side
     * if an order is not matched off.
     * a
     *
     * @param anOrder
     * @return
     */
    public Map<Double, List<Order>> getMarketSide(Order anOrder) {

        Map<Double, List<Order>> marketSide = null;
        switch (anOrder.getSide()) {
            case B:
                marketSide = bid;
                break;
            case S:
                marketSide = ask;
                break;
        }
        return marketSide;
    }

    /**
     * When an order comes in this is called to see if the trade can be matched off
     * with the current order book.
     *
     * @param anOrder
     */
    @Override
    public void match(Order anOrder) {

        switch (anOrder.getSide()) {
            case B:
                matchBuyOrder(anOrder);
                break;
            case S:
                matchSellOrder(anOrder);
                break;
        }
    }

    /**
     * This method will try to match a buy order up with any sells.
     */
    public void matchBuyOrder(Order anOrder) {

        if (!ask.isEmpty() && anOrder.getQuantity() > 0) {

            //get the best available price and check to see if the
            //buyer is willing to pay a price from any of the sells on the market
            //the buyer should be getting the best price avaiable so start at the lowest available price
            Double askPrice = ask.firstKey();
            if (anOrder.getPrice() >= askPrice) {

                //get the first order for hte best price as the first order should be the first
                //one to be filled
                if (!ask.get(askPrice).isEmpty()) {
                    Order askOrder = ask.get(askPrice).get(0);

                    //determine the trade qty
                    int tradeQty = 0;
                    if (askOrder.getQuantity() >= anOrder.getQuantity()) {
                        tradeQty = anOrder.getQuantity();
                    } else {
                        tradeQty = askOrder.getQuantity();
                    }

                    Trade trade = Trade.TradeBuilder.createTrade()
                            .withAction("T")
                            .havingId(incrementAndGetTradeId())
                            .havingPrice(askPrice)
                            .withQuantity(tradeQty).build();

                    //useful to know where trade came from.  add both sides to the
                    //trade log in case we need to debug
                    trade.addOrderInfo(askOrder.toString());
                    trade.addOrderInfo(anOrder.toString());
                    tradeLog.addTrade(trade);

                    //reset the qty on the two orders to refelct to trade
                    askOrder.setQuantity(askOrder.getQuantity() - tradeQty);
                    anOrder.setQuantity(anOrder.getQuantity() - tradeQty);
                    //per the spec print out the trade
                    System.out.println(trade.toString());

                    //if order in the book has been fully filled it needs to
                    //be removed from the book.
                    removeFullyFilledOrder(ask, askOrder);

                    //recursively try to fill the order until there is nothing left
                    //or there is not market for it
                    matchBuyOrder(anOrder);
                }
            }
        }
    }

    /**
     * This method will try to match a sell order up with any buys.
     *
     * @param anOrder
     */
    public void matchSellOrder(Order anOrder) {


        if (!bid.isEmpty() && anOrder.getQuantity() > 0) {

            //get the best available price and check to see if the
            //seller is willing to pay a price from any of the buys on the market
            //the seller should be getting the best price avaiable so start at the highest available price
            Double bidPrice = bid.lastKey();

            if (anOrder.getPrice() <= bidPrice) {

                //get the first order for hte best price as the first order should be the first
                //one to be filled
                if (!bid.get(bidPrice).isEmpty()) {
                    Order bidOrder = bid.get(bidPrice).get(0);

                    int tradeQty = 0;
                    if (bidOrder.getQuantity() >= anOrder.getQuantity()) {
                        tradeQty = anOrder.getQuantity();
                    } else {
                        tradeQty = bidOrder.getQuantity();
                    }

                    Trade trade = Trade.TradeBuilder.createTrade()
                            .withAction("T")
                            .havingId(incrementAndGetTradeId())
                            .havingPrice(bidPrice)
                            .withQuantity(tradeQty).build();

                    //useful to know where trade came from.  add it to the log
                    trade.addOrderInfo(bidOrder.toString());
                    trade.addOrderInfo(anOrder.toString());
                    tradeLog.addTrade(trade);

                    //set the qtys on the order down
                    bidOrder.setQuantity(bidOrder.getQuantity() - tradeQty);
                    anOrder.setQuantity(anOrder.getQuantity() - tradeQty);
                    //per the spec print out trade
                    System.out.println(trade.toString());

                    //if order in the book has been fully filled it needs to
                    //be removed from the book
                    removeFullyFilledOrder(bid, bidOrder);


                    //recursively try to fill the order until there is nothing left
                    //or there is not market for it
                    matchSellOrder(anOrder);
                }
            }
        }
    }


    /**
     * If an order is fully filled it needs to be removed from the market.  This important
     * for maintaining the data structure
     *
     * @param bidOrAsk
     * @param anOrder
     */
    public void removeFullyFilledOrder(NavigableMap<Double, List<Order>> bidOrAsk, Order anOrder) {

        if (anOrder.getQuantity() == 0) {
            try {
                //get the array that the order is in
                List<Order> depth = bidOrAsk.get(anOrder.getPrice());
                depth.remove(anOrder);

                //if the array that is holding the market depth is empty remove the price
                //as a key since there is no longer a market at that price
                if (depth.isEmpty()) {
                    bidOrAsk.remove(anOrder.getPrice());
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    /**
     * Take a string comma delimited string and creates and order from it.
     * <p>
     * Format is Action, OrderID, Side, Qty, Price
     * Example: A,100000,S,10,1000
     *
     * @param orderString
     * @return
     */
    public static Order createOrderFromString(String orderString) {
        Order order = null;

        try {
            String[] parsedString = orderString.trim().split(",");

            //length must be 5
            if (parsedString.length != 5)
                throw new InvalidOrderStringException("Order String did not contain 5 tokens.");

            String orderId = parsedString[1];

            if (orderId == null)
                throw new InvalidOrderIdException("Order Id was null");

            Double price = Double.parseDouble(parsedString[4]);
            if (price == null || price < 0)
                throw new InvalidOrderPriceException(price + " is not a valid price.");

            Integer qty = Integer.parseInt(parsedString[3]);
            if (qty == null || qty < 0)
                throw new InvalidOrderQuantityException(qty + " is not a valid qty.");

            Side side = null;
            try {
                side = Side.valueOf(parsedString[2]);
            } catch (IllegalArgumentException exp) {
                log.error(exp);
            }

            Action action = null;
            try {
                action = Action.valueOf(parsedString[0]);
            } catch (IllegalArgumentException exp) {
                log.error(exp);
            }

            order = Order.OrderBuilder.createOrder()
                    .havingOrderId(orderId.trim())
                    .havingPrice(price)
                    .withAction(action)
                    .withQuantity(qty)
                    .withSide(side).build();

        } catch (Exception e) {
            log.error(e);
        }
        return order;

    }


    /**
     * Ths method prints out the order book with the Asks descending from greatest to smallest and
     * the bids are in the opposite format.  This gives a good view of the market and a trader may be filled at.
     */
    @Override
    public void printOrderBook() {

        System.out.println("-----------------------------------Order Book-----------------------------------");

        System.out.println("Ask");
        System.out.println("---");

        for (Double askPrc : ask.descendingKeySet()) {
            System.out.println(askPrc);

            for (Object order : ask.get(askPrc)) {
                System.out.println("\t" + order.toString());
            }
        }

        System.out.println("Bid");
        System.out.println("---");

        for (Double bidPrc : bid.descendingKeySet()) {
            System.out.println(bidPrc);

            for (Object order : bid.get(bidPrc)) {
                System.out.println("\t" + order.toString());
            }
        }
        System.out.println("----------------------------------------------------------------------");

    }

    /**
     * The calcuates the mid of the market which is the greatest buy price and smallest sell price.
     *
     * @return
     */
    @Override
    public Double getMidQuote() {
        Double mid = 0.0;

        if (!ask.isEmpty() && !bid.isEmpty()) {
            Double bestAsk = ask.firstKey();
            Double bestBid = bid.lastKey();

            mid = (bestAsk + bestBid) / 2;
        } else {
            log.error("The market is currently one sided or empty.  No Mid Exists.");
            //per the spec return a Nan is the market is one sided.
            mid = Double.NaN;
        }

        return mid;

    }

    /**
     * Increment the trade id when a trade is made.
     *
     * @return
     */
    //although this process is not multhreaded if it was changed to be this would need to ensure thread safety
    private synchronized int incrementAndGetTradeId() {
        return tradeId++;
    }


    /**
     * Reads in a file and create list of the Strings that represent orders.
     *
     * @param filePath
     * @return
     */
    public static List<String> readInOrderFile(String filePath) {

        List<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> list.add(line));
        } catch (Exception e) {
            log.error("Error processing file", e);
        }
        return list;
    }

    public TradeLog getTradeLog() {
        return tradeLog;
    }

    public void setTradeLog(TradeLog tradeLog) {
        this.tradeLog = tradeLog;
    }

    public String getMarketInstrument() {
        return marketInstrument;
    }

    public void setMarketInstrument(String marketInstrument) {
        this.marketInstrument = marketInstrument;
    }

    public NavigableMap<Double, List<Order>> getBid() {
        return bid;
    }

    public void setBid(NavigableMap<Double, List<Order>> bid) {
        this.bid = bid;
    }

    public NavigableMap<Double, List<Order>> getAsk() {
        return ask;
    }

    public void setAsk(NavigableMap<Double, List<Order>> ask) {
        this.ask = ask;
    }

    public Map<String, Order> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<String, Order> orderMap) {
        this.orderMap = orderMap;
    }

    public int getOrderCounter() {
        return orderCounter;
    }

    public void setOrderCounter(int orderCounter) {
        this.orderCounter = orderCounter;
    }


}