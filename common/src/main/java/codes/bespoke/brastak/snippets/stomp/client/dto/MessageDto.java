package codes.bespoke.brastak.snippets.stomp.client.dto;

import java.time.OffsetDateTime;

public record MessageDto(String receiver, OffsetDateTime createdAt, OffsetDateTime sentAt, String sentFrom, String text) {
    public MessageDto(String receiver, OffsetDateTime createdAt, String text) {
        this(receiver, createdAt, null, null, text);
    }

    public MessageDto sent(OffsetDateTime sentAt, String sentFrom) {
        return new MessageDto(receiver, createdAt, sentAt, sentFrom, text);
    }
}
