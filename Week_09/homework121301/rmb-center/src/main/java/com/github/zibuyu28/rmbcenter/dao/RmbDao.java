package com.github.zibuyu28.rmbcenter.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RmbDao {
    private long id;

    private long userId;

    private BigDecimal rmb;

    private BigDecimal freeze;
}
