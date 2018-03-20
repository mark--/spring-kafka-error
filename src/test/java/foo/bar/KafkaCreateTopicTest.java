package foo.bar;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EmbeddedKafka(count = 1, topics = { "topic.foo" })
@SpringBootTest(properties = "kafka.bootstrap.servers=${spring.embedded.kafka.brokers}")
public class KafkaCreateTopicTest
{
    @Autowired
    private KafkaTemplate<Key, Record> template1;

    @Test
    public void successfullyReceivedNewEmployeeMasterDataRecordForNewEmployee() throws Exception
    {
        template1.send("topic.foo", new Key("123"), new Record("foofoo"));

        // Give the listener some time to be called, increase if needed, e.g. for debugging
        Thread.sleep(100000);

        fail("Look at the console log");
    }
}