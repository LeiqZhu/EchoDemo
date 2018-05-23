package exper.aboutJ.reflect;

import exper.aboutJ.reflect.bean.ReflectBean;
import exper.aboutJ.reflect.util.ReflectPool;

public class TestMain {

    public static void main(String[] args) {
        ReflectBean clazz = ReflectPool.obtain(ReflectBean.class);
        System.out.println(clazz.toString());

    }
}
