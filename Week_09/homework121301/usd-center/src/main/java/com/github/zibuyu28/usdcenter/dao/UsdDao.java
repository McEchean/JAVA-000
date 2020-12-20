package com.github.zibuyu28.usdcenter.dao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsdDao {
    private long id;

    private long userId;

    private BigDecimal usd;

    private BigDecimal freeze;
}
