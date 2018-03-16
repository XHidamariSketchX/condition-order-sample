package me.caosh.condition.domain.model.order.newstock;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import hbec.intellitrade.common.security.SecurityInfo;
import hbec.intellitrade.common.security.SecurityType;
import hbec.intellitrade.condorder.domain.AbstractConditionOrder;
import hbec.intellitrade.condorder.domain.OrderState;
import hbec.intellitrade.condorder.domain.TradeCustomerInfo;
import hbec.intellitrade.condorder.domain.strategyinfo.NativeStrategyInfo;
import hbec.intellitrade.condorder.domain.strategyinfo.StrategyInfo;
import hbec.intellitrade.condorder.domain.tradeplan.AutoPurchaseTradePlan;
import hbec.intellitrade.condorder.domain.tradeplan.TradePlan;
import hbec.intellitrade.condorder.domain.trigger.TradingMarketSupplier;
import hbec.intellitrade.condorder.domain.trigger.TriggerTradingContext;
import hbec.intellitrade.strategy.domain.TimeDrivenStrategy;
import hbec.intellitrade.strategy.domain.condition.Condition;
import hbec.intellitrade.strategy.domain.signal.Signal;
import hbec.intellitrade.strategy.domain.signal.Signals;
import hbec.intellitrade.strategy.domain.signal.TradeSignal;
import hbec.intellitrade.trade.domain.EntrustCommand;
import hbec.intellitrade.trade.domain.EntrustSuccessResult;
import hbec.intellitrade.trade.domain.ExchangeType;
import hbec.intellitrade.trade.domain.OrderType;
import me.caosh.condition.domain.model.market.SecurityInfoConstants;
import me.caosh.condition.domain.model.newstock.NewStock;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by caosh on 2017/8/31.
 *
 * @author caoshuhao@touker.com
 */
public class NewStockOrder extends AbstractConditionOrder implements TimeDrivenStrategy {
    private static final Logger logger = LoggerFactory.getLogger(NewStockOrder.class);

    private final AutoPurchaseTradePlan autoPurchaseTradePlan = new AutoPurchaseTradePlan();
    private final NewStockPurchaseCondition newStockPurchaseCondition;

    public NewStockOrder(Long orderId, TradeCustomerInfo tradeCustomerInfo,
                         NewStockPurchaseCondition newStockPurchaseCondition,
                         LocalDateTime expireTime,
                         OrderState orderState) {
        super(orderId, tradeCustomerInfo, SecurityInfoConstants.NEW_STOCK_PURCHASE, expireTime, orderState);
        this.newStockPurchaseCondition = newStockPurchaseCondition;
    }

    public NewStockPurchaseCondition getNewStockPurchaseCondition() {
        return newStockPurchaseCondition;
    }

    public Condition getCondition() {
        return getNewStockPurchaseCondition();
    }

    public Condition getRawCondition() {
        return newStockPurchaseCondition;
    }

    public StrategyInfo getStrategyInfo() {
        return NativeStrategyInfo.NEW_STOCK;
    }

    @Override
    public Signal onTimeTick(LocalDateTime localDateTime) {
        if (isMonitoringState() && isExpiredAt(localDateTime)) {
            return Signals.expire();
        }

        if (getOrderState() != OrderState.ACTIVE) {
            return Signals.none();
        }

        // TODO: pull up
        return newStockPurchaseCondition.onTimeTick(LocalDateTime.now());
    }

    public List<EntrustCommand> createEntrustCommand(List<NewStock> currentPurchasable) {
        return Lists.transform(currentPurchasable, new Function<NewStock, EntrustCommand>() {
            @Override
            public EntrustCommand apply(NewStock newStock) {
                return createPurchaseCommand(newStock);
            }
        });
    }

    private EntrustCommand createPurchaseCommand(NewStock newStock) {
        SecurityInfo securityInfo = new SecurityInfo(SecurityType.STOCK, newStock.getPurchaseCode(),
                newStock.getSecurityExchange(), newStock.getPurchaseName());
        return new EntrustCommand(securityInfo, ExchangeType.QUOTA_PURCHASE,
                newStock.getIssuePrice(), newStock.getPurchaseUpperLimit(), OrderType.LIMITED);
    }

    @Override
    public List<EntrustCommand> createEntrustCommands(TradeSignal tradeSignal, TradingMarketSupplier tradingMarketSupplier) {
        return Collections.emptyList();
    }

    @Override
    public void afterEntrustSuccess(TriggerTradingContext triggerTradingContext, EntrustCommand entrustCommand, EntrustSuccessResult entrustSuccessResult) {
        newStockPurchaseCondition.increasePurchasedCount();
    }

    @Override
    public void afterEntrustCommandsExecuted(TriggerTradingContext triggerTradingContext) {
        newStockPurchaseCondition.setTriggeredToday();
    }

    @Override
    public TradePlan getTradePlan() {
        return autoPurchaseTradePlan;
    }
}
