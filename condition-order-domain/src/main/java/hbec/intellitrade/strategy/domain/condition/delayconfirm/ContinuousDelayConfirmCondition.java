package hbec.intellitrade.strategy.domain.condition.delayconfirm;

import com.google.common.base.MoreObjects;
import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.strategy.domain.condition.market.MarketCondition;
import hbec.intellitrade.strategy.domain.signal.Signals;
import hbec.intellitrade.strategy.domain.signal.TradeSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连续延迟确认
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/8
 */
public class ContinuousDelayConfirmCondition extends AbstractDelayConfirmCondition implements MarketCondition {
    private static final Logger logger = LoggerFactory.getLogger(ContinuousDelayConfirmCondition.class);


    public ContinuousDelayConfirmCondition(int confirmTimes, MarketCondition marketCondition) {
        super(confirmTimes, marketCondition);
    }

    public ContinuousDelayConfirmCondition(int confirmTimes, int confirmedCount, MarketCondition marketCondition) {
        super(confirmTimes, confirmedCount, marketCondition);
    }

    @Override
    public TradeSignal onMarketTick(RealTimeMarket realTimeMarket) {
        TradeSignal tradeSignal = marketCondition.onMarketTick(realTimeMarket);
        boolean tradeSignalValid = tradeSignal.isValid();
        if (!tradeSignalValid) {
            if (counter.getConfirmedCount() > 0) {
                counter.reset();
                logger.trace("Confirmed count reset due to NONE signal");
            }
            return tradeSignal;
        }

        counter.increaseConfirmedCount();
        if (counter.isConfirmCompleted()) {
            logger.trace("Confirmed count is enough, counter={}", counter);
            return tradeSignal;
        } else {
            logger.trace("Delay confirm, counter={}", counter);
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

        ContinuousDelayConfirmCondition that = (ContinuousDelayConfirmCondition) o;

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
        return MoreObjects.toStringHelper(ContinuousDelayConfirmCondition.class).omitNullValues()
                .add("counter", counter)
                .add("marketCondition", marketCondition)
                .toString();
    }
}
