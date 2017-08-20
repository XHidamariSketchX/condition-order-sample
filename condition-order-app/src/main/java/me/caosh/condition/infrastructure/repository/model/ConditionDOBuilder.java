package me.caosh.condition.infrastructure.repository.model;

import me.caosh.condition.domain.model.order.Condition;
import me.caosh.condition.domain.model.order.ConditionVisitor;
import me.caosh.condition.domain.model.order.price.PriceCondition;
import me.caosh.condition.domain.model.order.time.SimpleTimeCondition;
import me.caosh.condition.domain.model.order.turnpoint.TurnUpCondition;
import me.caosh.condition.domain.util.InstantUtils;

/**
 * Created by caosh on 2017/8/15.
 */
public class ConditionDOBuilder implements ConditionVisitor {

    private ConditionDO conditionDO;

    public ConditionDOBuilder(Condition condition) {
        condition.accept(this);
    }

    public ConditionDO build() {
        return conditionDO;
    }

    @Override
    public void visitPriceCondition(PriceCondition priceCondition) {
        this.conditionDO = new PriceConditionDO(priceCondition.getCompareCondition().getValue(), priceCondition.getTargetPrice());
    }

    @Override
    public void visitTurnUpCondition(TurnUpCondition turnUpCondition) {
        this.conditionDO = new TurnUpConditionDO(turnUpCondition.getBreakPrice(), turnUpCondition.getTurnUpPercent());
    }

    @Override
    public void visitSimpleTimeCondition(SimpleTimeCondition simpleTimeCondition) {
        this.conditionDO = new SimpleTimeConditionDO(InstantUtils.toDate(simpleTimeCondition.getTargetTime()));
    }
}
