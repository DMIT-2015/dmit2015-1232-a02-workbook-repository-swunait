package dmit2015.model;

import lombok.Getter;
import lombok.Setter;

public class Rectangle {

    @Getter @Setter
    private double length;
    @Getter @Setter
    private double width;

    public double area() {
        return length * width;
    }

    public double perimeter() {
        return 2 * (length + width);
    }
}
