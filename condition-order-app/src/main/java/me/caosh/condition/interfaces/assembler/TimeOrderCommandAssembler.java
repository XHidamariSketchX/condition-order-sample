package me.caosh.condition.interfaces.assembler;

import me.caosh.condition.domain.model.constants.SecurityExchange;
import me.caosh.condition.domain.model.constants.SecurityType;
import me.caosh.condition.domain.model.market.SecurityInfo;
import me.caosh.condition.domain.model.order.TradeCustomer;
import me.caosh.condition.domain.model.order.constant.EntrustStrategy;
import me.caosh.condition.domain.model.order.constant.ExchangeType;
import me.caosh.condition.domain.model.order.constant.OrderState;
import me.caosh.condition.domain.model.order.plan.SingleDirectionTradePlan;
import me.caosh.condition.domain.model.order.plan.TradeNumber;
import me.caosh.condition.domain.model.order.plan.TradeNumberFactory;
import me.caosh.condition.domain.model.order.time.SimpleTimeCondition;
import me.caosh.condition.domain.model.order.time.TimeOrder;
import me.caosh.condition.domain.model.share.ValuedEnumUtil;
import me.caosh.condition.domain.util.InstantUtils;
import me.caosh.condition.interfaces.command.TimeOrderCreateCommand;
import me.caosh.condition.interfaces.command.TimeOrderUpdateCommand;

/**
 * Created by caosh on 2017/8/9.
 */
public class TimeOrderCommandAssembler {
    public static TimeOrder assemble(Long orderId, TradeCustomer customerIdentity, TimeOrderCreateCommand command) {
        OrderState orderState = OrderState.ACTIVE;
        SecurityType securityType = ValuedEnumUtil.valueOf(command.getSecurityType(), SecurityType.class);
        SecurityExchange securityExchange = SecurityExchange.valueOf(command.getSecurityExchange());
        SecurityInfo securityInfo = new SecurityInfo(securityType, command.getSecurityCode(), securityExchange,
                command.getSecurityName());
        SimpleTimeCondition simpleTimeCondition = new SimpleTimeCondition(InstantUtils.toLocalDateTime(command.getTargetTime()));
        ExchangeType exchangeType = ValuedEnumUtil.valueOf(command.getExchangeType(), ExchangeType.class);
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        SingleDirectionTradePlan tradePlan = new SingleDirectionTradePlan(exchangeType, entrustStrategy, tradeNumber);
        return new TimeOrder(orderId, customerIdentity, securityInfo, simpleTimeCondition, tradePlan, orderState);
    }

    public static TimeOrder merge(TimeOrder oldOrder, TimeOrderUpdateCommand command) {
        OrderState orderState = OrderState.ACTIVE;
        SimpleTimeCondition simpleTimeCondition = new SimpleTimeCondition(InstantUtils.toLocalDateTime(command.getTargetTime()));
        ExchangeType exchangeType = ValuedEnumUtil.valueOf(command.getExchangeType(), ExchangeType.class);
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        SingleDirectionTradePlan tradePlan = new SingleDirectionTradePlan(exchangeType, entrustStrategy, tradeNumber);
        return new TimeOrder(oldOrder.getOrderId(), oldOrder.getCustomer(),
                oldOrder.getSecurityInfo(), simpleTimeCondition, tradePlan, orderState);
    }

    private TimeOrderCommandAssembler() {
    }
}
