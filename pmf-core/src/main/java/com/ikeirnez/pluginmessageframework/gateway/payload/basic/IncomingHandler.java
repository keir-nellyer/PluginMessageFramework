package com.ikeirnez.pluginmessageframework.gateway.payload.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a constructor as being capable of handling incoming data.
 * There should only be 1 constructor marked with this annotation per class (framework limitation).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface IncomingHandler {

    String value();

}
