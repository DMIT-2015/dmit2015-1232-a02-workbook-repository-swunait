package dmit2015.faces;

import dmit2015.model.Circle;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;

import java.io.Serializable;

@Named("currentCircleViewScopedView")
@ViewScoped // create this object for one HTTP request and keep in memory if the next is for the same page
// class must implement Serializable
public class CircleViewScopedView implements Serializable {

    // Declare read/write properties (field + getter + setter) for each form field

    // Declare read only properties (field + getter) for data sources
    @Getter
    private Circle currentCircle = new Circle();

    // Declare private fields for internal usage only objects

    @PostConstruct // This method is executed after DI is completed (fields with @Inject now have values)
    public void init() { // Use this method to initialized fields instead of a constructor
        // Code to access fields annotated with @Inject

    }

    public void onCalculateArea() {
        Messages.addGlobalInfo("The area is {0} for a radius of {1}",
                currentCircle.area(), currentCircle.getRadius());
    }
    public void onCalculateCircumference() {
        Messages.addGlobalInfo("The circumference is {0} for a radius of {1}",
                currentCircle.circumference(), currentCircle.getRadius());
    }

    public void onCalculateDiameter() {
        Messages.addGlobalInfo("The diameter is {0} for a radius of {1}",
                currentCircle.diameter(), currentCircle.getRadius());
    }
    public void onClear() {
        // Set all fields to default values
        currentCircle = new Circle();
    }
}