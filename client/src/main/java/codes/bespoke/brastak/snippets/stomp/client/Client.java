package codes.bespoke.brastak.snippets.stomp.client;

import codes.bespoke.brastak.snippets.stomp.client.service.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Duration;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
public class Client {
    public static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Bean
    public CommandLineRunner runner(Initializer initializer,
                                    @Value("${application.duration:PT5M}") Duration duration) {
        return args -> {
            initializer.startClients();
            Thread.sleep(duration.toMillis());
            initializer.stopClients();
        };
    }
}