package com.x7ubi.kurswahl.student.authentication;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.DisabledException;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResultOpenRequiredAspect {

    private final SettingsService settingsService;

    public ResultOpenRequiredAspect(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Pointcut("@annotation(com.x7ubi.kurswahl.student.authentication.ResultOpenRequired)")
    private void resultOpenRequiredAnnotation() {
    }

    @Around("com.x7ubi.kurswahl.student.authentication.ResultOpenRequiredAspect.resultOpenRequiredAnnotation()")
    public Object choiceOpenRequired(ProceedingJoinPoint pjp) throws Throwable {
        if (!BooleanUtils.toBoolean(this.settingsService.getOrCreateSetting(SettingsService.RESULT_OPEN, false).getValue())) {
            throw new DisabledException(ErrorMessage.RESULT_DISABLED);
        }

        return pjp.proceed();
    }
}
