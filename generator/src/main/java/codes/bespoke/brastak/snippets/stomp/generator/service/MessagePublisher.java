package codes.bespoke.brastak.snippets.stomp.generator.service;

import codes.bespoke.brastak.snippets.stomp.client.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(MessageDto message) {
        rabbitTemplate.convertAndSend("messages", message);
    }
}
