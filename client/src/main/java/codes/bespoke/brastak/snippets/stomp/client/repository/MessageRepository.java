package codes.bespoke.brastak.snippets.stomp.client.repository;

import codes.bespoke.brastak.snippets.stomp.client.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
