package hbec.intellitrade.strategy.domain;

import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.strategy.domain.condition.market.MarketCondition;
import hbec.intellitrade.strategy.domain.signal.TradeSignal;

/**
 * 行情驱动策略
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/1
 */
public interface MarketDrivenStrategy extends RealTimeMarketAware {
    /**
     * 行情驱动策略的条件必然是行情条件
     *
     * @return 行情条件
     */
    @Override
    MarketCondition getCondition();

    /**
     * 接受实时行情Tick返回交易信号
     * <p>
     * {@link TradeSignal#isValid()}返回false表示无信号
     *
     * @param realTimeMarket 实时消息
     * @return 交易信号
     */
    TradeSignal onMarketTick(RealTimeMarket realTimeMarket);
}
