package org.example.creational.singleton.singleton;

public class LazySingleton {
    private static volatile LazySingleton instance;

    private LazySingleton() {
        System.out.println("Initializing a lazy singleton");
    }

    // double-checked locking
    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}
