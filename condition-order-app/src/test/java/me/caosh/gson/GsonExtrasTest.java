package me.caosh.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import me.caosh.condition.domain.model.constants.SecurityExchange;
import me.caosh.condition.domain.model.market.SecurityInfo;
import me.caosh.condition.domain.model.constants.SecurityType;
import me.caosh.condition.domain.model.order.*;
import me.caosh.condition.domain.model.order.constant.CompareCondition;
import me.caosh.condition.domain.model.order.constant.EntrustStrategy;
import me.caosh.condition.domain.model.order.constant.ExchangeType;
import me.caosh.condition.domain.model.order.constant.OrderState;
import me.caosh.condition.domain.model.order.plan.TradeNumberDirect;
import me.caosh.condition.domain.model.order.plan.TradePlan;
import me.caosh.condition.domain.model.order.price.PriceCondition;
import me.caosh.condition.domain.model.order.price.PriceOrder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by caosh on 2017/8/3.
 */
public class GsonExtrasTest {
    private static final Logger logger = LoggerFactory.getLogger(GsonExtrasTest.class);

    @Test
    public void test() throws Exception {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ConditionOrder.class)
                        .registerSubtype(PriceOrder.class)
        ).create();

        TradeCustomerIdentity customerIdentity = new TradeCustomerIdentity(303348, "010000061086");
        PriceOrder priceOrder = new PriceOrder(123L, customerIdentity, false, OrderState.ACTIVE,
                new SecurityInfo(SecurityType.STOCK, "600000", SecurityExchange.SH, "PFYH"),
                new PriceCondition(CompareCondition.LESS_THAN_OR_EQUALS, new BigDecimal("13.00")),
                new TradePlan(ExchangeType.BUY, EntrustStrategy.CURRENT_PRICE, new TradeNumberDirect(100))
        );
        String json = gson.toJson(priceOrder);
        logger.info(json);
        PriceOrder recovered = (PriceOrder) gson.fromJson(json, ConditionOrder.class);
        assertEquals(priceOrder.getOrderId(), recovered.getOrderId());
        assertEquals(priceOrder.getSecurityInfo(), recovered.getSecurityInfo());
        assertEquals(priceOrder.getCondition(), recovered.getCondition());
        assertEquals(priceOrder.getTradePlan(), recovered.getTradePlan());
    }
}
