package org.eventmanagement.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {


    @Override
    public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(new DateToStringConverter());
        }


    class DateToStringConverter implements Converter<Date, String> {

        @Override
        public String convert(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format(date);
        }
    }
}
