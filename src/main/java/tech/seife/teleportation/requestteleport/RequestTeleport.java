package tech.seife.teleportation.requestteleport;

import java.time.LocalDateTime;
import java.util.UUID;

public final class RequestTeleport {

    private final UUID requestSenderUuid;
    private final UUID requestReceiverUuid;

    private final RequestType requestType;

    private final LocalDateTime tpaExpirationDate;

    public RequestTeleport(UUID requestSenderUuid, UUID requestReceiverUuid, RequestType requestType, LocalDateTime tpaExpirationDate) {
        this.requestSenderUuid = requestSenderUuid;
        this.requestReceiverUuid = requestReceiverUuid;
        this.requestType = requestType;
        this.tpaExpirationDate = tpaExpirationDate;
    }

    public UUID getRequestSenderUuid() {
        return requestSenderUuid;
    }

    public UUID getRequestReceiverUuid() {
        return requestReceiverUuid;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public LocalDateTime getTpaExpirationDate() {
        return tpaExpirationDate;
    }

    public enum RequestType {
        TELEPORT_HERE,
        TELEPORT_THERE
    }
}
