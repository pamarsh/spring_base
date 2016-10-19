package org.swamps.houseController.counters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by paul on 8/01/13.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Counted {

    String key = "";


    String value();
}
