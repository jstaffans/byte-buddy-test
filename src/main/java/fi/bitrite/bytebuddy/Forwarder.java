package fi.bitrite.bytebuddy;

public interface Forwarder<T, S> {

    T to(S target);

}
