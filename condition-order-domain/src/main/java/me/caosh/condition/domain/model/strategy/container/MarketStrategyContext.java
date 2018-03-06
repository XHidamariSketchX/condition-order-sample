package me.caosh.condition.domain.model.strategy.container;

import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.strategy.domain.MarketDrivenStrategy;
import hbec.intellitrade.strategy.domain.RealTimeMarketAware;
import hbec.intellitrade.strategy.domain.Strategy;
import me.caosh.condition.domain.model.order.constant.StrategyState;
import me.caosh.condition.domain.model.signal.Signal;
import me.caosh.condition.domain.model.signal.Signals;
import me.caosh.condition.domain.model.signal.TradeSignal;
import hbec.intellitrade.strategy.domain.shared.DirtyFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 需要接受实时行情的策略上下文，其策略不一定是行情驱动策略
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/7
 * @see RealTimeMarketAware
 */
public class MarketStrategyContext extends StrategyContext {
    private static final Logger logger = LoggerFactory.getLogger(MarketStrategyContext.class);

    public MarketStrategyContext(BucketKey bucketKey, Strategy strategy, StrategyContextConfig strategyContextConfig) {
        super(bucketKey, strategy, strategyContextConfig);
    }

    @Override
    public RealTimeMarketAware getStrategy() {
        return (RealTimeMarketAware) super.getStrategy();
    }

    /**
     * 接受实时行情，如果是行情驱动策略，进行条件计算，否则不处理
     *
     * @param realTimeMarket 实时行情
     * @return 信号
     */
    public Signal onMarketUpdate(RealTimeMarket realTimeMarket) {
        if (isTriggerLocked()) {
            logger.warn("Trigger locked, strategyId={}, lockedDuration={}", getStrategy().getStrategyId(),
                    getTriggerLockedDuration());
            return Signals.none();
        }

        if (getStrategy().getStrategyState() != StrategyState.ACTIVE) {
            logger.warn("Illegal state, strategyId={}, orderState={}", getStrategy().getStrategyId(),
                    getStrategy().getStrategyState());
            return Signals.none();
        }

        // 非行情驱动策略不计算
        if (!(getStrategy() instanceof MarketDrivenStrategy)) {
            return Signals.none();
        }

        TradeSignal tradeSignal = ((MarketDrivenStrategy) getStrategy()).getCondition().onMarketUpdate(realTimeMarket);
        if (tradeSignal.isValid()) {
            lockTriggering();
            return tradeSignal;
        }

        // 未触发有效交易信号的，判断是否需要延迟同步
        if (getStrategy() instanceof DirtyFlag) {
            DirtyFlag dirtyFlag = (DirtyFlag) getStrategy();
            if (dirtyFlag.isDirty()) {
                markDelaySync();
                logger.info("Mark delay sync, strategyId={}, condition={}", getStrategy().getStrategyId(),
                        getStrategy().getCondition());
                // 清除脏标志，下次动态属性变更时再标记
                dirtyFlag.clearDirty();
            }
        }
        return tradeSignal;
    }
}
