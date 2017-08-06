package me.caosh.condition.domain.model.order;

import me.caosh.condition.domain.model.share.ValuedEnum;

/**
 * Created by caosh on 2017/8/2.
 */
public enum PriceDirection implements ValuedEnum<Integer> {
    UPWARD(1),
    DOWNWARD(2);

    int value;

    PriceDirection(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
