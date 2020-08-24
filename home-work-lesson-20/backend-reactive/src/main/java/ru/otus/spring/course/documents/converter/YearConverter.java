package ru.otus.spring.course.documents.converter;

import com.mongodb.lang.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.Year;
import java.util.Arrays;

@Configuration
public class YearConverter {

    @Bean
    public MongoCustomConversions customConversions(){
        return new MongoCustomConversions(Arrays.asList(
                new IntToYearConverter(),
                new YearToIntConverter()
        ));
    }

    @ReadingConverter
    private static class IntToYearConverter implements Converter<Integer, Year>{

        @Override
        public Year convert(@NonNull Integer integer) {
            return Year.of(integer);
        }
    }

    @WritingConverter
    private static class YearToIntConverter implements Converter<Year, Integer> {
        @Override
        public Integer convert(Year year) {
            return year.getValue();
        }
    }

}
