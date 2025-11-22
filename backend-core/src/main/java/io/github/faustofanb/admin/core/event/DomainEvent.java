package io.github.faustofanb.admin.core.event;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class DomainEvent implements Serializable {
    private String eventId;
    private LocalDateTime occurredOn;
    private Long tenantId;
    private String source;

    public DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
    }
}
