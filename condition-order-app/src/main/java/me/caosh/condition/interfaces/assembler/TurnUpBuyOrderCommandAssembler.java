package me.caosh.condition.interfaces.assembler;

import hbec.intellitrade.common.ValuedEnumUtil;
import hbec.intellitrade.common.security.SecurityExchange;
import hbec.intellitrade.common.security.SecurityInfo;
import hbec.intellitrade.common.security.SecurityType;
import hbec.intellitrade.condorder.domain.OrderState;
import hbec.intellitrade.condorder.domain.TradeCustomerInfo;
import hbec.intellitrade.condorder.domain.tradeplan.BasicTradePlan;
import hbec.intellitrade.condorder.domain.tradeplan.EntrustStrategy;
import hbec.intellitrade.condorder.domain.tradeplan.TradeNumber;
import hbec.intellitrade.condorder.domain.tradeplan.TradeNumberFactory;
import hbec.intellitrade.trade.domain.ExchangeType;
import me.caosh.condition.domain.model.condition.TurnUpCondition;
import me.caosh.condition.domain.model.order.turnpoint.TurnUpBuyOrder;
import me.caosh.condition.interfaces.command.TurnUpBuyOrderCreateCommand;
import me.caosh.condition.interfaces.command.TurnUpBuyOrderUpdateCommand;

/**
 * Created by caosh on 2017/8/9.
 */
public class TurnUpBuyOrderCommandAssembler {
    public static TurnUpBuyOrder assemble(Long orderId, TradeCustomerInfo tradeCustomerInfo, TurnUpBuyOrderCreateCommand command) {
        OrderState orderState = OrderState.ACTIVE;
        SecurityType securityType = ValuedEnumUtil.valueOf(command.getSecurityType(), SecurityType.class);
        SecurityExchange securityExchange = SecurityExchange.valueOf(command.getSecurityExchange());
        SecurityInfo securityInfo = new SecurityInfo(securityType, command.getSecurityCode(), securityExchange,
                command.getSecurityName());
        TurnUpCondition turnUpCondition = new TurnUpCondition(command.getBreakPrice(), command.getTurnUpPercent());
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        BasicTradePlan tradePlan = new BasicTradePlan(ExchangeType.BUY, entrustStrategy, tradeNumber);
        return new TurnUpBuyOrder(orderId, tradeCustomerInfo, securityInfo, turnUpCondition, tradePlan, orderState);
    }

    public static TurnUpBuyOrder merge(TurnUpBuyOrder oldOrder, TurnUpBuyOrderUpdateCommand command) {
        OrderState orderState = OrderState.ACTIVE;
        TurnUpCondition turnUpCondition = new TurnUpCondition(command.getBreakPrice(), command.getTurnUpPercent());
        if (turnUpCondition.isNeedSwap(oldOrder.getTurnUpCondition())) {
            turnUpCondition.swap(oldOrder.getTurnUpCondition());
        }
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        BasicTradePlan tradePlan = new BasicTradePlan(ExchangeType.BUY, entrustStrategy, tradeNumber);
        return new TurnUpBuyOrder(oldOrder.getOrderId(), oldOrder.getCustomer(), oldOrder.getSecurityInfo(),
                turnUpCondition, tradePlan, orderState);
    }

    private TurnUpBuyOrderCommandAssembler() {
    }
}
