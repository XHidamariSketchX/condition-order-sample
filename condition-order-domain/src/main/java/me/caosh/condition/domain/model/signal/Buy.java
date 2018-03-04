package me.caosh.condition.domain.model.signal;

/**
 * Created by caosh on 2017/8/1.
 */
public class Buy extends BS {
    public Buy() {
    }

    public Buy(boolean deviationExceeded) {
        super(deviationExceeded);
    }

    @Override
    public TradeSignal withDeviationExceeded() {
        return new Buy(true);
    }

    @Override
    public void accept(SignalVisitor visitor) {
        visitor.visitBuy(this);
    }
}
