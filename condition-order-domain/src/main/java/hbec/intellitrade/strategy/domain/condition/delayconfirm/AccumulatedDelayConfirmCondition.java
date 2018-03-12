package hbec.intellitrade.strategy.domain.condition.delayconfirm;

import com.google.common.base.MoreObjects;
import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.strategy.domain.condition.market.MarketCondition;
import hbec.intellitrade.strategy.domain.signal.Signals;
import hbec.intellitrade.strategy.domain.signal.TradeSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 累计延迟确认实现
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/8
 */
public class AccumulatedDelayConfirmCondition extends AbstractDelayConfirmCondition implements MarketCondition {
    private static final Logger logger = LoggerFactory.getLogger(AccumulatedDelayConfirmCondition.class);

    public AccumulatedDelayConfirmCondition(int confirmTimes, MarketCondition marketCondition) {
        super(confirmTimes, marketCondition);
    }

    @Override
    public TradeSignal onMarketTick(RealTimeMarket realTimeMarket) {
        TradeSignal tradeSignal = marketCondition.onMarketTick(realTimeMarket);
        if (!tradeSignal.isValid()) {
            return tradeSignal;
        }

        counter.increaseConfirmedCount();
        if (counter.isConfirmCompleted()) {
            // TODO: log with order id
            logger.info("Confirmed count is enough, counter={}", counter);
            return tradeSignal;
        } else {
            logger.info("Delay confirm, counter={}", counter);
            return Signals.none();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccumulatedDelayConfirmCondition that = (AccumulatedDelayConfirmCondition) o;

        if (!counter.equals(that.counter)) {
            return false;
        }
        return marketCondition.equals(that.marketCondition);
    }

    @Override
    public int hashCode() {
        int result = counter.hashCode();
        result = 31 * result + marketCondition.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(AccumulatedDelayConfirmCondition.class).omitNullValues()
                .add("counter", counter)
                .add("marketCondition", marketCondition)
                .toString();
    }
}
