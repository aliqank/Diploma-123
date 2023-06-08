package com.autoparts.dto.review;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class ReviewResponse<T> {

    List<T> items;
    int page;
    int limit;
    int total;
    int pages;
    int from;
    int to;

    public static <T> ReviewResponse<T> of(Page<T> pageResponse) {

        int to = (pageResponse.getNumber() + 1) * pageResponse.getSize();
        int from = (pageResponse.getNumber()) * pageResponse.getSize();

        int page = pageResponse.getNumber() + 1;
        int limit = pageResponse.getSize();
        int total = (int) pageResponse.getTotalElements();
        int pages = pageResponse.getTotalPages();

        return new ReviewResponse<>(pageResponse.getContent(),page, limit, total, pages, from, to);

    }


}
