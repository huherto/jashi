package jashi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.SOURCE)
public @interface Classpath {
	
	String value();

}
