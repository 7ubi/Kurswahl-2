package com.x7ubi.kurswahl.admin.authentication;

import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.repository.AdminRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AdminRequiredAspect {
    private final JwtUtils jwtUtils;

    private final AdminRepo adminRepo;

    public AdminRequiredAspect(JwtUtils jwtUtils, AdminRepo adminRepo) {
        this.jwtUtils = jwtUtils;
        this.adminRepo = adminRepo;
    }

    @Pointcut("@annotation(com.x7ubi.kurswahl.admin.authentication.AdminRequired)")
    private void adminRequiredAnnotation() {
    }

    @Around("com.x7ubi.kurswahl.admin.authentication.AdminRequiredAspect.adminRequiredAnnotation()")
    public Object adminRequired(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest req = getRequest();

        String token = req.getHeader("Authorization");
        String username = this.jwtUtils.getUsernameFromAuthorizationHeader(token);

        if (!this.adminRepo.existsAdminByUser_Username(username)) {
            throw new AccessDeniedException("Du musst ein Admin sein, um diese Ressource zu verwenden.");
        }

        return pjp.proceed();
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        return sra.getRequest();
    }
}
