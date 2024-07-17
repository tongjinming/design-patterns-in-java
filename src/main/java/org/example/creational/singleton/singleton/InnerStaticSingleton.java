package org.example.creational.singleton.singleton;

public class InnerStaticSingleton {
    // 私有构造函数
    private InnerStaticSingleton() {
        System.out.println("Singleton is initializing");
    }

    // 静态内部类
    private static class Impl {
        private static final InnerStaticSingleton INSTANCE = new InnerStaticSingleton();
    }

    // 公共静态方法获取实例
    public static InnerStaticSingleton getInstance() {
        return Impl.INSTANCE;
    }
}
