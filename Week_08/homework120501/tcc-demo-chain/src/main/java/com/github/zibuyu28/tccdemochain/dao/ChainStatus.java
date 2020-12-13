package com.github.zibuyu28.tccdemochain.dao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChainStatus {

    private long id;

    private int state;

    private String message;
}
