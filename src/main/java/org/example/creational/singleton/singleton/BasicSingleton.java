package org.example.creational.singleton.singleton;

import java.io.*;

class BasicSingleton implements Serializable {
    // cannot new this class from outside, however
    // * instance can be created deliberately (reflection)
    // * instance can be created accidentally (serialization)
    private BasicSingleton() {
        System.out.println("Singleton is initializing");
    }

    private static final BasicSingleton INSTANCE = new BasicSingleton();

    // Value to demonstrate mutable state within the singleton.
    private int value = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // required for correct serialization
    // readResolve is used for _replacing_ the object read from the stream
    // readResolve方法是在反序列化过程中由Java虚拟机（JVM）自动调用的。当JVM从输入流中读取对象时，如果发现类中定义了readResolve方法，它会在反序列化完成后立即调用此方法。通过这种方式，可以控制返回给调用者的对象，常用于保持单例模式的唯一性或者在反序列化时替换对象。用户不需要手动调用此方法。
    protected Object readResolve() {
        return INSTANCE;
    }

    // generated getter
    public static BasicSingleton getInstance() {
        return INSTANCE;
    }
}

class BasicSingletonDemo {
    /**
     * Saves the singleton instance to a file.
     *
     * @param singleton The singleton instance to save.
     * @param filename  The name of the file to save the singleton instance to.
     * @throws Exception If an I/O error occurs.
     */
    static void saveToFile(BasicSingleton singleton, String filename)
            throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(singleton);
        }
    }

    /**
     * Reads the singleton instance from a file.
     *
     * @param filename The name of the file to read the singleton instance from.
     * @return The singleton instance read from the file.
     * @throws Exception If an I/O error occurs.
     */
    static BasicSingleton readFromFile(String filename)
            throws Exception {
        //  try-with-resources 语句，这是一种简化的 try-catch 语法，专门用于自动管理资源的关闭。在这个语法中，所有实现了 AutoCloseable 接口的资源都可以在 try 语句后的括号内被声明。当执行块结束时，无论是正常结束还是遇到异常，这些资源都会自动被关闭。
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (BasicSingleton) in.readObject();
        }
    }

    public static void main(String[] args) throws Exception {
        BasicSingleton singleton = BasicSingleton.getInstance();
        singleton.setValue(111);

        String filename = "singleton.bin";
        saveToFile(singleton, filename);

        singleton.setValue(222);

        BasicSingleton singleton2 = readFromFile(filename);

        System.out.println(singleton == singleton2);

        System.out.println(singleton.getValue());// 222
        System.out.println(singleton2.getValue());// 如果不加readResolve就是111，证明singleton2和singleton不是同一个对象
    }
}
