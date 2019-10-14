package plugins.kuaishou.com.yxcorp.gifshow.retrofit;

import android.util.Pair;

import mirror.RefClass;
import mirror.RefMethod;

public class k {
    public static void init(ClassLoader classLoader) {
        try {
            TYPE = RefClass.load(k.class, classLoader.loadClass("com.yxcorp.gifshow.retrofit.k"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Class<?> TYPE;

    public static RefMethod<Pair<String, String>> computeSignature;
}
