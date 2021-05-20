package org.bookmc.transformie;

import org.bookmc.transformie.classloader.TransformieClassLoader;

public class Transformie {

    private final ClassLoader classLoader;
    private final String clazz;

    public Transformie(String clazz) {
        this.classLoader = new TransformieClassLoader();
        this.clazz = clazz;

        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public void main(String[] args) {
        try {
            Class<?> cl = classLoader.loadClass(clazz);

            cl.getDeclaredMethod("main", String[].class).invoke(null, (Object) args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
