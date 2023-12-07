package com.x7ubi.kurswahl.student.authentication;

import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.repository.StudentRepo;
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
public class StudentRequiredAspect {
    private final JwtUtils jwtUtils;

    private final StudentRepo studentRepo;

    public StudentRequiredAspect(JwtUtils jwtUtils, StudentRepo studentRepo) {
        this.jwtUtils = jwtUtils;
        this.studentRepo = studentRepo;
    }

    @Pointcut("@annotation(com.x7ubi.kurswahl.student.authentication.StudentRequired)")
    private void studentRequiredAnnotation() {
    }

    @Around("com.x7ubi.kurswahl.student.authentication.StudentRequiredAspect.studentRequiredAnnotation()")
    public Object studentRequired(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest req = getRequest();

        String token = req.getHeader("Authorization");
        String username = this.jwtUtils.getUsernameFromAuthorizationHeader(token);

        if (!this.studentRepo.existsStudentByUser_Username(username)) {
            throw new AccessDeniedException("Du musst ein Schüler sein, um diese Ressource zu verwenden.");
        }

        return pjp.proceed();
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        return sra.getRequest();
    }
}
