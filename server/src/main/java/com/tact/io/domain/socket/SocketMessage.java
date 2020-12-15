package com.tact.io.domain.socket;

public class SocketMessage {
    private HeaderType header;
    private SocketBody body;

    public SocketMessage(HeaderType header, SocketBody body) {
        this.header = header;
        this.body = body;
    }

    public HeaderType getHeader() {
        return header;
    }

    public SocketBody getBody() {
        return body;
    }
}
