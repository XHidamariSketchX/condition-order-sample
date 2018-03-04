package me.caosh.condition.interfaces.assembler;

import me.caosh.condition.domain.model.constants.SecurityExchange;
import me.caosh.condition.domain.model.constants.SecurityType;
import me.caosh.condition.domain.model.market.SecurityInfo;
import me.caosh.condition.domain.model.order.TradeCustomer;
import me.caosh.condition.domain.model.order.constant.StrategyState;
import me.caosh.condition.domain.model.order.grid.GridCondition;
import me.caosh.condition.domain.model.order.grid.GridTradeOrder;
import me.caosh.condition.domain.model.order.plan.DoubleDirectionTradePlan;
import me.caosh.condition.domain.model.order.plan.TradePlanFactory;
import me.caosh.condition.domain.model.share.ValuedEnumUtil;
import me.caosh.condition.interfaces.command.GridTradeOrderCreateCommand;
import me.caosh.condition.interfaces.command.GridTradeOrderUpdateCommand;

/**
 * Created by caosh on 2017/8/9.
 */
public class GirdTradeOrderCommandAssembler {
    public static GridTradeOrder assemble(Long orderId, TradeCustomer tradeCustomer, GridTradeOrderCreateCommand command) {
        StrategyState strategyState = StrategyState.ACTIVE;
        SecurityType securityType = ValuedEnumUtil.valueOf(command.getSecurityType(), SecurityType.class);
        SecurityExchange securityExchange = SecurityExchange.valueOf(command.getSecurityExchange());
        SecurityInfo securityInfo = new SecurityInfo(securityType, command.getSecurityCode(), securityExchange, command.getSecurityName());
        GridCondition gridCondition = new GridCondition(command.getGridLength(), command.getBasePrice());
        DoubleDirectionTradePlan tradePlan = TradePlanFactory.getInstance().createDouble(securityInfo, command.getEntrustStrategy(),
                command.getEntrustMethod(),
                command.getEntrustNumber(),
                command.getEntrustAmount());
        return new GridTradeOrder(orderId,
                tradeCustomer,
                securityInfo,
                gridCondition,
                tradePlan,
                strategyState);
    }

    public static GridTradeOrder merge(GridTradeOrder oldOrder, GridTradeOrderUpdateCommand command) {
        StrategyState strategyState = StrategyState.ACTIVE;
        GridCondition gridCondition = new GridCondition(command.getGridLength(), command.getBasePrice());
        DoubleDirectionTradePlan tradePlan = TradePlanFactory.getInstance().createDouble(oldOrder.getSecurityInfo(),
                command.getEntrustStrategy(),
                command.getEntrustMethod(),
                command.getEntrustNumber(),
                command.getEntrustAmount());
        return new GridTradeOrder(oldOrder.getOrderId(),
                oldOrder.getCustomer(),
                oldOrder.getSecurityInfo(),
                gridCondition, tradePlan, strategyState
        );
    }

    private GirdTradeOrderCommandAssembler() {
    }
}
