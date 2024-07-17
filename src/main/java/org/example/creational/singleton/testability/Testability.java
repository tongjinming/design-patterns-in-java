package org.example.creational.singleton.testability;

import com.google.common.collect.Iterables;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

interface Database {
    int getPopulation(String name);
}


class SingletonDatabase {
    private Dictionary<String, Integer> capitals = new Hashtable<>();
    private static int instanceCount = 0;

    public static int getCount() {
        return instanceCount;
    }

    private SingletonDatabase() {
        instanceCount++;
        System.out.println("Initializing database");

        try {
            File f = new File(
                    Testability.class.getProtectionDomain()
                            .getCodeSource().getLocation().getPath()
            );
            // 从应用程序的运行目录中读取一个名为 capitals.txt 的文件，并将其内容按行读取到一个字符串列表中
            Path fullPath = Paths.get(f.getPath(), "capitals.txt");
            List<String> lines = Files.readAllLines(fullPath);

            // Google Guava library的Iterables.partition方法将字符串列表分成大小为2的分区,返回一个 Iterable<List<String>>, 然后我们可以对其进行迭代，每个子列表包含两个元素：第一个元素是城市名称（key），第二个元素是对应的人口数量（value）。最后，我们将这些键值对放入一个哈希表中。
            Iterables.partition(lines, 2)
                    .forEach(kv -> capitals.put(
                            kv.get(0).trim(),
                            Integer.parseInt(kv.get(1))
                    ));
        } catch (Exception e) {
            // handle it!
            System.err.println(e);
        }
    }

    private static final SingletonDatabase INSTANCE = new SingletonDatabase();

    public static SingletonDatabase getInstance() {
        return INSTANCE;
    }

    public int getPopulation(String name) {
        return capitals.get(name);
    }
}

class SingletonRecordFinder {
    public int getTotalPopulation(List<String> names) {
        int result = 0;
        for (String name : names)
            result += SingletonDatabase.getInstance().getPopulation(name);
        return result;
    }
}

class ConfigurableRecordFinder {
    private Database database;

    public ConfigurableRecordFinder(Database database) {
        this.database = database;
    }

    public int getTotalPopulation(List<String> names) {
        int result = 0;
        for (String name : names)
            result += database.getPopulation(name);
        return result;
    }
}

class SingletonTestabilityDemo {
    public static void main(String[] args) {
        SingletonDatabase db = SingletonDatabase.getInstance();

        String city = "Tokyo";
        int pop = db.getPopulation(city);
        System.out.println(
                String.format("%s has population %d", city, pop)
        );
    }
}

class DummyDatabase implements Database {
    private Dictionary<String, Integer> data = new Hashtable<>();

    public DummyDatabase() {
        data.put("alpha", 1);
        data.put("beta", 2);
        data.put("gamma", 3);
    }

    @Override
    public int getPopulation(String name) {
        return data.get(name);
    }
}

public class Testability {
    @Test
    public void isSingletonTest() {
        SingletonDatabase db = SingletonDatabase.getInstance();
        SingletonDatabase db2 = SingletonDatabase.getInstance();
        assertSame(db, db2);
        assertEquals(1, SingletonDatabase.getCount());
    }

    @Test // this is an integration test, not a unit test
    public void singletonTotalPopulationTest() {
        // testing on a live database
        SingletonRecordFinder rf = new SingletonRecordFinder();
        List<String> names = List.of("Seoul", "Mexico City");
        int tp = rf.getTotalPopulation(names);
        assertEquals(17500000 + 17400000, tp);
    }

    @Test // this is an unit test
    public void dependentPopulationTest() {
        DummyDatabase db = new DummyDatabase();
        ConfigurableRecordFinder rf = new ConfigurableRecordFinder(db);
        assertEquals(4, rf.getTotalPopulation(
                List.of("alpha", "gamma")
        ));
    }
}