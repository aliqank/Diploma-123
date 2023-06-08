package com.autoparts.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@NamedEntityGraph(name = "AttributeGroup.attributes",
        attributeNodes = @NamedAttributeNode("attributes"))
public class AttributeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slug;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_group_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ProductAttribute> attributes;
}
