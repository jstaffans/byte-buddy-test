package fi.bitrite.bytebuddy;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class ConstantStringHandler implements HttpHandler {

    private final String value;

    public ConstantStringHandler() {
        this("Foobar");
    }

    public ConstantStringHandler(String value) {
        this.value = value;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        httpServerExchange.getResponseSender().send(value + "\n");
    }
}
