package com.Controller.ReturnType;

/**
 * 通用返回信息类
 */
public class CommonReturnType {
    //对应请求的处理结果状态：success/error
    private String status;
    //对应请求的处理结果：success则为返回数据、error则为通用错误码信息
    private Object data;

    //创建 通用返回信息类
    public static CommonReturnType create(Object data){
        return CommonReturnType.create("success",data);
    }

    public static CommonReturnType create(String status ,Object data){
        CommonReturnType commonReturnType=new CommonReturnType();
        commonReturnType.setData(data);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

