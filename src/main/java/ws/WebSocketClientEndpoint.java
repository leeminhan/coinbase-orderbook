package ws;

import org.apache.logging.log4j.LogManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;


@ClientEndpoint
public class WebSocketClientEndpoint {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(WebSocketClientEndpoint.class);

    private Session session;
    private MessageHandler messageHandler;

    public WebSocketClientEndpoint(URI uri) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        LOG.info("Opening WebSocket Connection");
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        this.session = null;
        LOG.info("Closed WebSocket Session: {}", reason.toString());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        LOG.error("Session: {}; Exception: {}", session, e.getMessage());
    }

    public void addMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public void close() {
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                LOG.error(e);
            }
        }
    }
}
