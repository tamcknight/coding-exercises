package com.acmebank.objects;

import com.acmeexchange.exceptions.DuplicateOrderException;
import com.acmeexchange.exceptions.InvalidOrderPriceException;
import com.acmeexchange.exceptions.InvalidOrderQuantityException;
import com.acmeexchange.exceptions.InvalidOrderSideException;
import com.acmeexchange.exchange.DefaultOrderBook;
import com.acmeexchange.exchange.OrderBook;
import com.acmeexchange.records.TradeLog;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;



public class ExchangeTests {

    static Logger log = Logger.getLogger(DefaultOrderBook.class.getName());

    @Test
    public void testMidQuote() {
        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/midOf1500.txt");
            assertThat(book.getMidQuote()).isEqualTo(1500.0);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Test
    public void testBuys() {
        try {
            DefaultOrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/testBuyMatch.txt");
            TradeLog log = book.getTradeLog();
            assertThat(log.getTradeLog().get(0).getQuantity()).isEqualTo(5);
            assertThat(log.getTradeLog().get(1).getQuantity()).isEqualTo(4);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Test
    public void testSells() {
        try {
            DefaultOrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/testSellMatch.txt");
            TradeLog log = book.getTradeLog();
            assertThat(log.getTradeLog().get(0).getQuantity()).isEqualTo(4);
            assertThat(log.getTradeLog().get(1).getQuantity()).isEqualTo(4);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Test
    public void testUpdatedOrder() {
        try {
            DefaultOrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/testSellMatchUpdate.txt");
            TradeLog log = book.getTradeLog();
            assertThat(log.getTradeLog().get(0).getQuantity()).isEqualTo(4);
            assertThat(log.getTradeLog().get(1).getQuantity()).isEqualTo(4);
        } catch (Exception e) {
            log.error(e);
        }
    }


    @Test
    public void testDuplicateOrderId() {

        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/orderswithDupes.txt");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(DuplicateOrderException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadOrderPrice() {

        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/badprice.txt");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderPriceException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadOrderQty() {

        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/badqty.txt");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderQuantityException.class);
            log.error(e);
        }
    }

    @Test
    public void testRemovesWithNoOrder() {

        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/removesNoOrder.txt");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class);
            log.error(e);
        }
    }

    @Test
    public void testOriginalCase() {

        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/originalCase.txt");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderQuantityException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadSide() {

        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/badside.txt");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderSideException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadAction() {

        try {
            OrderBook book = DefaultOrderBook.createOrderBookFromFile("src/test/resources/badaction.txt");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderSideException.class);
            log.error(e);
        }
    }
}



