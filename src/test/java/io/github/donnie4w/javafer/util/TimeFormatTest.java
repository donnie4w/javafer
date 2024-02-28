package io.github.donnie4w.javafer.util;

import io.github.donnie4w.javafer.util.TimeFormat;
import org.junit.Test;

public class TimeFormatTest {

    @Test
    public  void timeTest(){
        System.out.println(TimeFormat.time(TimeFormat.DateTime));
        System.out.println(TimeFormat.time(TimeFormat.DateOnly));
        System.out.println(TimeFormat.time(TimeFormat.TimeOnly));
        System.out.println(TimeFormat.time(TimeFormat.StampNano));
    }
}
