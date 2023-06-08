package com.autoparts.dto;

import com.autoparts.entity.ProductImage;

import java.util.Set;

public class TovarNameImages
{
    private String name;
    private Set<ProductImage> images;

    public TovarNameImages(String name, Set<ProductImage> images) {
        this.name = name;
        this.images = images;
    }
}
