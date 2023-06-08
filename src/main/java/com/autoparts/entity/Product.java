package com.autoparts.entity;
import javax.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@ToString(exclude = "category")
@EqualsAndHashCode(of = "articleNo")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String articleNo;

    private String name;

    private int count;

    private BigDecimal price;

    private String carName;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
