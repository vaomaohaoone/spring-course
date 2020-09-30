package ru.otus.spring.course.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "books")
@Data
@Accessors(chain = true)
public class BookDocument {
    @Id
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "publishedYear")
    @JsonSerialize(converter = JacksonYearToIntConverter.class)
    @JsonDeserialize(converter = JacksonIntToYearConverter.class)
    private Year publishedYear;
    @Field(name = "authors")
    @JsonIgnoreProperties("books")
    @DBRef(lazy = true)
    private Set<AuthorDocument> authors = new HashSet<>();

    private static class JacksonYearToIntConverter extends StdConverter<Year, Integer> {
        @Override
        public Integer convert(Year year) {
            return year.getValue();
        }
    }

    private static class JacksonIntToYearConverter extends StdConverter<Integer, Year> {
        @Override
        public Year convert(Integer integer) {
            return Year.of(integer);
        }
    }
}