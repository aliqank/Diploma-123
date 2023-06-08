package com.autoparts.bucket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    PRODUCT_IMAGE("autoparts-product-image");

    private final String bucketName;

}
