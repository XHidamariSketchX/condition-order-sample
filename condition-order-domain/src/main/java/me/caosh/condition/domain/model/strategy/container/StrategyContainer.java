package me.caosh.condition.domain.model.strategy.container;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.*;
import hbec.intellitrade.strategy.domain.Strategy;
import me.caosh.condition.domain.model.market.MarketID;
import me.caosh.condition.domain.model.market.RealTimeMarket;
import me.caosh.condition.domain.model.signal.MarketSignalPayload;
import me.caosh.condition.domain.model.signal.Signal;
import me.caosh.condition.domain.model.signal.SignalPayload;
import me.caosh.condition.domain.model.signal.Signals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 基于Guava的multi-map实现的策略执行容器，以{@link BucketKey}为键，以{@link java.util.LinkedHashSet}作为策略集合
 * <p>
 * 要求策略实现equals & hashCode方法，hashCode相同的策略视为同一策略
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/1/31
 */
public class StrategyContainer {
    private static final Logger logger = LoggerFactory.getLogger(StrategyContainer.class);

    private final SetMultimap<BucketKey, StrategyContext> strategies = MultimapBuilder
            .<BucketKey, StrategyContext>hashKeys()
            .linkedHashSetValues()
            .build();
    private final StrategyContextFactory strategyContextFactory;

    public StrategyContainer() {
        this.strategyContextFactory = new StrategyContextFactory(StrategyContextConfig.DEFAULT);
    }

    public StrategyContainer(StrategyContextConfig strategyContextConfig) {
        this.strategyContextFactory = new StrategyContextFactory(strategyContextConfig);
    }

    /**
     * 添加或更新策略
     *
     * @param strategy 策略
     */
    public void add(Strategy strategy) {
        StrategyContext strategyContext = strategyContextFactory.create(strategy);
        BucketKey bucketKey = strategyContext.getBucketKey();
        Set<StrategyContext> bucket = strategies.get(bucketKey);
        bucket.remove(strategyContext);
        bucket.add(strategyContext);
    }

    /**
     * 删除策略
     *
     * @param strategyId 策略ID
     */
    public void remove(int strategyId) {
        Iterator<Map.Entry<BucketKey, StrategyContext>> iterator = strategies.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BucketKey, StrategyContext> entry = iterator.next();
            StrategyContext strategyContext = entry.getValue();
            Strategy strategy = strategyContext.getStrategy();
            if (strategy.getStrategyId() == strategyId) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * 获取marketID对应的策略集合
     *
     * @param bucketKey bucket key
     * @return 策略集合
     */
    public Set<Strategy> getBucket(BucketKey bucketKey) {
        return Sets.newHashSet(Iterables.transform(strategies.get(bucketKey), new Function<StrategyContext, Strategy>() {
            @Override
            public Strategy apply(StrategyContext strategyContext) {
                return strategyContext.getStrategy();
            }
        }));
    }

    /**
     * 接受实时行情数据
     *
     * @param realTimeMarkets 实时行情数据
     * @return 触发的信号集合
     */
    public Collection<SignalPayload> onMarketUpdate(Iterable<RealTimeMarket> realTimeMarkets) {
        List<SignalPayload> signalPayloads = Lists.newArrayList();
        for (RealTimeMarket realTimeMarket : realTimeMarkets) {
            MarketID marketID = realTimeMarket.getMarketID();
            Set<StrategyContext> bucket = strategies.get(marketID);
            for (StrategyContext strategyContext : bucket) {
                Strategy strategy = strategyContext.getStrategy();
                Signal tradeSignal = checkMarketCondition((MarketStrategyContext) strategyContext, realTimeMarket);
                if (tradeSignal.isValid()) {
                    MarketSignalPayload marketSignalPayload = new MarketSignalPayload(tradeSignal, strategy,
                            realTimeMarket);
                    signalPayloads.add(marketSignalPayload);
                }
            }
        }
        return signalPayloads;
    }

    /**
     * 接受秒级时间变化
     *
     * @return 触发的信号集合
     */
    public Collection<SignalPayload> onSecondTick() {
        List<SignalPayload> signalPayloads = Lists.newArrayList();
        for (StrategyContext strategyContext : strategies.values()) {
            Strategy strategy = strategyContext.getStrategy();
            Signal signal = checkTimeCondition(strategyContext);
            if (signal.isValid()) {
                SignalPayload signalPayload = new SignalPayload(signal, strategy);
                signalPayloads.add(signalPayload);
            }
        }
        return signalPayloads;
    }

    private Signal checkMarketCondition(MarketStrategyContext marketStrategyContext, RealTimeMarket realTimeMarket) {
        // 实现异常安全
        try {
            return marketStrategyContext.onMarketUpdate(realTimeMarket);
        } catch (Exception e) {
            logger.error("Check error, strategyContext=" + marketStrategyContext, e);
            return Signals.none();
        }
    }

    private Signal checkTimeCondition(StrategyContext strategyContext) {
        // 实现异常安全
        try {
            return strategyContext.onSecondTick();
        } catch (Exception e) {
            logger.error("Check error, strategyContext=" + strategyContext, e);
            return Signals.none();
        }
    }

    /**
     * 获取策略数量
     *
     * @return 策略数量
     */
    public int size() {
        return strategies.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StrategyContainer that = (StrategyContainer) o;

        return strategies.equals(that.strategies);
    }

    @Override
    public int hashCode() {
        return strategies.hashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(StrategyContainer.class).omitNullValues()
                .add("strategies", strategies)
                .add("strategyContextFactory", strategyContextFactory)
                .toString();
    }
}
