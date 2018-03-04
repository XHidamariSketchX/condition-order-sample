package me.caosh.condition.domain.model.strategy.condition.deviation;

import me.caosh.condition.domain.model.market.RealTimeMarket;
import me.caosh.condition.domain.model.order.ConditionVisitor;
import me.caosh.condition.domain.model.signal.TradeSignal;
import me.caosh.condition.domain.model.strategy.condition.market.PredictableMarketCondition;
import me.caosh.condition.domain.model.strategy.factor.TargetPriceFactor;
import me.caosh.condition.domain.model.strategy.shared.BigDecimalRanges;
import me.caosh.condition.domain.model.strategy.shared.Range;

import java.math.BigDecimal;

/**
 * 启用状态下的偏差控制条件包装类，包装可预测的行情条件和偏差控制参数，实现偏差控制条件接口
 *
 * @author caoshuhao@touker.com
 * @date 2018/1/28
 */
public class EnabledDeviationCtrlCondition implements DeviationCtrlCondition, PredictableMarketCondition {
    private final PredictableMarketCondition predictableMarketCondition;
    private final BigDecimal deviationLimitPercent;

    public EnabledDeviationCtrlCondition(PredictableMarketCondition predictableMarketCondition, BigDecimal deviationLimitPercent) {
        this.predictableMarketCondition = predictableMarketCondition;
        this.deviationLimitPercent = deviationLimitPercent;
    }

    public BigDecimal getDeviationLimitPercent() {
        return deviationLimitPercent;
    }

    @Override
    public TargetPriceFactor getTargetPriceFactor() {
        return predictableMarketCondition.getTargetPriceFactor();
    }

    @Override
    public TradeSignal onMarketUpdate(RealTimeMarket realTimeMarket) {
        TradeSignal tradeSignal = predictableMarketCondition.onMarketUpdate(realTimeMarket);
        if (!tradeSignal.isValid()) {
            return tradeSignal;
        }

        boolean deviationLimitExceeded = isDeviationLimitExceeded(realTimeMarket);
        if (!deviationLimitExceeded) {
            return tradeSignal;
        }

        return tradeSignal.withDeviationExceeded();
    }

    @Override
    public void accept(ConditionVisitor visitor) {
        predictableMarketCondition.accept(visitor);
    }

    /**
     * 在行情条件达成，且开启偏差控制情况下，判断是否超出偏差限制
     *
     * @param realTimeMarket 实时行情
     * @return 超出偏差控制
     */
    private boolean isDeviationLimitExceeded(RealTimeMarket realTimeMarket) {
        TargetPriceFactor targetPriceFactor = predictableMarketCondition.getTargetPriceFactor();
        BigDecimal targetPrice = targetPriceFactor.getTargetPrice();

        // 偏差控制允许的区间
        // TODO: 是否可以不使用区间，直接使用>=或<=
        Range<BigDecimal> deviationLimitedRange = BigDecimalRanges.openCenterWithPercent(
                targetPrice, deviationLimitPercent);

        boolean inRange = deviationLimitedRange.isInRange(realTimeMarket.getCurrentPrice());
        return !inRange;
    }
}
