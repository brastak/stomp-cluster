package codes.bespoke.brastak.snippets.stomp.generator.service;

import codes.bespoke.brastak.snippets.stomp.client.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterClientHandler {
    private final MessagePublisher messagePublisher;

    @Value("${application.messages.count:1}")
    private int messagesCount;

    @Value("${application.batch.size:1}")
    private int batchSize;

    @Value("${application.publisher.initialDelay:PT0S}")
    private Duration initialDelay;
    @Value("${application.publisher.delay:PT0S}")
    private Duration delay;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(16);

    @RabbitListener(queues = "subscriptions")
    public void listen(String username) {
        log.debug("Subscribe {}", username);
        executor.schedule(() -> publish(username, 0), initialDelay.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void publish(String username, int messageNum) {
        int messagesToSend;
        if(delay.toMillis() == 0) {
            messagesToSend = messagesCount - messageNum;
        } else {
            messagesToSend = Math.min(this.batchSize, messagesCount - messageNum);
        }

        for(int i = 0; i < messagesToSend; i++) {
            messagePublisher.publish(new MessageDto(username, OffsetDateTime.now(), "message " + (messageNum + i)));
        }
        if(messageNum + messagesToSend < messagesCount) {
            executor.schedule(() -> publish(username, messageNum + messagesToSend), delay.toMillis(), TimeUnit.MILLISECONDS);
        }
    }
}
