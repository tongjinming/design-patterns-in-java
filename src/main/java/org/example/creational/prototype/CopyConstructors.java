package org.example.creational.prototype;

class HumanAddress {
    public String streetAddress, city, country;

    public HumanAddress(String streetAddress, String city, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    // developer can control the copy process and the properties to copy
    public HumanAddress(HumanAddress other) {
        this(other.streetAddress, other.city, other.country);
    }

    @Override
    public String toString() {
        return "HumanAddress{" +
                "streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}

class Employee {
    public String name;
    public HumanAddress address;

    public Employee(String name, HumanAddress address) {
        this.name = name;
        this.address = address;
    }

    public Employee(Employee other) {
        name = other.name;
        address = new HumanAddress(other.address);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}

class CopyConstructorDemo {
    public static void main(String[] args) {
        Employee john = new Employee("John",
                new HumanAddress("123 London Road", "London", "UK"));

        //Employee chris = john;
        Employee chris = new Employee(john);

        chris.name = "Chris";
        System.out.println(john);
        System.out.println(chris);
    }
}
