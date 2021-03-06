package io.confluent.developer.spring;

import com.github.javafaker.Faker;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

import io.confluent.developer.avro.Hobbit;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@SpringBootApplication
//@EnableKafkaStreams
public class SpringCcloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCcloudApplication.class, args);
	}

	@Bean
	NewTopic hobbitAvro() {
		return TopicBuilder.name("hobbit-arvo").partitions(12).replicas(3).build();
	}

	@Bean
	NewTopic counts() {
		return TopicBuilder.name("streams-wordcount-output1").partitions(6).replicas(3).build();
	}

}

@RequiredArgsConstructor
@Component
class Producer {

	private final KafkaTemplate<Integer, Hobbit> template;

	Faker faker;

	@EventListener(ApplicationStartedEvent.class)
	public void generate() {

		faker = Faker.instance();
		final Flux<Long> interval = Flux.interval(Duration.ofMillis(1_000));

		final Flux<String> quotes = Flux.fromStream(Stream.generate(() -> faker.hobbit().quote()));

//		Flux.zip(interval, quotes)
//			 	.map(it -> template.send("hobbit", faker.random().nextInt(42), it.getT2())).blockLast();
		Flux.zip(interval, quotes)
			 	.map(it -> template.send("hobbit-arvo", faker.random()
						.nextInt(42), new Hobbit(it.getT2()))).blockLast();
	}
}

/*@Component
class Consumer {
	@KafkaListener(topics= {"hobbit"}, groupId="spring-boot-kafka")
//	public void consume(String quote) {
	public void consume(ConsumerRecord<Integer, String> record) {
//		System.out.println("received= " + quote);
		System.out.println("received= " + record.value() + "with key" + record.key());
	}
}*/

@Component
class Consumer {
	@KafkaListener(topics= {"hobbit-arvo"}, groupId="spring-boot-kafka")
//	public void consume(String quote) {
	public void consume(ConsumerRecord<Integer, Hobbit> record) {
//		System.out.println("received= " + quote);
		System.out.println("received= " + record.value() + "with key" + record.key());
	}
}

/*

//TODO https://developer.confluent.io/learn-kafka/kafka-streams/get-started/
@Component
class Processor {
	@Autowired
	public void process(StreamsBuilder builder) {
		final Serde<Integer> integerSerde = Serdes.Integer();
		final Serde<String> stringSerde = Serdes.String();
		final Serde<Long> longSerde = Serdes.Long();

		KStream<Integer, String> textLines = builder.stream("hobbit", Consumed.with(integerSerde, stringSerde));

		KTable<String, Long> wordCounts = textLines
				.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
				.groupBy((key, value) -> value, Grouped.with(stringSerde, stringSerde))
				.count();

		wordCounts.toStream().to("streams-wordcount-output1", Produced.with(stringSerde, longSerde));
	}
}*/
