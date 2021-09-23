package io.confluent.developer.spring;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.time.Duration;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

/*@Configuration
@EnableKafka
@EnableKafkaStreams*/
public class KafkaStreamsConfig {
/*
  @Bean(name =
      KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  public KafkaStreamsConfiguration kStreamsConfigs() {
    return new KafkaStreamsConfiguration(Map.of(
        APPLICATION_ID_CONFIG, "testStreams",
        BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
        DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName(),
        DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
        DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName()
    ));
  }

  @Bean
  public KStream<Integer, String> kStream (StreamsBuilder kStreamBuilder) {
    KStream<Integer, String> stream = kStreamBuilder.stream("hobbit");
    stream
        .mapValues((ValueMapper<String, String>) String::toUpperCase)
        .groupByKey()
        .windowedBy(TimeWindows.of(Duration.ofMillis(1000)))
        .reduce((String value1, String value2) -> value1 + value2, Named.as("windowStore"))
        .toStream()
        .map((windowedId, value) -> new KeyValue<>(windowedId.key(), value))
        .filter((i, s) -> s.length() > 40)
        .to("streams-wordcount-output1");
    stream.print(Printed.toSysOut());
    return stream;
  }*/
}