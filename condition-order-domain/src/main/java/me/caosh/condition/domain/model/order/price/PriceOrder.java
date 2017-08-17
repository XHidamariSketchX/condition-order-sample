package me.caosh.condition.domain.model.order.price;

import com.google.common.base.MoreObjects;
import me.caosh.condition.domain.model.order.*;
import me.caosh.condition.domain.model.order.constant.OrderState;
import me.caosh.condition.domain.model.order.plan.TradePlan;
import me.caosh.condition.domain.model.trade.EntrustCommand;
import me.caosh.condition.domain.model.market.RealTimeMarket;
import me.caosh.condition.domain.model.market.SecurityInfo;
import me.caosh.condition.domain.model.signal.SignalFactory;
import me.caosh.condition.domain.model.signal.TradeSignal;
import me.caosh.condition.domain.model.strategy.NativeStrategyInfo;
import me.caosh.condition.domain.model.trade.EntrustPriceSelector;
import me.caosh.condition.domain.model.trade.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by caosh on 2017/8/1.
 */
public class PriceOrder extends ConditionOrder implements RealTimeMarketDriven {
    private static final Logger logger = LoggerFactory.getLogger(PriceOrder.class);

    public PriceOrder(Long orderId, TradeCustomerIdentity customerIdentity, boolean deleted, OrderState orderState,
                      SecurityInfo securityInfo,PriceCondition condition, TradePlan tradePlan) {
        super(orderId, customerIdentity, deleted, securityInfo, NativeStrategyInfo.PRICE, condition, tradePlan, orderState);
    }

    public PriceCondition getPriceCondition() {
        return (PriceCondition) getCondition();
    }

    @Override
    public TradeSignal onRealTimeMarketUpdate(RealTimeMarket realTimeMarket) {
        BigDecimal currentPrice = realTimeMarket.getCurrentPrice();
        logger.debug("Check price condition, orderId={}, currentPrice={}, condition={}",
                getOrderId(), currentPrice, getCondition());
        if (getPriceCondition().isSatisfiedBy(currentPrice)) {
            return SignalFactory.getInstance().general();
        }
        return SignalFactory.getInstance().none();
    }

    @Override
    public EntrustCommand onTradeSignal(TradeSignal signal, RealTimeMarket realTimeMarket) {
        BigDecimal entrustPrice = EntrustPriceSelector.selectPrice(realTimeMarket, getTradePlan().getEntrustStrategy());
        return new EntrustCommand(getCustomerIdentity(), getSecurityInfo(), getTradePlan().getExchangeType(),
                entrustPrice, getTradePlan().getTradeNumber().getNumber(entrustPrice), OrderType.LIMITED);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .toString();
    }
}
