package codes.bespoke.brastak.snippets.stomp.client.service;

import codes.bespoke.brastak.snippets.stomp.client.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class Initializer {
    private final WebSocketStompClient client;
    private final MessageRepository repository;
    private final ObjectMapper mapper;
    private final List<CompletableFuture<StompSession.Subscription>> subscriptions = new ArrayList<>();

    @Value("${application.clients.count:1}")
    private int clientsCount;
    @Value("${application.clients.delay:PT0.1S}")
    private Duration clientsSubscriptionDelay;
    @Value("${application.backend.url:ws://localhost:8080/ws}")
    private String url;
    @Value("${application.backend.queue:/user/topic/messages}")
    private String queue;


    public Initializer(MessageRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;

        SockJsClient sockJsClient = new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        );
        this.client = new WebSocketStompClient(sockJsClient);
        this.client.setMessageConverter(new StringMessageConverter());
    }

    public void startClients() {
        client.start();
        for(int i = 0; i < clientsCount; i++) {
            Subscriber subscriber = new Subscriber(client, repository, mapper, url, queue, "client-" + i);
            subscriptions.add(subscriber.subscribe());
            try {
                Thread.sleep(clientsSubscriptionDelay.toMillis());
            } catch (InterruptedException e) {
            }
        }
    }

    public void stopClients() {
        for(CompletableFuture<StompSession.Subscription> subscription: subscriptions) {
            subscription.join().unsubscribe();
        }
        client.stop();
    }
}
