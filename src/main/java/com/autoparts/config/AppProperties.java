package com.autoparts.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@ConfigurationProperties(prefix = "application")
public class AppProperties implements Validator {

    private static final String FIELD_REQUIRED = "field.required";

    private String appVersion;
    private String appDescription;
    private String jwtSecret;
    private Integer jwtExpirationMin;
    private String fileStorePath;

    @Override
    public boolean supports(Class<?> aClass) {
        return AppProperties.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jwtSecret", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jwtExpirationMin", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fileStorePath", FIELD_REQUIRED);
    }
}
