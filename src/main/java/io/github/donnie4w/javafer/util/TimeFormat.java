package io.github.donnie4w.javafer.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormat {
  public static final DateTimeFormatter ANSIC = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy");
  public static final DateTimeFormatter UnixDate = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss z yyyy");
  public static final DateTimeFormatter RubyDate = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss Z yyyy");
  public static final DateTimeFormatter RFC822 = DateTimeFormatter.ofPattern("d MMM yy HH:mm z");
  public static final DateTimeFormatter RFC822Z = DateTimeFormatter.ofPattern("d MMM yy HH:mm Z");
  public static final DateTimeFormatter RFC850 = DateTimeFormatter.ofPattern("EEEE, dd-MMM-yy HH:mm:ss z");
  public static final DateTimeFormatter RFC1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z");
  public static final DateTimeFormatter RFC1123Z = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");
  public static final DateTimeFormatter RFC3339 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
  public static final DateTimeFormatter RFC3339Nano = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX");
  public static final DateTimeFormatter Kitchen = DateTimeFormatter.ofPattern("h:mm a");
  public static final DateTimeFormatter Stamp = DateTimeFormatter.ofPattern("MMM d HH:mm:ss");
  public static final DateTimeFormatter StampMilli = DateTimeFormatter.ofPattern("MMM d HH:mm:ss.SSS");
  public static final DateTimeFormatter StampMicro = DateTimeFormatter.ofPattern("MMM d HH:mm:ss.SSSSSS");
  public static final DateTimeFormatter StampNano = DateTimeFormatter.ofPattern("MMM d HH:mm:ss.SSSSSSSSS");
  public static final DateTimeFormatter DateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter DateOnly = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final DateTimeFormatter TimeOnly = DateTimeFormatter.ofPattern("HH:mm:ss");

  public  static String time(DateTimeFormatter formatter){
    ZonedDateTime dateTime = ZonedDateTime.now();
    return dateTime.format(formatter);
  }

}
