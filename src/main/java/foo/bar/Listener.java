package foo.bar;

import java.net.URISyntaxException;
import java.text.ParseException;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@EnableKafka
@Configuration
public class Listener
{
    @KafkaListener(groupId = "group", topics = "topic.foo", containerFactory = "cf")
    public void verify(@Payload Record record, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Key key)
        throws ParseException, URISyntaxException
    {
        System.out.println("Never reached");
    }
}
