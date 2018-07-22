package fi.bitrite.bytebuddy;

import io.undertow.server.HttpHandler;
import net.bytebuddy.implementation.bind.annotation.Pipe;

public class ForwardingInstrumentationInterceptor {

    private final HttpHandler target;

    public ForwardingInstrumentationInterceptor(HttpHandler target) {
        this.target = target;
    }

    public void hanndleRequest(@Pipe Forwarder<Void, HttpHandler> pipe) {
        System.out.println("Handling request (forwarding)");

        try {
            pipe.to(target);
        } finally {
            System.out.println("Handled request");
        }
    }
}
