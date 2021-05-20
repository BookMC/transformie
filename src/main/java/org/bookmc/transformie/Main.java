package org.bookmc.transformie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>(Arrays.asList(args));
        int mainClassIndex = stringList.indexOf("--transformieMain");
        String clazz = stringList.get(mainClassIndex + 1);

        stringList.remove(mainClassIndex);
        stringList.remove(mainClassIndex + 1);

        new Transformie(clazz).main(stringList.toArray(new String[0]));
    }
}
