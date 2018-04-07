package com.acmeexchange.records;

import com.acmeexchange.objects.Trade;

import java.util.ArrayList;
import java.util.List;

public class TradeLog {

    private List<Trade> tradeLog;

    public TradeLog() {
        tradeLog = new ArrayList<Trade>();
    }

    public void addTrade(Trade aTrade) {
        tradeLog.add(aTrade);
    }

    public void printLog() {
        tradeLog.forEach((trade) -> System.out.println(trade));
    }

    public List<Trade> getTradeLog() {
        return tradeLog;
    }

    public void setTradeLog(List<Trade> tradeLog) {
        this.tradeLog = tradeLog;
    }
}
