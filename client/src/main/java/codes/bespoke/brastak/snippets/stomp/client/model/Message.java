package codes.bespoke.brastak.snippets.stomp.client.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String receiver;
    private OffsetDateTime createdAt;
    private OffsetDateTime sentAt;
    private String sentFrom;
    private OffsetDateTime receivedAt;
    private String text;

    public Message() {
    }

    public Message(String receiver, OffsetDateTime createdAt, OffsetDateTime sentAt, String sentFrom, String text) {
        this.receiver = receiver;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
        this.sentFrom = sentFrom;
        this.receivedAt = OffsetDateTime.now();
        this.text = text;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other == null) return false;
        if(other.getClass() != Message.class) return false;

        Message message = (Message) other;
        if(message.id == null) return false;
        return Objects.equals(id, message.id);
    }
}
