package com.example.tp3.dto;

import lombok.*;
import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderRequest {
    private List<OrderItem>  orderItems;
}