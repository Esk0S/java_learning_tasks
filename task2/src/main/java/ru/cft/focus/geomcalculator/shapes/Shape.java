package ru.cft.focus.geomcalculator.shapes;

public abstract class Shape {
    protected String name;
    protected double area;
    protected double perimeter;

    public double getArea() {
        return area;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public String getName() {
        return name;
    }

    protected String printCommon() {
        return String.format("""
                        Figure type: %s
                        Area: %.2f sqr mm
                        Perimeter: %.2f mm
                        """,
                getName(), getArea(), getPerimeter());
    }

    public abstract String printShape();
}
