package org.bookmc.transformie;

public interface Transformer {
    byte[] transform(String name, byte[] classBytes);
}
