package org.shreya.notificationservicenew.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.shreya.notificationservicenew.convertor.LongToTimeStampConverter;
import org.shreya.notificationservicenew.convertor.TimestampToLongConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.util.Arrays;

@Configuration
public class ESConfig {
    @Bean
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(Arrays.asList(
                new TimestampToLongConverter(),
                new LongToTimeStampConverter()
        ));
    }

}