package me.caosh.condition.domain.model.order.plan;

import me.caosh.condition.domain.model.order.constant.EntrustStrategy;
import me.caosh.condition.domain.model.order.constant.ExchangeType;
import me.caosh.condition.domain.model.share.ValuedEnumUtil;

import java.math.BigDecimal;

/**
 * Created by caosh on 2017/8/24.
 */
public class TradePlanFactory {
    private static final TradePlanFactory INSTANCE = new TradePlanFactory();

    public static final int DOUBLE_EXCHANGE_TYPE = 0;
    public static final int OPPOSITE_ENTRUST_STRATEGY_SUM = 13;

    public static TradePlanFactory getInstance() {
        return INSTANCE;
    }

    public TradePlan create(int exchangeType, Integer entrustStrategy,
                            Integer entrustMethod, Integer number, BigDecimal entrustAmount) {
        if (exchangeType == DOUBLE_EXCHANGE_TYPE) {
            return createDouble(entrustStrategy, entrustMethod, number, entrustAmount);
        } else {
            return createSingle(exchangeType, entrustStrategy, entrustMethod, number, entrustAmount);
        }
    }

    public SingleDirectionTradePlan createSingle(int exchangeType, Integer entrustStrategy, Integer entrustMethod,
                                                 Integer number, BigDecimal entrustAmount) {
        TradeNumber tradeNumber = TradeNumberFactory.getInstance().create(entrustMethod, number, entrustAmount);
        EntrustStrategy theEntrustStrategy = ValuedEnumUtil.valueOf(entrustStrategy, EntrustStrategy.class);
        ExchangeType theExchangeType = ValuedEnumUtil.valueOf(exchangeType, ExchangeType.class);
        return new SingleDirectionTradePlan(theExchangeType, theEntrustStrategy, tradeNumber);
    }

    public DoubleDirectionTradePlan createDouble(Integer entrustStrategy, Integer entrustMethod, Integer number, BigDecimal entrustAmount) {
        TradeNumber tradeNumber = TradeNumberFactory.getInstance().create(entrustMethod, number, entrustAmount);
        EntrustStrategy buyEntrustStrategy = ValuedEnumUtil.valueOf(entrustStrategy, EntrustStrategy.class);
        EntrustStrategy sellEntrustStrategy;
        if (buyEntrustStrategy == EntrustStrategy.CURRENT_PRICE) {
            sellEntrustStrategy = EntrustStrategy.CURRENT_PRICE;
        } else {
            sellEntrustStrategy = ValuedEnumUtil.valueOf(OPPOSITE_ENTRUST_STRATEGY_SUM - entrustStrategy, EntrustStrategy.class);
        }
        SingleDirectionTradePlan buyPlan = new SingleDirectionTradePlan(ExchangeType.BUY, buyEntrustStrategy, tradeNumber);
        SingleDirectionTradePlan sellPlan = new SingleDirectionTradePlan(ExchangeType.SELL, sellEntrustStrategy, tradeNumber);
        return new DoubleDirectionTradePlan(buyPlan, sellPlan);
    }

    private TradePlanFactory() {
    }
}
