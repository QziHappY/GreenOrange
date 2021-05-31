package com.Utils;

import org.apache.logging.log4j.util.Strings;

import java.util.Random;

public class Opt {
    //生成指定长度的验证码
    static public String setOpt(int length){
        Random random=new Random(System.currentTimeMillis());//时间戳作随机数种子
        StringBuilder opt=new StringBuilder();
        for(int i=0;i<length;i++){
            int randomInt=random.nextInt(10);//产生从0~9的随机数
            opt.append(randomInt);
        }
        return opt.toString();
    }
    //向指定手机号发送验证码：成功为1，失败为0
    static public int sendOpt(String opt,String telphone,String type){
        if(true){
            System.out.println("已经向手机号为：【"+telphone+"】 发送验证码："+opt+"--用途为："+type);
            return 1;
        }else{
            return 0;
        }
    }
}
