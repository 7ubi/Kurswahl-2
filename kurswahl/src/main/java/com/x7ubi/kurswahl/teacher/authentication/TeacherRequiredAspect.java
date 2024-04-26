package com.x7ubi.kurswahl.teacher.authentication;

import com.x7ubi.kurswahl.common.jwt.JwtUtils;
import com.x7ubi.kurswahl.common.repository.TeacherRepo;
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
public class TeacherRequiredAspect {
    private final JwtUtils jwtUtils;

    private final TeacherRepo teacherRepo;

    public TeacherRequiredAspect(JwtUtils jwtUtils, TeacherRepo teacherRepo) {
        this.jwtUtils = jwtUtils;
        this.teacherRepo = teacherRepo;
    }

    @Pointcut("@annotation(com.x7ubi.kurswahl.teacher.authentication.TeacherRequired)")
    private void adminRequiredAnnotation() {
    }

    @Around("com.x7ubi.kurswahl.teacher.authentication.TeacherRequiredAspect.adminRequiredAnnotation()")
    public Object adminRequired(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest req = getRequest();

        String token = req.getHeader("Authorization");
        String username = this.jwtUtils.getUsernameFromAuthorizationHeader(token);

        if (!this.teacherRepo.existsTeacherByUser_Username(username)) {
            throw new AccessDeniedException("Du musst ein Lehrer sein, um diese Ressource zu verwenden.");
        }

        return pjp.proceed();
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        return sra.getRequest();
    }
}
