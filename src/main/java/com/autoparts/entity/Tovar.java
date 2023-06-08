package com.autoparts.entity;

import com.autoparts.entity.enums.StockStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tovar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String slug;
    private String sku;
    private Integer price;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tovar_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ProductImage> images;
    private int rating;
    @ManyToOne()
    @JoinColumn(name = "brand_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Brand brand;
    private int reviews;
    private String availability;
    private String excerpt;
    @Column(length = 50000)
    private String description;
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Category category;
    private String partNumber;
    private Long   compareAtPrice;
    private String compatibility;
    private String tags;
    @Enumerated(EnumType.STRING)
    private StockStatus stock;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "sales_count")
    private int salesCount;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tovar")
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ProductAttribute> attributes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_type_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ProductType type;
    private String carBrand;
}
