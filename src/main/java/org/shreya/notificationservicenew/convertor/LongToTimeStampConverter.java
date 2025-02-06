package org.shreya.notificationservicenew.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@ReadingConverter
@Component
public class LongToTimeStampConverter implements Converter<Long, Timestamp> {
    @Override
    public Timestamp convert(Long source) {
        return new Timestamp(source);
    }
}