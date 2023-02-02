package org.sample.api;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@ToString
@Builder
public class Order {

    private int id;
    private String name;
    private int price;
}
