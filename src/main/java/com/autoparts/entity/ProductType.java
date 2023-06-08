package com.autoparts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slug;

    private String name;

    @OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id")
    private Set<AttributeGroup> attributeGroups;
}
