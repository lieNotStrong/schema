 package com.scheduling.common;
 
 public enum RetCode {
   OK(0, "success"),
   ERROR(1500, "操作失败"),
   NOPERM(1501, "没有权限"),
   PARAM_NULL(1502, "参数不可以为空"),
   UNKONW_ERROR(1515, "未知异常");
   private int code;
   private String msg;
   
   private RetCode(int code, String msg) {
     this.code = code;
     this.msg = msg;
   }
   
   public int code() {
     return this.code;
   }
   
   public String msg() {
     return this.msg;
   }
   
   public void setMsg(String msg) {
     this.msg = msg;
   }
 }


