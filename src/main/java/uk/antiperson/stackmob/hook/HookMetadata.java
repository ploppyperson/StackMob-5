package uk.antiperson.stackmob.hook;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface HookMetadata {
    String name();
    String config();
}
