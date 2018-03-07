package me.caosh.condition.domain.dto.order;

import com.google.common.base.MoreObjects;
import hbec.intellitrade.strategy.domain.strategies.condition.PriceCondition;
import hbec.intellitrade.strategy.domain.strategies.condition.PriceConditionBuilder;
import me.caosh.autoasm.MappedClass;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by caosh on 2017/8/11.
 *
 * @author caoshuhao@touker.com
 */
@MappedClass(value = PriceCondition.class, builderClass = PriceConditionBuilder.class)
public class PriceConditionDTO implements ConditionDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Range(min = 0, max = 1)
    private Integer compareOperator;
    @NotNull
    @DecimalMin("0")
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
    public void accept(ConditionDTOVisitor visitor) {
        visitor.visitPriceConditionDTO(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(PriceConditionDTO.class).omitNullValues()
                .add("compareOperator", compareOperator)
                .add("targetPrice", targetPrice)
                .toString();
    }
}
