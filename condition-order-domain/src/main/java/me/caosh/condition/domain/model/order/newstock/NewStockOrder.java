package me.caosh.condition.domain.model.order.newstock;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import me.caosh.condition.domain.model.constants.SecurityType;
import me.caosh.condition.domain.model.market.RealTimeMarket;
import me.caosh.condition.domain.model.market.SecurityInfo;
import me.caosh.condition.domain.model.market.SecurityInfoConstants;
import me.caosh.condition.domain.model.newstock.NewStock;
import me.caosh.condition.domain.model.order.*;
import me.caosh.condition.domain.model.order.constant.ExchangeType;
import me.caosh.condition.domain.model.order.constant.StrategyState;
import me.caosh.condition.domain.model.order.plan.AutoPurchaseTradePlan;
import me.caosh.condition.domain.model.order.plan.TradePlan;
import me.caosh.condition.domain.model.signal.TradeSignal;
import me.caosh.condition.domain.model.strategy.condition.Condition;
import me.caosh.condition.domain.model.strategy.description.NativeStrategyInfo;
import me.caosh.condition.domain.model.trade.*;

import java.util.List;

/**
 * Created by caosh on 2017/8/31.
 *
 * @author caoshuhao@touker.com
 */
public class NewStockOrder extends AbstractConditionOrder implements NewStockPurchaseOnTrigger,
        EntrustWithoutMarket, EntrustResultAware, TriggerPhaseListener {
    private final AutoPurchaseTradePlan autoPurchaseTradePlan = new AutoPurchaseTradePlan();
    private final NewStockPurchaseCondition newStockPurchaseCondition;

    public NewStockOrder(Long orderId, TradeCustomerInfo tradeCustomerInfo,
                         NewStockPurchaseCondition newStockPurchaseCondition, StrategyState strategyState) {
        super(orderId, tradeCustomerInfo, SecurityInfoConstants.NEW_STOCK_PURCHASE, NativeStrategyInfo.NEW_STOCK, strategyState);
        this.newStockPurchaseCondition = newStockPurchaseCondition;
    }

    public NewStockPurchaseCondition getNewStockPurchaseCondition() {
        return newStockPurchaseCondition;
    }

    @Override
    public Condition getCondition() {
        return getNewStockPurchaseCondition();
    }

    @Override
    public Condition getRawCondition() {
        return newStockPurchaseCondition;
    }

    @Override
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
    public void onTradeSignal(TradeSignal tradeSignal, RealTimeMarket realTimeMarket) {

    }

    @Override
    public void afterEntrustReturned(TriggerContext triggerContext, EntrustResult entrustResult) {
        newStockPurchaseCondition.increasePurchasedCount();
    }

    @Override
    public void afterEntrustCommandsExecuted(TriggerContext triggerContext) {
        newStockPurchaseCondition.setTriggeredToday();
    }

    @Override
    public TradePlan getTradePlan() {
        return autoPurchaseTradePlan;
    }
}
