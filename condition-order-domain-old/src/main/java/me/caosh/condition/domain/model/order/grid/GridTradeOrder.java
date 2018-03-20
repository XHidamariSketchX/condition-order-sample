package me.caosh.condition.domain.model.order.grid;

import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.common.security.SecurityInfo;
import hbec.intellitrade.condorder.domain.AbstractMarketConditionOrder;
import hbec.intellitrade.condorder.domain.OrderState;
import hbec.intellitrade.condorder.domain.TradeCustomerInfo;
import hbec.intellitrade.condorder.domain.strategyinfo.NativeStrategyInfo;
import hbec.intellitrade.condorder.domain.strategyinfo.StrategyInfo;
import hbec.intellitrade.condorder.domain.tradeplan.DoubleDirectionTradePlan;
import hbec.intellitrade.condorder.domain.tradeplan.TradePlan;
import hbec.intellitrade.condorder.domain.trigger.TriggerTradingContext;
import hbec.intellitrade.strategy.domain.condition.delayconfirm.DisabledDelayConfirm;
import hbec.intellitrade.strategy.domain.condition.deviation.DisabledDeviationCtrl;
import hbec.intellitrade.strategy.domain.condition.market.MarketCondition;
import hbec.intellitrade.strategy.domain.timerange.NoneMonitorTimeRange;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by caosh on 2017/8/23.
 */
public class GridTradeOrder extends AbstractMarketConditionOrder {
    private static final Logger logger = LoggerFactory.getLogger(GridTradeOrder.class);

    private final GridCondition gridCondition;
    private final DoubleDirectionTradePlan tradePlan;

    public GridTradeOrder(Long orderId,
                          TradeCustomerInfo tradeCustomerInfo,
                          SecurityInfo securityInfo,
                          GridCondition gridCondition,
                          LocalDateTime expireTime,
                          DoubleDirectionTradePlan tradePlan,
                          OrderState orderState) {
        super(orderId,
              tradeCustomerInfo,
              orderState,
              securityInfo,
              expireTime,
              null,
              NoneMonitorTimeRange.NONE,
              DisabledDelayConfirm.DISABLED,
              DisabledDeviationCtrl.DISABLED);
        this.gridCondition = gridCondition;
        this.tradePlan = tradePlan;
    }

    @Override
    public MarketCondition getCondition() {
        return getGridCondition();
    }

    public GridCondition getGridCondition() {
        return gridCondition;
    }

    @Override
    public StrategyInfo getStrategyInfo() {
        return NativeStrategyInfo.GRID;
    }

    @Override
    public TradePlan getTradePlan() {
        return tradePlan;
    }

    @Override
    public void afterEntrustCommandsExecuted(TriggerTradingContext triggerTradingContext) {
        RealTimeMarket realTimeMarket = triggerTradingContext.getTriggerMarket();
        logger.info("Changing base price {} => {}", gridCondition.getBasePrice(), realTimeMarket.getCurrentPrice());
        gridCondition.setBasePrice(realTimeMarket.getCurrentPrice());
    }
}
