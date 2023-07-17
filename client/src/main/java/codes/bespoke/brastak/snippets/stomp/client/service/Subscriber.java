package codes.bespoke.brastak.snippets.stomp.client.service;

import codes.bespoke.brastak.snippets.stomp.client.dto.MessageDto;
import codes.bespoke.brastak.snippets.stomp.client.model.Message;
import codes.bespoke.brastak.snippets.stomp.client.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class Subscriber {
    private final WebSocketStompClient stompClient;
    private final MessageRepository repository;
    private final ObjectMapper mapper;
    private final String url;
    private final String queue;
    private final String username;

    public CompletableFuture<StompSession.Subscription> subscribe() {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Username", username);

        return stompClient.connectAsync(url, headers, new StompSessionHandlerAdapter() {}, new Object[0])
                .thenApply(session -> session.subscribe(queue, new MessageHandler(repository, mapper, username)));
    }

    @RequiredArgsConstructor
    private static class MessageHandler extends StompSessionHandlerAdapter {
        private final MessageRepository repository;
        private final ObjectMapper mapper;
        private final String username;

        @Override
        @SneakyThrows
        public void handleFrame(StompHeaders headers, Object payload) {
            log.debug("{} handles '{}'", username, payload);
            MessageDto dto = mapper.readValue(String.valueOf(payload), MessageDto.class);

            Message message = new Message(
                    username,
                    dto.createdAt(),
                    dto.sentAt(),
                    dto.sentFrom(),
                    dto.text()
            );
            repository.save(message);
        }
    }
}
