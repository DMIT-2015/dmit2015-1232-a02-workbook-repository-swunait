package dmit2015.faces;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.omnifaces.util.Messages;

@Named      // allows you to access an object of this class with the name applicationScopeCounterView using EL
@ApplicationScoped  // create this object once for the application
public class ApplicationScopeCounterView {

    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public String submit() {
        counter += 1;
        Messages.addInfo(null,"ApplicationScoped Counter = {0}", counter);
        Messages.addInfo("counterResult", "ApplicationScoped Count Result = {0}", counter);
        return "";
    }
}