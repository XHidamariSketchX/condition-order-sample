package me.caosh.condition.domain.model.order.plan;

import hbec.intellitrade.common.security.SecurityInfoBuilder;
import me.caosh.autoasm.ConvertibleBuilder;

import java.math.BigDecimal;

/**
 * @author caoshuhao@touker.com
 * @date 2018/2/4
 */
public class TradePlanBuilder implements ConvertibleBuilder<TradePlan> {
    private int exchangeType;
    private Integer entrustStrategy;
    private TradeNumberBuilder tradeNumber = new TradeNumberBuilder();

    public TradePlanBuilder setExchangeType(int exchangeType) {
        this.exchangeType = exchangeType;
        return this;
    }

    public TradePlanBuilder setEntrustStrategy(Integer entrustStrategy) {
        this.entrustStrategy = entrustStrategy;
        return this;
    }

    public TradeNumberBuilder getTradeNumber() {
        return tradeNumber;
    }

    public TradePlanBuilder setTradeNumber(TradeNumberBuilder tradeNumber) {
        this.tradeNumber = tradeNumber;
        return this;
    }

    @Override
    public TradePlan build() {
        return TradePlanFactory.getInstance().create(
                exchangeType, entrustStrategy, tradeNumber.getEntrustMethod(), tradeNumber.getNumber().intValue(),
                tradeNumber.getNumber());
    }

    public static class TradeNumberBuilder {
        private Integer entrustMethod;
        private BigDecimal number;

        public Integer getEntrustMethod() {
            return entrustMethod;
        }

        public BigDecimal getNumber() {
            return number;
        }

        public TradeNumberBuilder setEntrustMethod(Integer entrustMethod) {
            this.entrustMethod = entrustMethod;
            return this;
        }

        public TradeNumberBuilder setNumber(BigDecimal number) {
            this.number = number;
            return this;
        }
    }
}
