package org.example.solid;

class Rectangle {
    protected int width, height;

    public Rectangle() {
    }

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArea() {
        return width * height;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    public boolean isSquare() {
        return width == height;
    }
}

// 违反了LSP原则。Square类继承自Rectangle类，并且重写了setWidth()和setHeight()方法。这个设计的问题在于，Square类的行为与Rectangle类不一致。在Rectangle类中，setWidth()和setHeight()方法是独立的，改变一个不会影响另一个。但是在Square类中，setWidth()和setHeight()方法是相关的，改变一个会影响另一个。
class Square extends Rectangle {
    public Square() {
    }

    public Square(int size) {
        width = height = size;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        super.setWidth(height);
    }
}

class RectangleFactory {
    public static Rectangle newSquare(int side) {
        return new Rectangle(side, side);
    }

    public static Rectangle newRectangle(int width, int height) {
        return new Rectangle(width, height);
    }
}

class LSPDemo {
    // maybe conform to ++
    static void useIt(Rectangle r) {
        int width = r.getWidth();
        r.setHeight(10);
        System.out.println("Expected area of " + (width * 10) + ", got " + r.getArea());
    }

    public static void main(String[] args) {
        Rectangle rc = new Rectangle(2, 3);
        useIt(rc);

        Rectangle sq = new Square();
        sq.setHeight(5);
        sq.setWidth(10);
        useIt(sq);

        //output:
        //Expected area of 20, got 20
        //Expected area of 100, got 100
    }
}