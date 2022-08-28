package com.uteq.app_smart_pills_dispenser.utils;

public class MoreUtils {

        public static <T> T coalesce(T one, T two)
        {
            return one != null ? one : two;
        }

}
