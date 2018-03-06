package me.caosh.condition.interfaces.assembler;

import hbec.intellitrade.common.security.SecurityExchange;
import hbec.intellitrade.common.security.SecurityInfo;
import hbec.intellitrade.common.security.SecurityType;
import me.caosh.condition.domain.model.condition.TimeReachedCondition;
import me.caosh.condition.domain.model.order.TradeCustomerInfo;
import me.caosh.condition.domain.model.order.constant.EntrustStrategy;
import me.caosh.condition.domain.model.order.constant.ExchangeType;
import me.caosh.condition.domain.model.order.constant.StrategyState;
import me.caosh.condition.domain.model.order.plan.BasicTradePlan;
import me.caosh.condition.domain.model.order.plan.TradeNumber;
import me.caosh.condition.domain.model.order.plan.TradeNumberFactory;
import me.caosh.condition.domain.model.order.time.TimeOrder;
import hbec.intellitrade.common.ValuedEnumUtil;
import me.caosh.condition.domain.util.InstantUtils;
import me.caosh.condition.interfaces.command.TimeOrderCreateCommand;
import me.caosh.condition.interfaces.command.TimeOrderUpdateCommand;

/**
 * Created by caosh on 2017/8/9.
 */
public class TimeOrderCommandAssembler {
    public static TimeOrder assemble(Long orderId, TradeCustomerInfo tradeCustomerInfo, TimeOrderCreateCommand command) {
        StrategyState strategyState = StrategyState.ACTIVE;
        SecurityType securityType = ValuedEnumUtil.valueOf(command.getSecurityType(), SecurityType.class);
        SecurityExchange securityExchange = SecurityExchange.valueOf(command.getSecurityExchange());
        SecurityInfo securityInfo = new SecurityInfo(securityType, command.getSecurityCode(), securityExchange,
                command.getSecurityName());
        TimeReachedCondition timeReachedCondition = new TimeReachedCondition(InstantUtils.toLocalDateTime(command.getTargetTime()));
        ExchangeType exchangeType = ValuedEnumUtil.valueOf(command.getExchangeType(), ExchangeType.class);
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        BasicTradePlan tradePlan = new BasicTradePlan(exchangeType, entrustStrategy, tradeNumber);
        return new TimeOrder(orderId, tradeCustomerInfo, securityInfo, timeReachedCondition, tradePlan, strategyState);
    }

    public static TimeOrder merge(TimeOrder oldOrder, TimeOrderUpdateCommand command) {
        StrategyState strategyState = StrategyState.ACTIVE;
        TimeReachedCondition timeReachedCondition = new TimeReachedCondition(InstantUtils.toLocalDateTime(command.getTargetTime()));
        ExchangeType exchangeType = ValuedEnumUtil.valueOf(command.getExchangeType(), ExchangeType.class);
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        BasicTradePlan tradePlan = new BasicTradePlan(exchangeType, entrustStrategy, tradeNumber);
        return new TimeOrder(oldOrder.getOrderId(), oldOrder.getCustomer(),
                oldOrder.getSecurityInfo(), timeReachedCondition, tradePlan, strategyState);
    }

    private TimeOrderCommandAssembler() {
    }
}
