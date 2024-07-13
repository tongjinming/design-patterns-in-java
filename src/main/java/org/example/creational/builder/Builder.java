package org.example.creational.builder;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents an HTML element.
 * An HTML element can have a name, text, and child elements.
 */
class HtmlElement {
    public String name, text;
    public ArrayList<HtmlElement> elements = new ArrayList<HtmlElement>();
    // 缩进字符数
    private final int indentSize = 2;
    private final String newLine = System.lineSeparator();

    public HtmlElement() {
    }

    public HtmlElement(String name, String text) {
        this.name = name;
        this.text = text;
    }

    /**
     * This method is used to convert an HtmlElement into a formatted string representation.
     * The string representation includes the element's name, text, and any child elements.
     * The method uses indentation to visually represent the hierarchy of elements.
     *
     * @param indent 当前的缩进级别。这用于格式化输出字符串。
     * @return HtmlElement的字符串表示形式
     */
    private String toStringImpl(int indent) {
        // Create a new StringBuilder to build the output string.
        StringBuilder sb = new StringBuilder();

        // Generate a string of spaces for the current level of indentation.
        String i = String.join("", Collections.nCopies(indent * indentSize, " "));

        // Append the opening tag for the element.
        sb.append(String.format("%s<%s>%s", i, name, newLine));

        // If the element has text, append it with additional indentation.
        if (text != null && !text.isEmpty()) {
            sb.append(String.join("", Collections.nCopies(indentSize * (indent + 1), " ")))
                    .append(text)
                    .append(newLine);
        }

        // For each child element, append its string representation with increased indentation.递归的终止条件是elements列表为空
        for (HtmlElement e : elements)
            sb.append(e.toStringImpl(indent + 1));

        // Append the closing tag for the element.
        sb.append(String.format("%s</%s>%s", i, name, newLine));

        // Return the final string representation of the element.
        return sb.toString();
    }

    @Override
    public String toString() {
        return toStringImpl(0);
    }
}

/**
 * Builds an HTML document using HtmlElements.
 */
class HtmlBuilder {
    private String rootName;
    private HtmlElement root = new HtmlElement();

    public HtmlBuilder(String rootName) {
        this.rootName = rootName;
        root.name = rootName;
    }

    // not fluent
    public void addChild(String childName, String childText) {
        HtmlElement e = new HtmlElement(childName, childText);
        root.elements.add(e);
    }

    public HtmlBuilder addChildFluent(String childName, String childText) {
        HtmlElement e = new HtmlElement(childName, childText);
        root.elements.add(e);
        return this;
    }

    public void clear() {
        root = new HtmlElement();
        root.name = rootName;
    }

    // delegating
    @Override
    public String toString() {
        return root.toString();
    }

    // 下面方法是为了方便测试，证明支持嵌套元素
    public HtmlBuilder addChildWithChildren(String childName, String childText, String grandchildName, String grandchildText) {
        HtmlElement child = new HtmlElement(childName, childText);
        HtmlElement grandchild = new HtmlElement(grandchildName, grandchildText);
        child.elements.add(grandchild);
        root.elements.add(child);
        return this;
    }
}

/**
 * Demonstrates the use of the HtmlBuilder class.
 */
class BuilderDemo {
    public static void main(String[] args) {


        // we want to build a simple HTML paragraph
        System.out.println("Testing");
        String hello = "hello";
        System.out.println("<p>" + hello + "</p>");

        // now we want to build a list with 2 words
        String[] words = {"hello", "world"};
        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>\n");
        for (String word : words) {
            // indentation management, line breaks and other evils
            sb.append(String.format("  <li>%s</li>\n", word));
        }
        sb.append("</ul>");
        System.out.println(sb);

        // ordinary non-fluent builder
        HtmlBuilder builder = new HtmlBuilder("ul");
        builder.addChild("li", "hello");
        builder.addChild("li", "world");
        System.out.println(builder);

        // fluent builder
        builder.clear();
        builder.addChildFluent("li", "hello")
                .addChildFluent("li", "world");
        System.out.println(builder);

        builder.clear();
        // 下面的代码段演示了如何使用addChildWithChildren方法构建具有子元素的元素。
        HtmlBuilder builderTest = new HtmlBuilder("body");
        builderTest.addChildWithChildren("ul", "li", "li", "hello");
        System.out.println(builderTest);
    }
}