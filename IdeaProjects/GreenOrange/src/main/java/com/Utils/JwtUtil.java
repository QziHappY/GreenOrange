package com.Utils;

import com.sun.javafx.scene.traversal.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class JwtUtil {

    public static String setToken(){
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}