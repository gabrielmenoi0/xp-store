package com.xp.store.utils;


import jakarta.validation.constraints.Null;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UtilsDate {

    public static String dateNow(@Null DateTimeFormatter formatter){
        DateTimeFormatter dateFormatter = formatter == null ? DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss") : formatter;
        return Instant.now().atZone(ZoneId.of("America/Sao_Paulo"))
                .format(dateFormatter);
    }

    public static String dateTimeNow(){return dateNow(null);}

}
