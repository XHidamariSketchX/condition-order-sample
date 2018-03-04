package me.caosh.condition.domain.model.order.price;

import hbec.intellitrade.strategy.domain.factor.CompareOperator;
import me.caosh.condition.domain.model.condition.PriceCondition;
import me.caosh.condition.domain.model.constants.SecurityExchange;
import me.caosh.condition.domain.model.constants.SecurityType;
import me.caosh.condition.domain.model.market.RealTimeMarket;
import me.caosh.condition.domain.model.market.SecurityInfo;
import me.caosh.condition.domain.model.order.TradeCustomer;
import me.caosh.condition.domain.model.order.TradeCustomerInfo;
import me.caosh.condition.domain.model.order.WrapperTradingMarketSupplier;
import me.caosh.condition.domain.model.order.constant.EntrustStrategy;
import me.caosh.condition.domain.model.order.constant.ExchangeType;
import me.caosh.condition.domain.model.order.constant.StrategyState;
import me.caosh.condition.domain.model.order.plan.BasicTradePlan;
import me.caosh.condition.domain.model.order.plan.TradeNumberDirect;
import me.caosh.condition.domain.model.signal.Signal;
import me.caosh.condition.domain.model.signal.Signals;
import me.caosh.condition.domain.model.signal.TradeSignal;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.testng.Assert.assertEquals;

/**
 * Created by caosh on 2017/8/31.
 *
 * @author caoshuhao@touker.com
 */
public class PriceOrderTest {

    @Test
    public void test() throws Exception {
        TradeCustomerInfo tradeCustomerInfo = new TradeCustomerInfo(303348, "010000061086");
        SecurityInfo pfyh = new SecurityInfo(SecurityType.STOCK, "600000", SecurityExchange.SH, "PFYH");
        ExchangeType exchangeType = ExchangeType.BUY;
        PriceOrder priceOrder = new PriceOrder(123L, tradeCustomerInfo, pfyh,
                new PriceCondition(CompareOperator.LE, new BigDecimal("13.00")),
                new BasicTradePlan(exchangeType, EntrustStrategy.CURRENT_PRICE, new TradeNumberDirect(100)),
                StrategyState.ACTIVE);

        assertEquals(priceOrder.getCondition().onMarketUpdate(new RealTimeMarket(pfyh.getMarketID(), new BigDecimal("13.01"),
                        Collections.<BigDecimal>emptyList())),
                Signals.none());

        final RealTimeMarket realTimeMarket = new RealTimeMarket(pfyh.getMarketID(), new BigDecimal("13.00"),
                Collections.<BigDecimal>emptyList());
        Signal signal = priceOrder.getCondition().onMarketUpdate(realTimeMarket);
        assertEquals(signal, Signals.buyOrSell());

        TradeCustomer tradeCustomer = new TradeCustomer(303348, "010000061086");
        priceOrder.onTradeSignal((TradeSignal) signal, tradeCustomer, new WrapperTradingMarketSupplier(realTimeMarket));
        assertEquals(priceOrder.getStrategyState(), StrategyState.TERMINATED);
    }

    @Test
    public void testBasic() throws Exception {
        TradeCustomerInfo tradeCustomerInfo = new TradeCustomerInfo(303348, "010000061086");
        SecurityInfo pfyh = new SecurityInfo(SecurityType.STOCK, "600000", SecurityExchange.SH, "PFYH");
        ExchangeType exchangeType = ExchangeType.BUY;

        PriceOrder priceOrder1 = new PriceOrder(123L, tradeCustomerInfo, pfyh,
                new PriceCondition(CompareOperator.LE, new BigDecimal("13.00")),
                new BasicTradePlan(exchangeType, EntrustStrategy.CURRENT_PRICE, new TradeNumberDirect(100)),
                StrategyState.ACTIVE);
        PriceOrder priceOrder2 = new PriceOrder(123L, tradeCustomerInfo, pfyh,
                new PriceCondition(CompareOperator.LE, new BigDecimal("13.00")),
                new BasicTradePlan(exchangeType, EntrustStrategy.CURRENT_PRICE, new TradeNumberDirect(100)),
                StrategyState.ACTIVE);

        assertEquals(priceOrder1, priceOrder2);
        assertEquals(priceOrder1.hashCode(), priceOrder2.hashCode());
    }
}
