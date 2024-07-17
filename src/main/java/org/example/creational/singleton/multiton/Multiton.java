package org.example.creational.singleton.multiton;

import java.util.HashMap;

/**
 * Represents a subsystem type for the Printer instances.
 * This enum defines the types of subsystems that can have a Printer instance.
 */
enum Subsystem {
    PRIMARY,
    AUXILIARY,
    FALLBACK
}

/**
 * Printer class implementing the Multiton pattern.
 * This class allows for a limited number of instances, one for each subsystem type.
 */
class Printer {
    private static int instanceCount = 0; // Tracks the number of instances created.

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Increments the instanceCount each time an instance is created.
     */
    private Printer() {
        instanceCount++;
        System.out.println("A total of " +
                instanceCount + " instances created so far.");
    }

    // Stores the instances of Printer, one for each Subsystem type.
    private static HashMap<Subsystem, Printer> instances = new HashMap<>();

    /**
     * Returns the Printer instance associated with the specified Subsystem.
     * If an instance for the Subsystem does not exist, it creates a new one.
     *
     * @param ss The Subsystem for which the Printer instance is requested.
     * @return The Printer instance for the specified Subsystem.
     */
    public static Printer get(Subsystem ss) {
        if (instances.containsKey(ss))
            return instances.get(ss);

        Printer instance = new Printer();
        instances.put(ss, instance);
        return instance;
    }
}

/**
 * Demonstrates the use of the Multiton pattern with the Printer class.
 */
public class Multiton {
    public static void main(String[] args) {
        // Fetching instances for PRIMARY and AUXILIARY subsystems.
        // AUXILIARY instance is fetched twice to demonstrate that the same instance is returned.
        Printer main = Printer.get(Subsystem.PRIMARY);
        Printer aux = Printer.get(Subsystem.AUXILIARY);
        Printer aux2 = Printer.get(Subsystem.AUXILIARY);
    }
}