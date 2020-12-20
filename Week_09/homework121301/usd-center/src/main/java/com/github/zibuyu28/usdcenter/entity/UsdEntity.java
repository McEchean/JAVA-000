package com.github.zibuyu28.usdcenter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsdEntity {

    private long id;

    private long userId;

    private BigDecimal usd;

    private BigDecimal freeze;
}
