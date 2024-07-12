package cz.scholz.sandbox.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class AdminApiClientAsyncTest {
    @Test
    @Timeout(value = 60)
    public void testFreecode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Admin admin = adminClient();

        admin.listTopics().names()
                .whenComplete((topics, error) -> {
                    if (error != null) {
                        System.out.println("Failed: " + error);
                        Assertions.fail(error);
                    } else {
                        System.out.println("Succeeded: " + topics);

                        admin.describeTopics(topics).allTopicNames()
                                .whenComplete((topicNames, error2) -> {
                                    if (error2 != null) {
                                        System.out.println("Failed2: " + error2);
                                        Assertions.fail(error2);
                                    } else {
                                        System.out.println("Succeeded2: " + topicNames);
                                        latch.countDown();
                                    }
                                });
                    }
                });

        latch.await();
        admin.close();
    }

    Admin adminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.73:32298");

        return AdminClient.create(props);
    }
}
