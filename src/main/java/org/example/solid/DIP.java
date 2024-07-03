package org.example.solid;

// A. High-level modules should not depend on low-level modules.
// Both should depend on abstractions.

// B. Abstractions should not depend on details.
// Details should depend on abstractions.

import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum Relationship {
    PARENT,
    CHILD,
    SIBLING
}

class Person {
    public String name;
    // dob etc.


    public Person(String name) {
        this.name = name;
    }
}

interface RelationshipBrowser {
    List<Person> findAllChildrenOf(String name);
}

class Relationships implements RelationshipBrowser {
    /**
     * 该方法用于查找给定姓名的人的所有子女。
     *
     * @param name 要查找子女的人的姓名
     * @return 代表给定姓名的人的所有子女的Person对象列表
     *
     * <p>该方法通过流式处理关系，过滤出第一个值的姓名等于提供的姓名且关系为PARENT的元素，
     * 然后映射结果以获取三元组的第三个值，该值在关系中代表子女。结果是所有匹配Person对象的列表。
     */
    public List<Person> findAllChildrenOf(String name) {
        return relations.stream()
                .filter(x -> Objects.equals(x.getValue0().name, name)
                        && x.getValue1() == Relationship.PARENT)
                .map(Triplet::getValue2)
                .collect(Collectors.toList());
    }

    // Triplet class requires javatuples。org.javatuples.Triplet 是来自 JavaTuples 库的一个类。JavaTuples 是一个开源的 Java 库，它提供了一些用于处理元组的类。元组是一种可以存储固定数量的项的数据结构，这些项可以是不同类型的。Triplet 类表示一个包含三个元素的元组。你可以使用它来存储三个相关联的对象，而不需要创建一个自定义的类。例如，你可以使用 Triplet 来存储一个人的名字、年龄和地址。
    private List<Triplet<Person, Relationship, Person>> relations =
            new ArrayList<>();

    public List<Triplet<Person, Relationship, Person>> getRelations() {
        return relations;
    }

    public void addParentAndChild(Person parent, Person child) {
        relations.add(new Triplet<>(parent, Relationship.PARENT, child));
        relations.add(new Triplet<>(child, Relationship.CHILD, parent));
    }
}

class Research {
    public Research(Relationships relationships) {
        // high-level: find all of john's children
        List<Triplet<Person, Relationship, Person>> relations = relationships.getRelations();
        relations.stream()
                .filter(x -> x.getValue0().name.equals("John")
                        && x.getValue1() == Relationship.PARENT)
                .forEach(ch -> System.out.println("John has a child called " + ch.getValue2().name));
    }

    public Research(RelationshipBrowser browser) {
        List<Person> children = browser.findAllChildrenOf("John");
        for (Person child : children)
            System.out.println("John has a child called " + child.name);
    }
}

class DIPDemo {
    public static void main(String[] args) {
        Person parent = new Person("John");
        Person child1 = new Person("Chris");
        Person child2 = new Person("Matt");

        // low-level module
        Relationships relationships = new Relationships();
        relationships.addParentAndChild(parent, child1);
        relationships.addParentAndChild(parent, child2);

        new Research(relationships);
    }
}
