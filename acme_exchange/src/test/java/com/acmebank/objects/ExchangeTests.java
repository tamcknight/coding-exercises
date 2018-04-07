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

import java.util.List;

import static com.acmeexchange.exchange.DefaultOrderBook.readInOrderFile;
import static org.assertj.core.api.Assertions.assertThat;


public class ExchangeTests {

    static Logger log = Logger.getLogger(DefaultOrderBook.class.getName());

    @Test
    public void testMidQuote() {

        List<String> list = readInOrderFile("src/test/resources/midOf1500.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
            assertThat(book.getMidQuote()).isEqualTo(1500.0);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Test
    public void testBuys() {

        List<String> list = readInOrderFile("src/test/resources/testBuyMatch.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
            TradeLog log = book.getTradeLog();
            assertThat(log.getTradeLog().get(0).getQuantity()).isEqualTo(5);
            assertThat(log.getTradeLog().get(1).getQuantity()).isEqualTo(4);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Test
    public void testSells() {

        List<String> list = readInOrderFile("src/test/resources/testSellMatch.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
            TradeLog log = book.getTradeLog();
            assertThat(log.getTradeLog().get(0).getQuantity()).isEqualTo(4);
            assertThat(log.getTradeLog().get(1).getQuantity()).isEqualTo(4);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Test
    public void testUpdatedOrder() {

        List<String> list = readInOrderFile("src/test/resources/testSellMatchUpdate.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));

            TradeLog log = book.getTradeLog();
            assertThat(log.getTradeLog().get(0).getQuantity()).isEqualTo(4);
            assertThat(log.getTradeLog().get(1).getQuantity()).isEqualTo(4);
        } catch (Exception e) {
            log.error(e);
        }
    }


    @Test
    public void testDuplicateOrderId() {

        List<String> list = readInOrderFile("src/test/resources/orderswithDupes.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(DuplicateOrderException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadOrderPrice() {

        List<String> list = readInOrderFile("src/test/resources/badprice.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderPriceException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadOrderQty() {

        List<String> list = readInOrderFile("src/test/resources/badqty.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderQuantityException.class);
            log.error(e);
        }
    }

    @Test
    public void testRemovesWithNoOrder() {

        List<String> list = readInOrderFile("src/test/resources/removesNoOrder.txt");
        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class);
            log.error(e);
        }
    }

    @Test
    public void testOriginalCase() {

        List<String> list = readInOrderFile("src/test/resources/originalCase.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderQuantityException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadSide() {

        List<String> list = readInOrderFile("src/test/resources/badside.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));

        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidOrderSideException.class);
            log.error(e);
        }
    }

    @Test
    public void testBadAction() {

        List<String> list = readInOrderFile("src/test/resources/badaction.txt");

        try {
            OrderBook book = new DefaultOrderBook("ACME");
            list.forEach(order -> book.processOrderFromString(order));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            log.error(e);
        }
    }
}



