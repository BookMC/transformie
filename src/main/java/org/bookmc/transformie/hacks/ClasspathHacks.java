package org.bookmc.transformie.hacks;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class ClasspathHacks {
    @SuppressWarnings({"restriction", "unchecked"})
    public static URL[] getClasspathURLs() {
        ClassLoader classLoader = ClasspathHacks.class.getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            return ((URLClassLoader) classLoader).getURLs();
        }

        if (classLoader.getClass().getName().startsWith("jdk.internal.loader.ClassLoaders$")) {
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                Unsafe unsafe = (Unsafe) field.get(null);

                // jdk.internal.loader.ClassLoaders.AppClassLoader.ucp
                Field ucpField;
                try {
                    ucpField = classLoader.getClass().getDeclaredField("ucp");
                } catch (NoSuchFieldException | SecurityException e) {
                    ucpField = classLoader.getClass().getSuperclass().getDeclaredField("ucp");
                }

                long ucpFieldOffset = unsafe.objectFieldOffset(ucpField);
                Object ucpObject = unsafe.getObject(classLoader, ucpFieldOffset);

                // jdk.internal.loader.URLClassPath.path
                Field pathField = ucpField.getType().getDeclaredField("path");
                long pathFieldOffset = unsafe.objectFieldOffset(pathField);
                ArrayList<URL> path = (ArrayList<URL>) unsafe.getObject(ucpObject, pathFieldOffset);

                return path.toArray(new URL[0]);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to find system class path URLs. Incompatible JDK?", e);
            }
        }
        return null;
    }
}
