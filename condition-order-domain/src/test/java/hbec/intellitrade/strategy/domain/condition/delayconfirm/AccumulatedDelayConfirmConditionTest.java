package hbec.intellitrade.strategy.domain.condition.delayconfirm;

import hbec.intellitrade.conditionorder.domain.orders.price.PriceCondition;
import hbec.intellitrade.mock.MockMarkets;
import hbec.intellitrade.strategy.domain.factor.CompareOperator;
import hbec.intellitrade.strategy.domain.signal.Signals;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

/**
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/8
 */
public class AccumulatedDelayConfirmConditionTest {
    @Test
    public void testBasic() throws Exception {
        AccumulatedDelayConfirmCondition delayConfirmCondition = new AccumulatedDelayConfirmCondition(3,
                new PriceCondition(CompareOperator.GE, new BigDecimal("10.00")));
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 0);

        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 1);
        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 2);
        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.buyOrSell());
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 3);
    }

    @Test
    public void testCanceled() throws Exception {
        AccumulatedDelayConfirmCondition delayConfirmCondition = new AccumulatedDelayConfirmCondition(3,
                new PriceCondition(CompareOperator.GE, new BigDecimal("10.00")));

        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("9.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 1);
        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.buyOrSell());
    }

    @Test
    public void testReset() throws Exception {
        AccumulatedDelayConfirmCondition delayConfirmCondition =
                new AccumulatedDelayConfirmCondition(3,
                                                     new PriceCondition(CompareOperator.GE, new BigDecimal("10.00")));

        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                     Signals.none());
        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("9.00"))),
                     Signals.none());
        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                     Signals.none());
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 2);

        delayConfirmCondition.resetCounter();
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 0);

        assertEquals(delayConfirmCondition.onMarketTick(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                     Signals.none());
        assertEquals(delayConfirmCondition.getDelayConfirmedCount(), 1);
    }

    @Test
    public void testEquals() throws Exception {
        AccumulatedDelayConfirmCondition delayConfirmCondition1 = new AccumulatedDelayConfirmCondition(3,
                new PriceCondition(CompareOperator.GE, new BigDecimal("10.00")));

        AccumulatedDelayConfirmCondition delayConfirmCondition2 = new AccumulatedDelayConfirmCondition(3,
                new PriceCondition(CompareOperator.GE, new BigDecimal("10.00")));

        assertEquals(delayConfirmCondition1, delayConfirmCondition2);
        assertEquals(delayConfirmCondition1.hashCode(), delayConfirmCondition2.hashCode());
        System.out.println(delayConfirmCondition1);
    }
}
