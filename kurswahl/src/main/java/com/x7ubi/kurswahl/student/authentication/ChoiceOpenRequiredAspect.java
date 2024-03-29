package com.x7ubi.kurswahl.student.authentication;

import com.x7ubi.kurswahl.common.error.ErrorMessage;
import com.x7ubi.kurswahl.common.exception.DisabledException;
import com.x7ubi.kurswahl.common.settings.service.SettingsService;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChoiceOpenRequiredAspect {

    private final SettingsService settingsService;

    private final Logger logger = LoggerFactory.getLogger(ChoiceOpenRequiredAspect.class);

    public ChoiceOpenRequiredAspect(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Pointcut("@annotation(com.x7ubi.kurswahl.student.authentication.ChoiceOpenRequired)")
    private void choiceOpenRequiredAnnotation() {
    }

    @Around("com.x7ubi.kurswahl.student.authentication.ChoiceOpenRequiredAspect.choiceOpenRequiredAnnotation()")
    public Object choiceOpenRequired(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("CHECKING");
        if (!BooleanUtils.toBoolean(this.settingsService.getOrCreateSetting(SettingsService.CHOICE_OPEN, true).getValue())) {
            throw new DisabledException(ErrorMessage.CHOICE_DISABLED);
        }

        return pjp.proceed();
    }
}
