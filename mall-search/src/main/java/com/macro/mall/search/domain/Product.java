package com.macro.mall.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Caosen
 * @Date 2022/9/18 15:04
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String productName;
    private double price;
    private int number;
    public String geIndexId() {
        int id = 1;
        id += id;
        String indesId = String.valueOf(id);
        return indesId;
    }
}
