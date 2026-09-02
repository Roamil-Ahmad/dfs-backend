package com.wallet.transaction.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  private DateUtils() {
    // private constructor to prevent instantiation
  }

  public static class DateBundle {
    public final String transDate;
    public final String yyyyMMdd;
    public final String HHmmss;
    public final String rrnDate;

    public DateBundle(String transDate, String yyyyMMdd, String HHmmss, String rrnDate) {
      this.transDate = transDate;
      this.yyyyMMdd = yyyyMMdd;
      this.HHmmss = HHmmss;
      this.rrnDate = rrnDate;
    }
  }

  public static DateBundle buildFormattedDates() {
    Date now = new Date();
    return new DateBundle(
        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(now),
        new SimpleDateFormat("yyyyMMdd").format(now),
        new SimpleDateFormat("HHmmss").format(now),
        new SimpleDateFormat("yyMMdd").format(now)
    );
  }
}


