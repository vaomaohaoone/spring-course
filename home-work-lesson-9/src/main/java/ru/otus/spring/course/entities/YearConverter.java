package ru.otus.spring.course.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Year;

@Converter
public class YearConverter implements AttributeConverter<Year, String> {
    @Override
    public String convertToDatabaseColumn(Year year) {
        return year.toString();
    }

    @Override
    public Year convertToEntityAttribute(String s) {
        return Year.of(Integer.parseInt(s));
    }
}
