package me.caosh.condition.domain.model.strategy.condition.delayconfirm;

import me.caosh.condition.domain.model.condition.PriceCondition;
import me.caosh.condition.domain.model.signal.Signals;
import me.caosh.condition.domain.model.strategy.factor.CompareOperator;
import me.caosh.condition.domain.util.MockMarkets;
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

        assertEquals(delayConfirmCondition.onMarketUpdate(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.onMarketUpdate(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.onMarketUpdate(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.buyOrSell());
    }

    @Test
    public void testCanceled() throws Exception {
        AccumulatedDelayConfirmCondition delayConfirmCondition = new AccumulatedDelayConfirmCondition(3,
                new PriceCondition(CompareOperator.GE, new BigDecimal("10.00")));

        assertEquals(delayConfirmCondition.onMarketUpdate(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.onMarketUpdate(MockMarkets.withCurrentPrice(new BigDecimal("9.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.onMarketUpdate(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.none());
        assertEquals(delayConfirmCondition.onMarketUpdate(MockMarkets.withCurrentPrice(new BigDecimal("10.00"))),
                Signals.buyOrSell());
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
