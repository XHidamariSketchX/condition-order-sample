package me.caosh.condition.domain.model.strategy.container;

import me.caosh.condition.domain.model.market.MarketID;
import me.caosh.condition.domain.model.strategy.RealTimeMarketAware;
import me.caosh.condition.domain.model.strategy.Strategy;

/**
 * {@link StrategyContext}工厂
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/7
 */
public class StrategyContextFactory {

    private final StrategyContextConfig strategyContextConfig;

    public StrategyContextFactory(StrategyContextConfig strategyContextConfig) {
        this.strategyContextConfig = strategyContextConfig;
    }

    /**
     * 根据策略类型创建策略运行上下文
     *
     * @param strategy 策略
     * @return 策略上下文
     */
    public StrategyContext create(Strategy strategy) {
        if (strategy instanceof RealTimeMarketAware) {
            RealTimeMarketAware realTimeMarketAware = (RealTimeMarketAware) strategy;
            MarketID trackMarketID = realTimeMarketAware.getTrackMarketID();
            return new MarketStrategyContext(trackMarketID, realTimeMarketAware, strategyContextConfig);
        }
        return new StrategyContext(TimeDrivenBucketKey.INSTANCE, strategy, strategyContextConfig);
    }
}
