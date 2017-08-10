package me.caosh.condition.domain.model.signal;

/**
 * Created by caosh on 2017/8/1.
 */
public class SignalFactory {
    private static final SignalFactory INSTANCE = new SignalFactory();

    public static SignalFactory getInstance() {
        return INSTANCE;
    }

    private static final General general = new General();
    private static final None none = new None();

    public None none() {
        return none;
    }

    public General general() {
        return general;
    }

    private SignalFactory() {
    }
}
