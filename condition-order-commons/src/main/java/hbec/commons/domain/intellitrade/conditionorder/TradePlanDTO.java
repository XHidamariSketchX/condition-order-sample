package hbec.commons.domain.intellitrade.conditionorder;

import com.google.common.base.MoreObjects;
import me.caosh.autoasm.Convertible;
import me.caosh.autoasm.FieldMapping;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by caosh on 2017/8/11.
 *
 * @author caoshuhao@touker.com
 */
@Convertible
public class TradePlanDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 股东号
     */
    private String stockHolderNo;
    @NotNull
    @Range(min = 1, max = 2)
    private Integer exchangeType;
    @NotNull
    @Range(min = 1, max = 11)
    private Integer entrustStrategy;
    private BigDecimal entrustPrice;
    @NotNull
    @Range(min = 0, max = 1)
    @FieldMapping(mappedProperty = "tradeNumber.entrustMethod")
    private Integer entrustMethod;
    @FieldMapping(mappedProperty = "tradeNumber.number")
    private Integer entrustNumber;
    @FieldMapping(mappedProperty = "tradeNumber.amount")
    private BigDecimal entrustAmount;
    private Integer orderType;
    /**
     * 站点信息
     */
    @NotBlank
    private String nodeInfo;

    public TradePlanDTO() {
    }

    public TradePlanDTO(String stockHolderNo,
                        Integer exchangeType,
                        Integer entrustStrategy,
                        BigDecimal entrustPrice,
                        Integer entrustMethod,
                        Integer entrustNumber,
                        BigDecimal entrustAmount,
                        Integer orderType,
                        String nodeInfo) {
        this.stockHolderNo = stockHolderNo;
        this.exchangeType = exchangeType;
        this.entrustStrategy = entrustStrategy;
        this.entrustPrice = entrustPrice;
        this.entrustMethod = entrustMethod;
        this.entrustNumber = entrustNumber;
        this.entrustAmount = entrustAmount;
        this.orderType = orderType;
        this.nodeInfo = nodeInfo;
    }

    public String getStockHolderNo() {
        return stockHolderNo;
    }

    public void setStockHolderNo(String stockHolderNo) {
        this.stockHolderNo = stockHolderNo;
    }

    public Integer getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(Integer exchangeType) {
        this.exchangeType = exchangeType;
    }

    public Integer getEntrustStrategy() {
        return entrustStrategy;
    }

    public void setEntrustStrategy(Integer entrustStrategy) {
        this.entrustStrategy = entrustStrategy;
    }

    public BigDecimal getEntrustPrice() {
        return entrustPrice;
    }

    public void setEntrustPrice(BigDecimal entrustPrice) {
        this.entrustPrice = entrustPrice;
    }

    public Integer getEntrustMethod() {
        return entrustMethod;
    }

    public void setEntrustMethod(Integer entrustMethod) {
        this.entrustMethod = entrustMethod;
    }

    public Integer getEntrustNumber() {
        return entrustNumber;
    }

    public void setEntrustNumber(Integer entrustNumber) {
        this.entrustNumber = entrustNumber;
    }

    public BigDecimal getEntrustAmount() {
        return entrustAmount;
    }

    public void setEntrustAmount(BigDecimal entrustAmount) {
        this.entrustAmount = entrustAmount;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(String nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(TradePlanDTO.class).omitNullValues()
                          .add("stockHolderNo", stockHolderNo)
                          .add("exchangeType", exchangeType)
                          .add("entrustStrategy", entrustStrategy)
                          .add("entrustPrice", entrustPrice)
                          .add("entrustMethod", entrustMethod)
                          .add("entrustNumber", entrustNumber)
                          .add("entrustAmount", entrustAmount)
                          .add("orderType", orderType)
                          .add("nodeInfo", nodeInfo)
                          .toString();
    }
}
