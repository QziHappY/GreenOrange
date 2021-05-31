package com.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class OrderUtils {
    /**
     * 生成订单流水号
     * @return
     */
    public static String getSerialNumber(int orderListSize) {
        StringBuffer serialnumber = new StringBuffer();
        serialnumber.append(new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()));
        serialnumber.append("-"+orderListSize+"-");
        Random r = new Random();
        serialnumber.append(String.valueOf(r.nextInt(900000)+100000));
        return serialnumber.toString();
    }
}
