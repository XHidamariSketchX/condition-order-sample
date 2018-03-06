package me.caosh.condition.infrastructure.repository.model;

import com.google.common.base.MoreObjects;
import me.caosh.autoasm.MappedClass;
import hbec.intellitrade.strategy.domain.strategies.condition.PriceCondition;
import hbec.intellitrade.strategy.domain.strategies.condition.PriceConditionBuilder;

import java.math.BigDecimal;

/**
 * Created by caosh on 2017/8/15.
 */
@MappedClass(value = PriceCondition.class, builderClass = PriceConditionBuilder.class)
public class PriceConditionDO implements ConditionDO {
    private Integer compareOperator;
    private BigDecimal targetPrice;

    public PriceConditionDO() {
    }

    public PriceConditionDO(Integer compareOperator, BigDecimal targetPrice) {
        this.compareOperator = compareOperator;
        this.targetPrice = targetPrice;
    }

    public Integer getCompareOperator() {
        return compareOperator;
    }

    public void setCompareOperator(Integer compareOperator) {
        this.compareOperator = compareOperator;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    @Override
    public void accept(ConditionDOVisitor visitor) {
        visitor.visitPriceConditionDO(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("compareOperator", compareOperator)
                .add("targetPrice", targetPrice)
                .toString();
    }
}
