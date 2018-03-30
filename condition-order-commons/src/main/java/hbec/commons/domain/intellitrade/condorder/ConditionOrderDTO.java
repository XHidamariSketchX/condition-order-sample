package hbec.commons.domain.intellitrade.condorder;

import com.google.common.base.MoreObjects;
import hbec.commons.domain.intellitrade.condition.ConditionDTO;
import me.caosh.autoasm.Convertible;
import me.caosh.autoasm.FieldMapping;
import hbec.commons.domain.intellitrade.security.SecurityInfoDTO;
import hbec.commons.domain.intellitrade.market.TrackedIndexDTO;

import java.io.Serializable;

/**
 * Created by caosh on 2017/8/11.
 *
 * @author caoshuhao@touker.com
 */
@Convertible
public class ConditionOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private TradeCustomerInfoDTO customer;
    private Boolean deleted = false;
    private Integer orderState;
    private Integer dataVersion;
    private SecurityInfoDTO securityInfo;
    private TrackedIndexDTO trackedIndex;
    @FieldMapping(mappedProperty = "strategyInfo.strategyType")
    private Integer strategyType;
    private ConditionDTO condition;
    private String expireTime;
    private TradePlanDTO tradePlan;
    private MonitorTimeRangeDTO monitorTimeRange;
    @FieldMapping(mappedProperty = "condition.delayConfirm")
    private DelayConfirmDTO delayConfirm;
    @FieldMapping(mappedProperty = "condition.deviationCtrl")
    private DeviationCtrlDTO deviationCtrl;
    private Integer triggerCount;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public TradeCustomerInfoDTO getCustomer() {
        return customer;
    }

    public void setCustomer(TradeCustomerInfoDTO customer) {
        this.customer = customer;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public SecurityInfoDTO getSecurityInfo() {
        return securityInfo;
    }

    public void setSecurityInfo(SecurityInfoDTO securityInfo) {
        this.securityInfo = securityInfo;
    }

    public TrackedIndexDTO getTrackedIndex() {
        return trackedIndex;
    }

    public void setTrackedIndex(TrackedIndexDTO trackedIndex) {
        this.trackedIndex = trackedIndex;
    }

    public Integer getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(Integer strategyType) {
        this.strategyType = strategyType;
    }

    public ConditionDTO getCondition() {
        return condition;
    }

    public void setCondition(ConditionDTO condition) {
        this.condition = condition;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public TradePlanDTO getTradePlan() {
        return tradePlan;
    }

    public void setTradePlan(TradePlanDTO tradePlan) {
        this.tradePlan = tradePlan;
    }

    public MonitorTimeRangeDTO getMonitorTimeRange() {
        return monitorTimeRange;
    }

    public void setMonitorTimeRange(MonitorTimeRangeDTO monitorTimeRange) {
        this.monitorTimeRange = monitorTimeRange;
    }

    public DelayConfirmDTO getDelayConfirm() {
        return delayConfirm;
    }

    public void setDelayConfirm(DelayConfirmDTO delayConfirm) {
        this.delayConfirm = delayConfirm;
    }

    public DeviationCtrlDTO getDeviationCtrl() {
        return deviationCtrl;
    }

    public void setDeviationCtrl(DeviationCtrlDTO deviationCtrl) {
        this.deviationCtrl = deviationCtrl;
    }

    public Integer getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(Integer triggerCount) {
        this.triggerCount = triggerCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ConditionOrderDTO.class).omitNullValues()
                          .add("orderId", orderId)
                          .add("customer", customer)
                          .add("deleted", deleted)
                          .add("orderState", orderState)
                          .add("dataVersion", dataVersion)
                          .add("securityInfo", securityInfo)
                          .add("trackedIndex", trackedIndex)
                          .add("strategyType", strategyType)
                          .add("condition", condition)
                          .add("expireTime", expireTime)
                          .add("tradePlan", tradePlan)
                          .add("monitorTimeRange", monitorTimeRange)
                          .add("delayConfirm", delayConfirm)
                          .add("deviationCtrl", deviationCtrl)
                          .add("triggerCount", triggerCount)
                          .toString();
    }
}
