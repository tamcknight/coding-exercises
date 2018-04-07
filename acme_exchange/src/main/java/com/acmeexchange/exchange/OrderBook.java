package com.acmeexchange.exchange;

import com.acmeexchange.exceptions.DuplicateOrderException;
import com.acmeexchange.objects.Order;

import java.util.List;
import java.util.Map;

public interface OrderBook {

    public void process(Order anOrder);
    public Double getMidQuote();
    public void printOrderBook();
    public Order processOrderCancel(Order removedOrder);
    public Order processOrderUpate(Order updatedOrder);
    public void processOrderAdd(Order addedOrder) throws DuplicateOrderException;
    public void match(Order anOrder);
}
