package me.caosh.condition.domain.model.order.grid;

import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.common.market.RealTimeMarketSupplier;
import hbec.intellitrade.common.security.SecurityExchange;
import hbec.intellitrade.common.security.SecurityInfo;
import hbec.intellitrade.common.security.SecurityType;
import hbec.intellitrade.trade.domain.EntrustOrderWriter;
import hbec.intellitrade.trade.domain.EntrustResult;
import me.caosh.condition.domain.model.account.TradeCustomer;
import me.caosh.condition.domain.model.constants.EntrustMethod;
import me.caosh.condition.domain.model.order.TriggerTradingContext;
import me.caosh.condition.domain.model.order.TradeCustomerInfo;
import me.caosh.condition.domain.model.order.BasicTriggerTradingContext;
import me.caosh.condition.domain.model.order.WrapperTradingMarketSupplier;
import me.caosh.condition.domain.model.order.constant.EntrustStrategy;
import me.caosh.condition.domain.model.order.constant.StrategyState;
import me.caosh.condition.domain.model.order.plan.DoubleDirectionTradePlan;
import me.caosh.condition.domain.model.order.plan.TradePlanFactory;
import hbec.intellitrade.strategy.domain.signal.Signal;
import hbec.intellitrade.strategy.domain.signal.Signals;
import hbec.intellitrade.strategy.domain.signal.TradeSignal;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.testng.Assert.assertEquals;

/**
 * Created by caosh on 2017/8/31.
 *
 * @author caoshuhao@touker.com
 */
public class GridOrderTest {
    @Mock
    private RealTimeMarketSupplier realTimeMarketSupplier;

    @Mock
    private EntrustOrderWriter entrustOrderWriter;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() throws Exception {
        TradeCustomerInfo tradeCustomerInfo = new TradeCustomerInfo(303348, "010000061086");
        SecurityInfo pfyh = new SecurityInfo(SecurityType.STOCK, "600000", SecurityExchange.SH, "PFYH");
        GridCondition gridCondition = new GridCondition(new BigDecimal("1.00"), new BigDecimal("13.00"));
        DoubleDirectionTradePlan tradePlan = TradePlanFactory.getInstance().createDouble(
                pfyh, EntrustStrategy.CURRENT_PRICE.getValue(), EntrustMethod.AMOUNT.getValue(), 0, new BigDecimal("4500"));
        GridTradeOrder gridTradeOrder = new GridTradeOrder(123L, tradeCustomerInfo, pfyh, gridCondition,
                tradePlan, StrategyState.ACTIVE);

        assertEquals(Signals.none(),
                gridTradeOrder.getCondition().onMarketTick(new RealTimeMarket(pfyh.getMarketID(), new BigDecimal("13.01"),
                        Collections.<BigDecimal>emptyList())));

        RealTimeMarket realTimeMarket = new RealTimeMarket(pfyh.getMarketID(), new BigDecimal("14.00"),
                Collections.<BigDecimal>emptyList());
        Signal signal = gridTradeOrder.getCondition().onMarketTick(realTimeMarket);
        assertEquals(Signals.sell(), signal);
        TradeCustomer tradeCustomer = new TradeCustomer(303348, "010000061086");
        TriggerTradingContext triggerTradingContext = new BasicTriggerTradingContext(signal, gridTradeOrder, tradeCustomer,
                realTimeMarketSupplier, entrustOrderWriter, realTimeMarket);

//        assertEquals(new EntrustCommand(pfyh, ExchangeType.SELL, new BigDecimal("14.00"), 300, OrderType.LIMITED),
//                gridTradeOrder.onTradeSignal2((TradeSignal) signal, realTimeMarket));
        gridTradeOrder.createEntrustCommands((TradeSignal) signal, new WrapperTradingMarketSupplier(realTimeMarket));

        gridTradeOrder.afterEntrustSuccess(triggerTradingContext, null,
                new EntrustResult(EntrustResult.SUCCESS, "OK", 456));
        gridTradeOrder.afterEntrustCommandsExecuted(triggerTradingContext);
        assertEquals(gridTradeOrder.getStrategyState(), StrategyState.ACTIVE);
        assertEquals(gridTradeOrder.getGridCondition().getBasePrice(), new BigDecimal("14.00"));

        realTimeMarket = new RealTimeMarket(pfyh.getMarketID(), new BigDecimal("13.00"),
                Collections.<BigDecimal>emptyList());
        signal = gridTradeOrder.getCondition().onMarketTick(realTimeMarket);
        assertEquals(Signals.buy(), signal);
        triggerTradingContext = new BasicTriggerTradingContext(signal, gridTradeOrder, tradeCustomer,
                realTimeMarketSupplier, entrustOrderWriter, realTimeMarket);

//        assertEquals(new EntrustCommand(pfyh, ExchangeType.BUY, new BigDecimal("13.00"), 300, OrderType.LIMITED),
//                gridTradeOrder.onTradeSignal(signal, realTimeMarket));
        gridTradeOrder.createEntrustCommands((TradeSignal) signal, new WrapperTradingMarketSupplier(realTimeMarket));

        gridTradeOrder.afterEntrustSuccess(triggerTradingContext, null,
                new EntrustResult(EntrustResult.SUCCESS, "OK", 457));
        gridTradeOrder.afterEntrustCommandsExecuted(triggerTradingContext);
        assertEquals(gridTradeOrder.getStrategyState(), StrategyState.ACTIVE);
        assertEquals(gridTradeOrder.getGridCondition().getBasePrice(), new BigDecimal("13.00"));
    }
}
