package me.caosh.condition.infrastructure.tunnel.model;

import com.google.common.base.MoreObjects;
import hbec.intellitrade.conditionorder.domain.orders.price.DecoratedPriceCondition;
import hbec.intellitrade.conditionorder.domain.orders.price.DecoratedPriceConditionBuilder;
import me.caosh.autoasm.MappedClass;

import java.math.BigDecimal;

/**
 * Created by caosh on 2017/8/15.
 */
@MappedClass(value = DecoratedPriceCondition.class, builderClass = DecoratedPriceConditionBuilder.class)
public class PriceConditionDO implements ConditionDO {
    private Integer compareOperator;
    private BigDecimal targetPrice;

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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("compareOperator", compareOperator)
                .add("targetPrice", targetPrice)
                .toString();
    }
}
