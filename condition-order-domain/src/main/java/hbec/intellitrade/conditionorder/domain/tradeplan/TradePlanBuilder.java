package hbec.intellitrade.conditionorder.domain.tradeplan;

import hbec.intellitrade.trade.domain.ExchangeType;
import hbec.intellitrade.trade.domain.OrderType;
import me.caosh.autoasm.ConvertibleBuilder;

import java.math.BigDecimal;

/**
 * @author caoshuhao@touker.com
 * @date 2018/2/4
 */
public class TradePlanBuilder implements ConvertibleBuilder<TradePlan> {
    private ExchangeType exchangeType;
    private EntrustStrategy entrustStrategy;
    private EntrustStrategy buyStrategy;
    private EntrustStrategy sellStrategy;
    private BigDecimal entrustPrice;
    private TradeNumberBuilder tradeNumber = new TradeNumberBuilder();
    private OrderType orderType = OrderType.LIMITED;

    public void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public void setEntrustStrategy(EntrustStrategy entrustStrategy) {
        this.entrustStrategy = entrustStrategy;
    }

    public void setBuyStrategy(EntrustStrategy buyStrategy) {
        this.buyStrategy = buyStrategy;
    }

    public void setSellStrategy(EntrustStrategy sellStrategy) {
        this.sellStrategy = sellStrategy;
    }

    public void setEntrustPrice(BigDecimal entrustPrice) {
        this.entrustPrice = entrustPrice;
    }

    public TradeNumberBuilder getTradeNumber() {
        return tradeNumber;
    }

    public TradePlanBuilder setTradeNumber(TradeNumberBuilder tradeNumber) {
        this.tradeNumber = tradeNumber;
        return this;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public TradePlan build() {
        if (exchangeType != null && entrustStrategy != null) {
            return TradePlanFactory.getInstance().createSingle(
                    exchangeType,
                    entrustStrategy,
                    entrustPrice,
                    tradeNumber.build(),
                    orderType);
        } else {
            return TradePlanFactory.getInstance().createBidirectional(tradeNumber.build(),
                    buyStrategy, sellStrategy, orderType);
        }
    }

    public static class TradeNumberBuilder implements ConvertibleBuilder<TradeNumber> {
        private EntrustMethod entrustMethod;
        private Integer number;
        private BigDecimal amount;

        public void setEntrustMethod(EntrustMethod entrustMethod) {
            this.entrustMethod = entrustMethod;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        @Override
        public TradeNumber build() {
            if (entrustMethod == EntrustMethod.NUMBER) {
                return new TradeNumberDirect(number);
            } else if (entrustMethod == EntrustMethod.AMOUNT) {
                return new TradeNumberByAmount(amount);
            } else {
                throw new IllegalArgumentException("entrustMethod=" + entrustMethod);
            }
        }
    }
}
