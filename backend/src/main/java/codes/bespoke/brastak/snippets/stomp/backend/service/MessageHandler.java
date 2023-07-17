package codes.bespoke.brastak.snippets.stomp.backend.service;

import codes.bespoke.brastak.snippets.stomp.client.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;

    @Value("${HOSTNAME}")
    private String hostname;

    @RabbitListener(queues = "messages")
    public void listen(MessageDto message) throws JsonProcessingException {
        log.debug("Handle {}", message);
        var enriched = message.sent(OffsetDateTime.now(), hostname);
        template.convertAndSendToUser(message.receiver(), "/topic/messages", mapper.writeValueAsString(enriched));
    }
}
