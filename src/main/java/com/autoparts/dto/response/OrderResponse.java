package com.autoparts.dto.response;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class OrderResponse<T> {

    List<T> items;
    int page;
    int limit;
    int total;
    int pages;
    int from;
    int to;
    public static <T> OrderResponse<T> of(Page<T> pageResponse) {

        int page = pageResponse.getNumber() + 1;
        int limit = 16;
        int total = (int) pageResponse.getTotalElements();
        int pages = pageResponse.getTotalPages();
        int from = (page - 1) * limit;
        int to = Math.min(from + limit, total);






        return new OrderResponse<>(pageResponse.getContent(), page, limit, total, pages, from, to);

    }


}
