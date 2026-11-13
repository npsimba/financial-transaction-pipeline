
package com.yottaflex.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    public static Logger getLogger(Class<?> className) {
        return LoggerFactory.getLogger(className);
    }
}
