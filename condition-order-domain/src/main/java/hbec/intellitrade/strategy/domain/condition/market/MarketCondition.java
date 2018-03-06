package hbec.intellitrade.strategy.domain.condition.market;

import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.strategy.domain.signal.TradeSignal;
import hbec.intellitrade.strategy.domain.condition.Condition;

/**
 * 行情条件，在行情更新时根据具体的条件返回交易信号
 *
 * @author caoshuhao@touker.com
 * @date 2018/1/27
 */
public interface MarketCondition extends Condition {
    /**
     * 条件实体接受实时行情Tick返回交易信号
     * {@link TradeSignal#isValid()}返回false表示无信号
     *
     * @param realTimeMarket 实时消息
     * @return 交易信号
     */
    TradeSignal onMarketTick(RealTimeMarket realTimeMarket);
}
