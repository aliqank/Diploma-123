package com.autoparts.dto.category;

import com.autoparts.entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategorySubDto {

    Long id;
    String type;
    String name;
    String slug;
    String image;
    CategoryReadDto parent;
    String layout;
}
