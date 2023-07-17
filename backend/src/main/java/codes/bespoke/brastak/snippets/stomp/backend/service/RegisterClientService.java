package codes.bespoke.brastak.snippets.stomp.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegisterClientService {
    private final RabbitTemplate rabbitTemplate;

    public RegisterClientService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void registerClient(String username) {
        rabbitTemplate.convertAndSend("subscriptions", username);
    }
}
