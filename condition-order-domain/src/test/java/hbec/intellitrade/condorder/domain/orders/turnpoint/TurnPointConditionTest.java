package hbec.intellitrade.condorder.domain.orders.turnpoint;

import hbec.intellitrade.strategy.domain.factor.CompareOperator;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Created by caosh on 2017/8/19.
 */
public class TurnPointConditionTest {
    @Test
    public void testTurnUp() throws Exception {
        TurnPointCondition turnPointCondition = new TurnPointCondition(CompareOperator.LE,
                                                                       BigDecimal.valueOf(11),
                                                                       BigDecimal.valueOf(1));
        assertFalse(turnPointCondition.getTargetPriceFactor().apply(new BigDecimal("10.01")));
        assertTrue(turnPointCondition.isDirty());
        turnPointCondition.clearDirty();

        assertFalse(turnPointCondition.getTargetPriceFactor().apply(new BigDecimal("10.00")));
        assertTrue(turnPointCondition.isDirty());
        turnPointCondition.clearDirty();

        assertFalse(turnPointCondition.getTargetPriceFactor().apply(new BigDecimal("10.01")));
        assertFalse(turnPointCondition.isDirty());

        // 9.9485
        assertFalse(turnPointCondition.getTargetPriceFactor().apply(new BigDecimal("9.85")));
        assertTrue(turnPointCondition.isDirty());
        turnPointCondition.clearDirty();

        assertFalse(turnPointCondition.getTargetPriceFactor().apply(new BigDecimal("9.94")));
        assertFalse(turnPointCondition.isDirty());

        assertTrue(turnPointCondition.getTargetPriceFactor().apply(new BigDecimal("9.95")));
    }
}
