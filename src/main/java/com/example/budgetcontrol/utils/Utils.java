package com.example.budgetcontrol.utils;

import java.time.LocalDateTime;
import java.util.Date;

public class Utils {

    public static LocalDateTime getTimeNow() {
        return DateUtil.getInstance().convertToLocalDateTime(new Date());
    }
}
