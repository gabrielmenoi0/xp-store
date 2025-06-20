package com.xp.store.utils.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface UtilsLogger {

    default Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }
}
