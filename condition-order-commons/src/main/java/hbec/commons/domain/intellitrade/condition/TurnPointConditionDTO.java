package hbec.commons.domain.intellitrade.condition;

import com.google.common.base.MoreObjects;
import hbec.intellitrade.conditionorder.domain.orders.turnpoint.DecoratedTurnPointCondition;
import hbec.intellitrade.conditionorder.domain.orders.turnpoint.DecoratedTurnPointConditionBuilder;
import me.caosh.autoasm.FieldMapping;
import me.caosh.autoasm.MappedClass;

import java.math.BigDecimal;

/**
 * 拐点（回落）条件DTO
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/3/30
 */
@MappedClass(value = DecoratedTurnPointCondition.class, builderClass = DecoratedTurnPointConditionBuilder.class)
public class TurnPointConditionDTO implements ConditionDTO {
    private static final long serialVersionUID = 1L;

    private Integer compareOperator;
    private BigDecimal breakPrice;
    @FieldMapping(mappedProperty = "turnBackBinaryPriceFactor.binaryFactorType")
    private Integer binaryFactorType;
    @FieldMapping(mappedProperty = "turnBackBinaryPriceFactor.percent")
    private BigDecimal turnBackPercent;
    @FieldMapping(mappedProperty = "turnBackBinaryPriceFactor.increment")
    private BigDecimal turnBackIncrement;
    private Boolean useGuaranteedPrice;
    private BigDecimal baselinePrice;

    // ---------------------------------- Dynamic properties ----------------------------------

    private Boolean broken;
    private BigDecimal extremePrice;
    private Integer turnPointDelayConfirmedCount;
    private Integer crossDelayConfirmedCount;

    public TurnPointConditionDTO() {
    }

    public TurnPointConditionDTO(Integer compareOperator,
                                 BigDecimal breakPrice,
                                 Integer binaryFactorType,
                                 BigDecimal turnBackPercent,
                                 BigDecimal turnBackIncrement,
                                 Boolean useGuaranteedPrice,
                                 Boolean broken,
                                 BigDecimal extremePrice,
                                 Integer turnPointDelayConfirmedCount,
                                 Integer crossDelayConfirmedCount) {
        this.compareOperator = compareOperator;
        this.breakPrice = breakPrice;
        this.binaryFactorType = binaryFactorType;
        this.turnBackPercent = turnBackPercent;
        this.turnBackIncrement = turnBackIncrement;
        this.useGuaranteedPrice = useGuaranteedPrice;
        this.broken = broken;
        this.extremePrice = extremePrice;
        this.turnPointDelayConfirmedCount = turnPointDelayConfirmedCount;
        this.crossDelayConfirmedCount = crossDelayConfirmedCount;
    }

    public Integer getCompareOperator() {
        return compareOperator;
    }

    public void setCompareOperator(Integer compareOperator) {
        this.compareOperator = compareOperator;
    }

    public BigDecimal getBreakPrice() {
        return breakPrice;
    }

    public void setBreakPrice(BigDecimal breakPrice) {
        this.breakPrice = breakPrice;
    }

    public Integer getBinaryFactorType() {
        return binaryFactorType;
    }

    public void setBinaryFactorType(Integer binaryFactorType) {
        this.binaryFactorType = binaryFactorType;
    }

    public BigDecimal getTurnBackPercent() {
        return turnBackPercent;
    }

    public void setTurnBackPercent(BigDecimal turnBackPercent) {
        this.turnBackPercent = turnBackPercent;
    }

    public BigDecimal getTurnBackIncrement() {
        return turnBackIncrement;
    }

    public void setTurnBackIncrement(BigDecimal turnBackIncrement) {
        this.turnBackIncrement = turnBackIncrement;
    }

    public Boolean getUseGuaranteedPrice() {
        return useGuaranteedPrice;
    }

    public void setUseGuaranteedPrice(Boolean useGuaranteedPrice) {
        this.useGuaranteedPrice = useGuaranteedPrice;
    }

    public BigDecimal getBaselinePrice() {
        return baselinePrice;
    }

    public void setBaselinePrice(BigDecimal baselinePrice) {
        this.baselinePrice = baselinePrice;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }

    public BigDecimal getExtremePrice() {
        return extremePrice;
    }

    public void setExtremePrice(BigDecimal extremePrice) {
        this.extremePrice = extremePrice;
    }

    public Integer getTurnPointDelayConfirmedCount() {
        return turnPointDelayConfirmedCount;
    }

    public void setTurnPointDelayConfirmedCount(Integer turnPointDelayConfirmedCount) {
        this.turnPointDelayConfirmedCount = turnPointDelayConfirmedCount;
    }

    public Integer getCrossDelayConfirmedCount() {
        return crossDelayConfirmedCount;
    }

    public void setCrossDelayConfirmedCount(Integer crossDelayConfirmedCount) {
        this.crossDelayConfirmedCount = crossDelayConfirmedCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(TurnPointConditionDTO.class).omitNullValues()
                          .add("compareOperator", compareOperator)
                          .add("breakPrice", breakPrice)
                          .add("binaryFactorType", binaryFactorType)
                          .add("turnBackPercent", turnBackPercent)
                          .add("turnBackIncrement", turnBackIncrement)
                          .add("useGuaranteedPrice", useGuaranteedPrice)
                          .add("baselinePrice", baselinePrice)
                          .add("broken", broken)
                          .add("extremePrice", extremePrice)
                          .add("turnPointDelayConfirmedCount", turnPointDelayConfirmedCount)
                          .add("crossDelayConfirmedCount", crossDelayConfirmedCount)
                          .toString();
    }
}
