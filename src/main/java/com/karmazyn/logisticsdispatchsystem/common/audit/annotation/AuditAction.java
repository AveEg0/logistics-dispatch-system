package com.karmazyn.logisticsdispatchsystem.common.audit.annotation;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditAction {
    UserAction value();
}
