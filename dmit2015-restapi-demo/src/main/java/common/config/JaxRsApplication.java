package common.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("restapi") // The resource-wide application path that forms the base URI of all root resource classes.
public class JaxRsApplication extends Application {

}