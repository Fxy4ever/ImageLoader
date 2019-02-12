package com.fxymine4ever.main.imageloader.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 关闭IO流工具类
 */
public class CloseUtil {
    public static void close(Closeable close){
        try {
            if(close!=null){
                close.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
