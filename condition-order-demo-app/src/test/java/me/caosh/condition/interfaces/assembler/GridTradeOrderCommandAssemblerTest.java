package me.caosh.condition.interfaces.assembler;

import hbec.commons.domain.intellitrade.conditionorder.BidirectionalTradePlanDTO;
import hbec.commons.domain.intellitrade.conditionorder.DelayConfirmDTO;
import hbec.commons.domain.intellitrade.conditionorder.DeviationCtrlDTO;
import hbec.commons.domain.intellitrade.conditionorder.MonitorTimeRangeDTO;
import hbec.commons.domain.intellitrade.security.SecurityInfoDTO;
import hbec.intellitrade.common.security.SecurityExchange;
import hbec.intellitrade.common.security.SecurityInfo;
import hbec.intellitrade.common.security.SecurityType;
import hbec.intellitrade.conditionorder.domain.OrderState;
import hbec.intellitrade.conditionorder.domain.TradeCustomerInfo;
import hbec.intellitrade.conditionorder.domain.orders.grid.DecoratedGridCondition;
import hbec.intellitrade.conditionorder.domain.orders.grid.GridTradeOrder;
import hbec.intellitrade.conditionorder.domain.orders.grid.InflexionSubCondition;
import hbec.intellitrade.conditionorder.domain.tradeplan.EntrustStrategy;
import hbec.intellitrade.conditionorder.domain.tradeplan.OfferedPriceBidirectionalTradePlan;
import hbec.intellitrade.conditionorder.domain.tradeplan.TradeNumberByAmount;
import hbec.intellitrade.strategy.domain.condition.delayconfirm.DelayConfirmInfo;
import hbec.intellitrade.strategy.domain.condition.delayconfirm.DelayConfirmOption;
import hbec.intellitrade.strategy.domain.condition.deviation.DeviationCtrlInfo;
import hbec.intellitrade.strategy.domain.factor.BinaryFactorType;
import hbec.intellitrade.strategy.domain.factor.CompareOperator;
import hbec.intellitrade.strategy.domain.factor.PercentBinaryTargetPriceFactor;
import hbec.intellitrade.strategy.domain.shared.Week;
import hbec.intellitrade.strategy.domain.timerange.LocalTimeRange;
import hbec.intellitrade.strategy.domain.timerange.WeekRange;
import hbec.intellitrade.strategy.domain.timerange.WeekTimeRange;
import me.caosh.condition.interfaces.command.GridConditionCommandDTO;
import me.caosh.condition.interfaces.command.GridTradeOrderCreateCommand;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

/**
 * @author caosh/caoshuhao@touker.com
 * @date 2018/3/15
 */
public class GridTradeOrderCommandAssemblerTest {
    @Test
    public void test() throws Exception {
        GridTradeOrderCreateCommand createCommand = new GridTradeOrderCreateCommand();
        SecurityInfoDTO securityInfo = new SecurityInfoDTO();
        securityInfo.setType(4);
        securityInfo.setCode("600000");
        securityInfo.setName("浦发银行");
        securityInfo.setExchange("SH");
        createCommand.setSecurityInfo(securityInfo);

        GridConditionCommandDTO condition = new GridConditionCommandDTO();
        condition.setBasePrice(new BigDecimal("10.00"));
        condition.setBinaryFactorType(0);
        condition.setIncreasePercent(new BigDecimal("1.00"));
        condition.setFallPercent(new BigDecimal("-0.50"));
        condition.setDecreasePercent(new BigDecimal("-2.00"));
        condition.setReboundPercent(new BigDecimal("0.75"));
        condition.setUseGuaranteedPrice(true);
        createCommand.setCondition(condition);

        createCommand.setExpireTime(LocalDateTime.parse("2018-03-15T15:00:00").toDate());

        BidirectionalTradePlanDTO tradePlan = new BidirectionalTradePlanDTO();
        tradePlan.setBuyStrategy(4);
        tradePlan.setSellStrategy(9);
        tradePlan.setEntrustMethod(1);
        tradePlan.setEntrustAmount(new BigDecimal("10000.00"));
        createCommand.setTradePlan(tradePlan);

        MonitorTimeRangeDTO monitorTimeRange = new MonitorTimeRangeDTO();
        monitorTimeRange.setOption(1);
        monitorTimeRange.setBeginWeek(2);
        monitorTimeRange.setEndWeek(4);
        monitorTimeRange.setBeginTime("10:00:00");
        monitorTimeRange.setEndTime("14:00:00");
        createCommand.setMonitorTimeRange(monitorTimeRange);

        DelayConfirmDTO delayConfirmParam = new DelayConfirmDTO();
        delayConfirmParam.setOption(1);
        delayConfirmParam.setConfirmTimes(3);
        createCommand.setDelayConfirm(delayConfirmParam);

        DeviationCtrlDTO deviationCtrlParam = new DeviationCtrlDTO();
        deviationCtrlParam.setOption(1);
        deviationCtrlParam.setLimitPercent(new BigDecimal(1));
        createCommand.setDeviationCtrl(deviationCtrlParam);

        TradeCustomerInfo tradeCustomerInfo = new TradeCustomerInfo(303348, "010000061086");
        GridTradeOrder gridTradeOrder = GirdTradeOrderCommandAssembler.assemble(123L, tradeCustomerInfo, createCommand);

        BigDecimal basePrice = new BigDecimal("10.00");
        GridTradeOrder expected = new GridTradeOrder(123L,
                tradeCustomerInfo,
                OrderState.ACTIVE,
                new SecurityInfo(SecurityType.STOCK,
                        "600000",
                        SecurityExchange.SH,
                        "浦发银行"),
                new DecoratedGridCondition(basePrice,
                        BinaryFactorType.PERCENT,
                        new InflexionSubCondition(new PercentBinaryTargetPriceFactor(CompareOperator.LE,
                                new BigDecimal("-2.00")),
                                new PercentBinaryTargetPriceFactor(CompareOperator.GE, new BigDecimal("0.75")),
                                basePrice,
                                true),
                        new InflexionSubCondition(new PercentBinaryTargetPriceFactor(CompareOperator.GE,
                                new BigDecimal("1.00")),
                                new PercentBinaryTargetPriceFactor(CompareOperator.LE, new BigDecimal("-0.50")),
                                basePrice,
                                true),
                        true,
                        new DelayConfirmInfo(DelayConfirmOption.ACCUMULATE, 3),
                        new DeviationCtrlInfo(new BigDecimal("1")),
                        0,
                        0),
                LocalDateTime.parse("2018-03-15T15:00:00"),
                new OfferedPriceBidirectionalTradePlan(new TradeNumberByAmount(new BigDecimal("10000.00")),
                        EntrustStrategy.SELL3, EntrustStrategy.BUY3),
                new WeekTimeRange(new WeekRange(Week.TUE, Week.THU),
                        new LocalTimeRange(LocalTime.parse("10:00:00"), LocalTime.parse("14:00:00"))));

        assertEquals(gridTradeOrder, expected);
        assertEquals(gridTradeOrder.toString(), expected.toString());
    }
}
