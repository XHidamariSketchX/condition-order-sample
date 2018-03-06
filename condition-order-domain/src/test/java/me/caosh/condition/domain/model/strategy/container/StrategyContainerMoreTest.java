package me.caosh.condition.domain.model.strategy.container;

import hbec.intellitrade.common.market.MarketID;
import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.common.security.SecurityType;
import hbec.intellitrade.strategy.domain.factor.CompareOperator;
import me.caosh.condition.domain.model.condition.PriceCondition;
import me.caosh.condition.domain.model.condition.TurnUpCondition;
import me.caosh.condition.domain.model.signalpayload.SignalPayload;
import hbec.intellitrade.strategy.domain.signal.Signals;
import me.caosh.condition.domain.util.MockMarkets;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

/**
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/7
 */
public class StrategyContainerMoreTest {
    private static final MarketID MARKET_ID = new MarketID(SecurityType.STOCK, "600000");
    private static final PriceCondition PRICE_CONDITION = new PriceCondition(CompareOperator.GE, new BigDecimal("10.00"));

    @Test
    public void testExpire() throws Exception {
        StrategyContainer container = new StrategyContainer();
        TestPriceStrategy testPriceStrategy1 = new TestPriceStrategy(1, MARKET_ID, PRICE_CONDITION);
        TestPriceStrategy testPriceStrategy2 = new TestPriceStrategy(2, MARKET_ID, PRICE_CONDITION,
                LocalDateTime.now().plusMinutes(1));
        container.add(testPriceStrategy1);
        container.add(testPriceStrategy2);

        assertTrue(container.onSecondTick().isEmpty());

        testPriceStrategy2.setExpireTime(LocalDateTime.now());
        Collection<SignalPayload> signalPayloads = container.onSecondTick();
        assertEquals(signalPayloads.size(), 1);
        SignalPayload signalPayload = signalPayloads.iterator().next();
        assertEquals(signalPayload.getSignal(), Signals.expire());
        assertEquals(signalPayload.getStrategy(), testPriceStrategy2);
    }

    @Test
    public void testTriggerLock() throws Exception {
        StrategyContainer container = new StrategyContainer(new StrategyContextConfig(1, 1));
        TestPriceStrategy testPriceStrategy = new TestPriceStrategy(1, MARKET_ID, PRICE_CONDITION);
        container.add(testPriceStrategy);

        RealTimeMarket realTimeMarket1 = MockMarkets.withCurrentPrice(new BigDecimal("10.00"));
        Collection<SignalPayload> signalPayloads1 = container.onMarketUpdate(Collections.singleton(realTimeMarket1));
        assertEquals(signalPayloads1.size(), 1);
        assertEquals(signalPayloads1.iterator().next().getSignal(), Signals.buyOrSell());

        Collection<SignalPayload> signalPayloads2 = container.onMarketUpdate(Collections.singleton(realTimeMarket1));
        assertTrue(signalPayloads2.isEmpty());

        Collection<SignalPayload> signalPayloads21 = container.onSecondTick();
        assertTrue(signalPayloads21.isEmpty());

        TimeUnit.SECONDS.sleep(1);

        Collection<SignalPayload> signalPayloads22 = container.onSecondTick();
        assertTrue(signalPayloads22.isEmpty());

        Collection<SignalPayload> signalPayloads3 = container.onMarketUpdate(Collections.singleton(realTimeMarket1));
        assertEquals(signalPayloads3.size(), 1);
        assertEquals(signalPayloads3.iterator().next().getSignal(), Signals.buyOrSell());
    }

    @Test
    public void testDelaySync() throws Exception {
        StrategyContainer container = new StrategyContainer(new StrategyContextConfig(1, 1));
        TestTurnUpStrategy testTurnUpStrategy = new TestTurnUpStrategy(1, MARKET_ID,
                new TurnUpCondition(new BigDecimal("10.00"), new BigDecimal("1.00")));
        container.add(testTurnUpStrategy);

        RealTimeMarket realTimeMarket1 = MockMarkets.withCurrentPrice(new BigDecimal("9.00"));
        Collection<SignalPayload> signalPayloads1 = container.onMarketUpdate(Collections.singleton(realTimeMarket1));
        assertTrue(signalPayloads1.isEmpty());
        assertFalse(testTurnUpStrategy.isDirty());

        assertTrue(container.onSecondTick().isEmpty());

        TimeUnit.MILLISECONDS.sleep(500);
        RealTimeMarket realTimeMarket2 = MockMarkets.withCurrentPrice(new BigDecimal("9.01"));
        assertTrue(container.onMarketUpdate(Collections.singleton(realTimeMarket2)).isEmpty());

        TimeUnit.MILLISECONDS.sleep(500);
        Collection<SignalPayload> signalPayloads2 = container.onSecondTick();
        assertEquals(signalPayloads2.size(), 1);
        SignalPayload signalPayload = signalPayloads2.iterator().next();
        assertEquals(signalPayload.getSignal(), Signals.cacheSync());

        assertTrue(container.onMarketUpdate(Collections.singleton(realTimeMarket2)).isEmpty());
    }
}
