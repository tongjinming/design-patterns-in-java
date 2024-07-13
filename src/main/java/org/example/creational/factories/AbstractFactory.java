package org.example.creational.factories;

import org.javatuples.Pair;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 表示热饮料的接口。
 */
interface IHotDrink {
    /**
     * 消费此热饮料。
     */
    void consume();
}

/**
 * 茶，一种热饮料。
 */
class Tea implements IHotDrink {
    @Override
    public void consume() {
        System.out.println("This tea is nice but I'd prefer it with milk.");
    }
}

/**
 * 咖啡，一种热饮料。
 */
class Coffee implements IHotDrink {
    @Override
    public void consume() {
        System.out.println("This coffee is delicious");
    }
}

/**
 * 热饮料工厂的接口。
 */
interface IHotDrinkFactory {
    /**
     * 准备指定数量的热饮料。
     *
     * @param amount 饮料的数量（例如，毫升）
     * @return 准备好的热饮料
     */
    IHotDrink prepare(int amount);
}

/**
 * 茶的工厂类。
 */
class TeaFactory implements IHotDrinkFactory {
    @Override
    public IHotDrink prepare(int amount) {
        System.out.println(
                "Put in tea bag, boil water, pour "
                        + amount + "ml, add lemon, enjoy!"
        );
        return new Tea();
    }
}

/**
 * 咖啡的工厂类。
 */
class CoffeeFactory implements IHotDrinkFactory {

    @Override
    public IHotDrink prepare(int amount) {
        System.out.println(
                "Grind some beans, boil water, pour "
                        + amount + " ml, add cream and sugar, enjoy!"
        );
        return new Coffee();
    }
}

/**
 * 热饮料机，能够制作多种热饮料。
 */
class HotDrinkMachine {
    /**
     * 可用的饮料类型。
     */
    public enum AvailableDrink {
        COFFEE, TEA
    }

    private Map<AvailableDrink, IHotDrinkFactory> factories =
            new HashMap<>();

    private List<Pair<String, IHotDrinkFactory>> namedFactories =
            new ArrayList<>();

    /**
     * 构造一个新的热饮料机实例。
     *
     * @throws Exception 如果无法实例化饮料工厂
     */
    public HotDrinkMachine() throws Exception {
        // 选项1：使用枚举初始化工厂
//        for (AvailableDrink drink : AvailableDrink.values()) {
//            String s = drink.toString();
//            String factoryName = "" + Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
//            Class<?> factory = Class.forName("org.example.creational.factories." + factoryName + "Factory");
//            factories.put(drink, (IHotDrinkFactory) factory.getDeclaredConstructor().newInstance());
//        }

        // 选项2：查找所有IHotDrinkFactory的实现
        Set<Class<? extends IHotDrinkFactory>> types =
                new Reflections("org.example.creational.factories")
                        .getSubTypesOf(IHotDrinkFactory.class);
        // 遍历了所有实现了IHotDrinkFactory接口的类的类型集合types。对于每个这样的类型，它创建了该类型的一个新实例，并将这个实例与其简化的类名（去掉了"Factory"后缀）一起存储在namedFactories列表中。这里使用Pair类型是为了同时存储两种信息：简化的类名和对应的工厂实例。
        for (Class<? extends IHotDrinkFactory> type : types) {
            namedFactories.add(new Pair<>(// 使用Pair可以方便地将这两个相关联的信息（饮料名称和工厂实例）捆绑在一起，便于后续处理，如显示可用饮料列表或根据用户选择制作饮料。Pair的getValue0()方法返回饮料名称，getValue1()方法返回对应的工厂实例。
                    type.getSimpleName().replace("Factory", ""),
                    type.getDeclaredConstructor().newInstance()
            ));
        }
    }

    /**
     * 通过用户输入制作一种热饮料。
     *
     * @return 制作好的热饮料
     * @throws IOException 如果读取输入时出错
     */
    public IHotDrink makeDrink() throws IOException {
        System.out.println("Available drinks");
        for (int index = 0; index < namedFactories.size(); ++index) {
            Pair<String, IHotDrinkFactory> item = namedFactories.get(index);
            System.out.println("" + index + ": " + item.getValue0());
        }

        // 使用BufferedReader来从控制台读取用户输入。使用InputStreamReader将System.in的字节流转换为字符流，然后BufferedReader再对这个字符流进行高效的缓冲读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String s;
            int i, amount;
            // reader.readLine()：从控制台读取一行输入。如果用户输入了内容并按下了回车键，这个方法会返回读取到的字符串。如果遇到输入流结束（EOF），则返回null
            if ((s = reader.readLine()) != null
                    && (i = Integer.parseInt(s)) >= 0
                    && i < namedFactories.size()) {
                System.out.println("Specify amount: ");
                s = reader.readLine();
                if (s != null && (amount = Integer.parseInt(s)) > 0) {
                    return namedFactories.get(i).getValue1().prepare(amount);
                }
            }
            System.out.println("Incorrect input, try again.");
        }
    }
}

public class AbstractFactory {
    public static void main(String[] args) throws Exception {
        HotDrinkMachine machine = new HotDrinkMachine();
        IHotDrink drink = machine.makeDrink();
        drink.consume();
    }
}