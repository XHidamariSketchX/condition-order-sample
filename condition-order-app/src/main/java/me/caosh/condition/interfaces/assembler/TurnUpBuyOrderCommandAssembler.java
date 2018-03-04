package me.caosh.condition.interfaces.assembler;

import me.caosh.condition.domain.model.condition.TurnUpCondition;
import me.caosh.condition.domain.model.constants.SecurityExchange;
import me.caosh.condition.domain.model.constants.SecurityType;
import me.caosh.condition.domain.model.market.SecurityInfo;
import me.caosh.condition.domain.model.order.TradeCustomer;
import me.caosh.condition.domain.model.order.constant.EntrustStrategy;
import me.caosh.condition.domain.model.order.constant.ExchangeType;
import me.caosh.condition.domain.model.order.constant.StrategyState;
import me.caosh.condition.domain.model.order.plan.BasicTradePlan;
import me.caosh.condition.domain.model.order.plan.TradeNumber;
import me.caosh.condition.domain.model.order.plan.TradeNumberFactory;
import me.caosh.condition.domain.model.order.turnpoint.TurnUpBuyOrder;
import me.caosh.condition.domain.model.share.ValuedEnumUtil;
import me.caosh.condition.interfaces.command.TurnUpBuyOrderCreateCommand;
import me.caosh.condition.interfaces.command.TurnUpBuyOrderUpdateCommand;

/**
 * Created by caosh on 2017/8/9.
 */
public class TurnUpBuyOrderCommandAssembler {
    public static TurnUpBuyOrder assemble(Long orderId, TradeCustomer tradeCustomer, TurnUpBuyOrderCreateCommand command) {
        StrategyState strategyState = StrategyState.ACTIVE;
        SecurityType securityType = ValuedEnumUtil.valueOf(command.getSecurityType(), SecurityType.class);
        SecurityExchange securityExchange = SecurityExchange.valueOf(command.getSecurityExchange());
        SecurityInfo securityInfo = new SecurityInfo(securityType, command.getSecurityCode(), securityExchange,
                command.getSecurityName());
        TurnUpCondition turnUpCondition = new TurnUpCondition(command.getBreakPrice(), command.getTurnUpPercent());
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        BasicTradePlan tradePlan = new BasicTradePlan(ExchangeType.BUY, entrustStrategy, tradeNumber);
        return new TurnUpBuyOrder(orderId, tradeCustomer, securityInfo, turnUpCondition, tradePlan, strategyState);
    }

    public static TurnUpBuyOrder merge(TurnUpBuyOrder oldOrder, TurnUpBuyOrderUpdateCommand command) {
        StrategyState strategyState = StrategyState.ACTIVE;
        TurnUpCondition turnUpCondition = new TurnUpCondition(command.getBreakPrice(), command.getTurnUpPercent());
        if (turnUpCondition.isNeedSwap(oldOrder.getTurnUpCondition())) {
            turnUpCondition.swap(oldOrder.getTurnUpCondition());
        }
        EntrustStrategy entrustStrategy = ValuedEnumUtil.valueOf(command.getEntrustStrategy(), EntrustStrategy.class);
        TradeNumber tradeNumber = TradeNumberFactory.getInstance()
                .create(command.getEntrustMethod(), command.getEntrustNumber(), command.getEntrustAmount());
        BasicTradePlan tradePlan = new BasicTradePlan(ExchangeType.BUY, entrustStrategy, tradeNumber);
        return new TurnUpBuyOrder(oldOrder.getOrderId(), oldOrder.getCustomer(), oldOrder.getSecurityInfo(),
                turnUpCondition, tradePlan, strategyState);
    }

    private TurnUpBuyOrderCommandAssembler() {
    }
}
