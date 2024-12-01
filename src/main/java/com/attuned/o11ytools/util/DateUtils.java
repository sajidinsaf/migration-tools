package com.attuned.o11ytools.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  public String getCurrentTimeStampAsDateString(String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.format(new Date());
  }
}
