package hbec.intellitrade.strategy.container;

import com.google.common.base.MoreObjects;
import hbec.intellitrade.common.market.MarketID;
import hbec.intellitrade.common.market.RealTimeMarket;
import hbec.intellitrade.condorder.domain.OrderState;
import hbec.intellitrade.condorder.domain.orders.turnpoint.TurnPointCondition;
import hbec.intellitrade.strategy.domain.MarketDrivenStrategy;
import hbec.intellitrade.strategy.domain.MutableStrategy;
import hbec.intellitrade.strategy.domain.TimeDrivenStrategy;
import hbec.intellitrade.strategy.domain.signal.Signal;
import hbec.intellitrade.strategy.domain.signal.Signals;
import hbec.intellitrade.strategy.domain.signal.TradeSignal;
import org.joda.time.LocalDateTime;

/**
 * @author caosh/caoshuhao@touker.com
 * @date 2018/2/8
 */
public class TestTurnUpStrategy implements MarketDrivenStrategy, TimeDrivenStrategy, MutableStrategy {
    private final int strategyId;
    private final MarketID marketID;
    private final TurnPointCondition turnPointCondition;
    private OrderState orderState = OrderState.ACTIVE;

    public TestTurnUpStrategy(int strategyId, MarketID marketID, TurnPointCondition turnPointCondition) {
        this.strategyId = strategyId;
        this.marketID = marketID;
        this.turnPointCondition = turnPointCondition;
    }

    public long getStrategyId() {
        return strategyId;
    }

    @Override
    public MarketID getTrackMarketID() {
        return marketID;
    }

    @Override
    public TradeSignal onMarketTick(RealTimeMarket realTimeMarket) {
        return turnPointCondition.onMarketTick(realTimeMarket);
    }

    @Override
    public Signal onTimeTick(LocalDateTime localDateTime) {
        return Signals.none();
    }

    @Override
    public boolean isDirty() {
        return turnPointCondition.isDirty();
    }

    @Override
    public void clearDirty() {
        turnPointCondition.clearDirty();
    }

    @Override
    public boolean isPersistentPropertyDirty() {
        return isDirty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestTurnUpStrategy that = (TestTurnUpStrategy) o;

        if (strategyId != that.strategyId) return false;
        if (!marketID.equals(that.marketID)) return false;
        return turnPointCondition.equals(that.turnPointCondition);
    }

    @Override
    public int hashCode() {
        int result = strategyId;
        result = 31 * result + marketID.hashCode();
        result = 31 * result + turnPointCondition.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(TestTurnUpStrategy.class).omitNullValues()
                          .add("strategyId", strategyId)
                          .add("marketID", marketID)
                          .add("turnPointCondition", turnPointCondition)
                          .add("strategyState", orderState)
                          .toString();
    }
}
