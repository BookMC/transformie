package org.bookmc.transformie;

import org.bookmc.transformie.classloader.TransformieClassLoader;
import org.bookmc.transformie.mixin.MixinTransformer;

public class Transformie {

    private final ClassLoader classLoader;
    private final String clazz;

    public Transformie(String clazz) {
        this.classLoader = new TransformieClassLoader();
        TransformieClassLoader.addTransformer(new MixinTransformer());
        this.clazz = clazz;

        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public void main(String[] args) {
        try {
            Class<?> c = Class.forName(clazz, true, classLoader);

            c.getDeclaredMethod("main", String[].class)
                .invoke(null, (Object) args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
