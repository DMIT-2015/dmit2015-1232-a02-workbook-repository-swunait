package dmit2015.security;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

/**
 * Sales role are permitted to call methods that begin with add, update, and delete.
 * IT role are permitted to call that begins with delete.
 */
public class MovieRepositorySecurityInterceptor {
    @Inject
    private jakarta.security.enterprise.SecurityContext securityContext;

    @AroundInvoke
    public Object verifyAccess(InvocationContext ic) throws Exception {
        String methodName = ic.getMethod().getName();
        // Permit only Sales role to call methods that begin with add and update.
        if (methodName.startsWith("add") || methodName.startsWith("update")) {
            boolean isInRole = securityContext.isCallerInRole("Sales");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }
        // Permit only Sales or IT role to call methods that being with delete.
        if (methodName.startsWith("delete")) {
            boolean isInRole = securityContext.isCallerInRole("Sales") || securityContext.isCallerInRole("IT");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }
        return ic.proceed();
    }
}