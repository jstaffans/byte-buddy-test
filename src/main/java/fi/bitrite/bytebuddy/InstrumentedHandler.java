package fi.bitrite.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class InstrumentedHandler {

    public static void handleRequest(@SuperCall Callable<Void> zuper) throws Exception {
        System.out.println("Handling request (subclass)");

        try {
            zuper.call();
        } finally {
            System.out.println("Handled request");
        }
    }
}
