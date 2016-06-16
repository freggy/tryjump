package de.bergwerklabs.tryjump.gameserver.util;

import net.amoebaman.util.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nexotekHD on 04.06.2016.
 */
public class ReclectionTests {


    private void test() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Object o = Reflection.getNMSClass("RetardObject").newInstance();
        Field f = Reflection.getField(o.getClass(), "a");
        int a = (int) f.get(o);
        Method method = Reflection.getMethod(o.getClass(), "b", int.class, boolean.class, int.class);
        method.invoke(o, 100, true,100);

        Constructor<?> constructor = Reflection.getNMSClass("RetardObject$RetardObject").getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        Object o1 = constructor.newInstance(100);
       // o1.getClass().getEnumConstants()[0];
    }

    public static class RetardObject
    {
        private int a = 1234;

        private RetardObject(int a)
        {
            this.a = a;
        }

        private void b(int i, boolean b, int fucku)
        {

        }
    }

}
