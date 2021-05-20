package org.bookmc.transformie.classloader;

import org.bookmc.transformie.Transformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransformieClassLoader extends ClassLoader {
    private static final List<Transformer> transformers = new ArrayList<>();

    private static final List<String> exclusions = new ArrayList<>(Arrays.asList("java.", "sun.", "org.lwjgl.", "org.apache.logging.", "org.bookmc.transformie.", "com.sun.", "javax."));

    public TransformieClassLoader() {
        super(TransformieClassLoader.class.getClassLoader());
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        ClassLoader parent = getParent();

        for (String exclusion : exclusions) {
            if (name.startsWith(exclusion)) {
                return parent.loadClass(name);
            }
        }

        try (InputStream is = getResourceAsStream(name.replace(".", "/") + ".class")) {
            if (is != null) {
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    byte[] data = new byte[1024];

                    int read;

                    while ((read = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, read);
                    }

                    byte[] classBytes = buffer.toByteArray();

                    for (Transformer transformer : transformers) {
                        classBytes = transformer.transform(name, classBytes);
                    }

                    return defineClass(name, classBytes, 0, classBytes.length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.loadClass(name);
    }

    public static void addTransformer(Transformer transformer) {
        transformers.add(transformer);
    }

    public static void addClassloaderExclusion(String dontLoad) {
        exclusions.add(dontLoad);
    }
}
