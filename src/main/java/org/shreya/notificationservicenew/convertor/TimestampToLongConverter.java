package org.shreya.notificationservicenew.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@WritingConverter
@Component
public class TimestampToLongConverter implements Converter<Timestamp, Long> {
    @Override
    public Long convert(Timestamp source) {
        return source.getTime();
    }
}