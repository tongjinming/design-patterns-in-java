package org.example.structural.bridge;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

interface Renderer {
    void renderCircle(float radius);
}

class VectorRenderer implements Renderer {
    @Override
    public void renderCircle(float radius) {
        System.out.println("Drawing a circle of radius " + radius);
    }
}

class RasterRenderer implements Renderer {
    @Override
    public void renderCircle(float radius) {
        System.out.println("Drawing pixels for a circle of radius " + radius);
    }
}

abstract class Shape {
    protected Renderer renderer;

    public Shape(Renderer renderer) {
        this.renderer = renderer;
    }

    public abstract void draw();

    public abstract void resize(float factor);
}

class Circle extends Shape {
    public float radius;

    @Inject
    public Circle(Renderer renderer) {
        super(renderer);
    }

    public Circle(Renderer renderer, float radius) {
        super(renderer);
        this.radius = radius;
    }

    @Override
    public void draw() {
        renderer.renderCircle(radius);
    }

    @Override
    public void resize(float factor) {
        radius *= factor;
    }
}

// 创建的模块类，用来用来配置Google juice依赖注入
class ShapeModule extends AbstractModule {
    @Override
    protected void configure() {
        // 将Renderer接口绑定到VectorRenderer实现。即每次请求注入Renderer时，都会创建一个新的VectorRenderer实例
        bind(Renderer.class).to(VectorRenderer.class);
    }
}

class BridgeDemo {
    public static void main(String[] args) {
//    手动进行依赖注入
//    RasterRenderer rasterRenderer = new RasterRenderer();
//    VectorRenderer vectorRenderer = new VectorRenderer();
//    Circle circle = new Circle(vectorRenderer, 5);
//    circle.draw();
//    circle.resize(2);
//    circle.draw();

        // 使用Google Guice依赖注入框架实现自动注入
        Injector injector = Guice.createInjector(new ShapeModule());
        Circle instance = injector.getInstance(Circle.class);
        instance.radius = 3;
        instance.draw();
        instance.resize(2);
        instance.draw();
    }
}

