package com.autoparts.dto.review;

import com.autoparts.entity.Tovar;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

public class ReviewCreateDto {


    private String author;
    private int rating;
    private String content;
}
