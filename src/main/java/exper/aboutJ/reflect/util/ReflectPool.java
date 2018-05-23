package exper.aboutJ.reflect.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReflectPool<T> {

    public final int max;
    public final Constructor<T> constructor;
    public int peak;

    //private final ConcurrentLinkedQueue freeObjects;

    public ReflectPool(Class<T> cls) {
        this(cls, Integer.MAX_VALUE);
    }

    public ReflectPool(Class<T> cls, int max) {
        //freeObjects= new ConcurrentLinkedQueue<>();
        constructor = findConstructor(cls);
        this.max = max;
    }

    public Constructor findConstructor(Class<T> cls){
        try {
            return cls.getConstructor();
        } catch (NoSuchMethodException e) {
            try {
                return cls.getDeclaredConstructor();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    public T newObject(){
        try {
            return (T)constructor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T obtain(Class<T> cls){
        return new ReflectPool<T>(cls,10).newObject();
    }

}
