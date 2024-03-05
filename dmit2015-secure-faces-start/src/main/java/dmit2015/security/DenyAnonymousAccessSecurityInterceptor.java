package dmit2015.security;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class DenyAnonymousAccessSecurityInterceptor {
    @Inject
    private jakarta.security.enterprise.SecurityContext _securityContext;

    @AroundInvoke
    public Object verifyAccess(InvocationContext ic) throws Exception {
// Deny access to anonymous users
        String username = _securityContext.getCallerPrincipal().getName();
        if (username.equalsIgnoreCase("anonymous")) {
            throw new RuntimeException("Access denied! Only authenticated users have permission to execute this method");
        }
        return ic.proceed();
    }
}