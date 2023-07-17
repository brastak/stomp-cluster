package codes.bespoke.brastak.snippets.stomp.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionListener implements ApplicationListener<SessionSubscribeEvent>, Ordered {
    private final RegisterClientService registerClientService;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        log.debug("Subscribe {} user", event.getUser().getName());
        registerClientService.registerClient(event.getUser().getName());
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
