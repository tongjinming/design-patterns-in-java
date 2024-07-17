package org.example.creational.singleton.singleton;

import java.io.*;

// since Java 1.5
// you cannot inherit from this
enum EnumBasedSingleton {
    // 枚举常量，一般在枚举声明的开始部分定义，通常使用大写字母。枚举常量之间用逗号分隔。枚举常量实际上是静态的最终实例，它们是枚举类型的公共静态最终字段。枚举常量不需要数据类型声明。
    INSTANCE; // hooray

    // 枚举类型已经有一个默认的私有构造函数，但是如果需要初始化东西，可以按照下面的方式编写自己的构造函数
    // Java不允许给枚举类型定义public或protected的构造函数
    EnumBasedSingleton() {
        value = 42;
    }

    // enums are inherently serializable (but what good is that?)
    // reflection not a problem, guaranteed 1 instance in JVM

    // 实例变量，定义在枚举类型内部的变量
    // field values not serialized!
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class EnumBasedSingletonDemo {
    static void saveToFile(EnumBasedSingleton singleton, String filename)
            throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(singleton);
        }
    }

    static EnumBasedSingleton readFromFile(String filename)
            throws Exception {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (EnumBasedSingleton) in.readObject();
        }
    }

    //
    public static void main(String[] args)
            throws Exception {
        String filename = "myfile.bin";

        // run again with next 3 lines commented out

        EnumBasedSingleton singleton = EnumBasedSingleton.INSTANCE;
        singleton.setValue(111);
        saveToFile(singleton, filename);

        EnumBasedSingleton singleton2 = readFromFile(filename);
        System.out.println(singleton2 == singleton);// true，内存地址相同是同一个对象
        System.out.println(singleton2.getValue());// 111
    }
}
