package me.caosh.condition.domain.model.order.time;

import com.google.common.base.MoreObjects;
import hbec.intellitrade.common.security.SecurityInfo;
import hbec.intellitrade.conditionorder.domain.AbstractExplicitTradingSecurityOrder;
import hbec.intellitrade.conditionorder.domain.OrderState;
import hbec.intellitrade.conditionorder.domain.TradeCustomerInfo;
import hbec.intellitrade.conditionorder.domain.strategyinfo.NativeStrategyInfo;
import hbec.intellitrade.conditionorder.domain.strategyinfo.StrategyInfo;
import hbec.intellitrade.conditionorder.domain.tradeplan.BaseTradePlan;
import hbec.intellitrade.conditionorder.domain.tradeplan.TradePlan;
import hbec.intellitrade.conditionorder.domain.trigger.TriggerTradingContext;
import hbec.intellitrade.strategy.domain.TimeDrivenStrategy;
import hbec.intellitrade.strategy.domain.condition.Condition;
import hbec.intellitrade.strategy.domain.signal.Signal;
import hbec.intellitrade.strategy.domain.signal.Signals;
import me.caosh.condition.domain.model.condition.TimeReachedCondition;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by caosh on 2017/8/20.
 */
public class TimeOrder extends AbstractExplicitTradingSecurityOrder implements TimeDrivenStrategy {
    private static final Logger logger = LoggerFactory.getLogger(TimeOrder.class);

    private final TimeReachedCondition timeReachedCondition;
    private final BaseTradePlan tradePlan;

    public TimeOrder(Long orderId, TradeCustomerInfo tradeCustomerInfo, SecurityInfo securityInfo,
                     TimeReachedCondition timeCondition, LocalDateTime expireTime,
                     BaseTradePlan tradePlan, OrderState orderState) {
        super(orderId, tradeCustomerInfo, orderState, securityInfo, expireTime);
        this.timeReachedCondition = timeCondition;
        this.tradePlan = tradePlan;
    }

    @Override
    public Condition getCondition() {
        return timeReachedCondition;
    }

    @Override
    public Signal onTimeTick(LocalDateTime localDateTime) {
        if (isMonitoringState() && isExpiredAt(localDateTime)) {
            return Signals.expire();
        }

        if (getOrderState() != OrderState.ACTIVE) {
            return Signals.none();
        }

        return timeReachedCondition.onTimeTick(localDateTime);
    }

    @Override
    public StrategyInfo getStrategyInfo() {
        return NativeStrategyInfo.TURN_POINT;
    }

    @Override
    public TradePlan getTradePlan() {
        return tradePlan;
    }

    @Override
    protected void afterEntrustCommandsExecuted(TriggerTradingContext triggerTradingContext) {
        setOrderState(OrderState.TERMINATED);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .add("timeReachedCondition", timeReachedCondition)
                .add("tradePlan", tradePlan)
                .toString();
    }
}
