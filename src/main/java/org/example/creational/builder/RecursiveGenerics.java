package org.example.creational.builder;

// builder inheritance with recursive generics

class Person {
    public String name;

    public String position;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}

// 下面PersonBuilder定义很有技巧性！使用递归泛型，使得子类的方法返回的是子类的类型，而不是父类的类型
// SELF是一个类型参数，用于实现递归泛型，SELF继承了PersonBuilder<SELF>。这种模式允许链式调用方法时，返回的类型是当前类的类型，而不是父类的类型。这对于构建者模式（Builder Pattern）特别有用，因为它允许派生类的方法返回派生类的实例，从而支持方法链的流畅使用。
class PersonBuilder<SELF extends PersonBuilder<SELF>> {
    protected Person person = new Person();

    // critical to return SELF here
    // 在PersonBuilder类中，withName方法返回类型为SELF，这意味着当你在子类中调用这个方法时，返回的对象类型仍然是那个子类的类型，而不是PersonBuilder类型。这样，就可以继续链式调用子类中定义的其他方法，而不会丢失子类的类型信息。
    public SELF withName(String name) {
        person.name = name;
        return self();
    }

    protected SELF self() {
        // unchecked cast, but actually safe
        // proof: try sticking a non-PersonBuilder
        //        as SELF parameter; it won't work!
        return (SELF) this;
    }

    public Person build() {
        return person;
    }
}

class EmployeeBuilder
        extends PersonBuilder<EmployeeBuilder> {
    public EmployeeBuilder worksAs(String position) {
        person.position = position;
        return self();
    }

    @Override
    protected EmployeeBuilder self() {
        return this;
    }
}

class RecursiveGenericsDemo {
    public static void main(String[] args) {
        // 假如不用递归泛型，那么下面的withName返回的就是对象类型就是PersonBuilder类型而不是我们真正想要的子类的类型
        EmployeeBuilder eb = new EmployeeBuilder()
                .withName("Dmitri")
                .worksAs("Quantitative Analyst");
        System.out.println(eb.build());
    }
}
