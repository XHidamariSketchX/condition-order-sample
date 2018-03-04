package me.caosh.condition.domain.model.order;

import me.caosh.condition.domain.model.trade.EntrustCommand;
import me.caosh.condition.domain.model.trade.EntrustResult;

/**
 * 交易系统适配器
 *
 * @author caosh/caoshuhao@touker.com
 * @date 2018/3/2
 */
public interface TradeSystemAdapter {
    /**
     * 执行证券委托
     *
     * @param customerNo     客户号
     * @param entrustCommand 委托命令
     * @return 委托结果
     */
    EntrustResult entrust(String customerNo, EntrustCommand entrustCommand);
}
