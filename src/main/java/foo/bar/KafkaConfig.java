package foo.bar;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig
{
    @Value("${kafka.bootstrap.servers:localhost:9092}")
    private String bootstrapServers;

    public Map<String, Object> baseConfig()
    {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    ConsumerFactory<Key, Record> employeeMasterDataRecordConsumerFactory()
    {
        return new DefaultKafkaConsumerFactory<>(
            baseConfig(),
            new JsonDeserializer<>(Key.class),
            new JsonDeserializer<>(Record.class));
    }

    @Bean("cf")
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Key, Record>> kafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<Key, Record> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(employeeMasterDataRecordConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean("template")
    KafkaTemplate<Key, Record> kafkaTemplate()
    {
        JsonSerializer<Key> keySerializer = new JsonSerializer<>();
        // keySerializer.setAddTypeInfo(false); // (*)
        JsonSerializer<Record> valueSerializer = new JsonSerializer<>();
        // valueSerializer.setAddTypeInfo(false); // (*)

        return new KafkaTemplate<>(
            new DefaultKafkaProducerFactory<>(
                baseConfig(),
                keySerializer,
                valueSerializer));
    }
}
