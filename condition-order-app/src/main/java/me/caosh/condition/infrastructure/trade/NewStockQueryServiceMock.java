package me.caosh.condition.infrastructure.trade;

import com.google.common.collect.ImmutableList;
import me.caosh.condition.domain.model.constants.SecurityExchange;
import me.caosh.condition.domain.model.newstock.NewStock;
import me.caosh.condition.domain.service.NewStockQueryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by caosh on 2017/9/4.
 *
 * @author caoshuhao@touker.com
 */
@Service
public class NewStockQueryServiceMock implements NewStockQueryService {
    @Override
    public List<NewStock> getCurrentPurchasable() {
        return ImmutableList.of(new NewStock("732386", "骏亚申购", SecurityExchange.SH,
                        new BigDecimal("6.23"), 20000),
                new NewStock("300700", "岱勒新材", SecurityExchange.SZ,
                        new BigDecimal("10.49"), 8000));
    }
}
