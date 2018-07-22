package fi.bitrite.bytebuddy;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.implementation.bind.annotation.Pipe;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        ByteBuddy byteBuddy = new ByteBuddy();

        // Method 1: intercept method by subclassing
        HttpHandler handler1 = byteBuddy
                .subclass(ConstantStringHandler.class)
                .method(named("handleRequest")).intercept(MethodDelegation.to(InstrumentedHandler.class))
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        // Method 2: enhance already existing instance
        HttpHandler handler2 = new ConstantStringHandler("Handler 2");

        HttpHandler instrumentedHandler2 = byteBuddy
                .subclass(ConstantStringHandler.class)
                .method(named("handleRequest")).intercept(MethodDelegation.withDefaultConfiguration()
                .withBinders(Pipe.Binder.install(Forwarder.class)).to(new ForwardingInstrumentationInterceptor(handler2)))
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        // Misc: stub method (suppress call)
        HttpHandler handler3 = new ConstantStringHandler("Handler 3");
        HttpHandler stubbedHandler = byteBuddy
                .subclass(ConstantStringHandler.class)
                .method(named("handleRequest")).intercept(StubMethod.INSTANCE)
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new RoutingHandler()
                        .get("/test1", handler1)
                        .get("/test2", instrumentedHandler2)
                        .get("/test3", stubbedHandler))
                .build();

        server.start();
    }
}

