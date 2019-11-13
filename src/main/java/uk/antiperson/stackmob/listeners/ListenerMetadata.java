package uk.antiperson.stackmob.listeners;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ListenerMetadata {
    String config();
}
