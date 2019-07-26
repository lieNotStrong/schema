package com.scheduling.demo;

import com.google.common.io.Files;
import com.scheduling.utils.TLinx2Util;
import com.scheduling.utils.TestParams;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

public class DownloadBillDemo {

	  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadBillDemo.class);

  /**
   * Method downloadBill
   * Description 说明：
   *
   * @param day 说明：清算日期（YYYY-MM-DD，今天传昨天的日期）
   */

  public void downloadBill(String day, String tar_type) {
      String timestamp = new Date().getTime() / 1000 + "";    // 时间
//      System.out.println("==time=" + timestamp);
      try {
    	  // 固定参数
          TreeMap<String, String> postmap = new TreeMap<String, String>();    // 请求参数的map

          postmap.put("open_id", TestParams.OPEN_ID);
          postmap.put("timestamp", timestamp);

          TreeMap<String, Object> datamap = new TreeMap<String, Object>();    // data参数的map

          datamap.put("day", day);
          datamap.put("tar_type", tar_type);         

          /**
           * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
           */
          TLinx2Util.handleEncrypt(datamap, postmap);
          

          /**
           * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
           */
          TLinx2Util.handleSign(postmap);
          

          /**
           * 3 请求、响应
           */
          String rspStr = TLinx2Util.handlePost(postmap, TestParams.DOWNLOADBILL);

          System.out.println("返回字符串=" + rspStr);
    	  
                
          try{
              if(rspStr.isEmpty()){
                  return;
              }
              JSONObject respObject = JSONObject.fromObject(rspStr);
              System.out.println("===响应错误码：" + respObject.get("errcode"));
              System.out.println("===响应错误提示：" + respObject.get("msg"));
          }catch (JSONException e){
              /**
               * 4 保存到文本 或者.gz压缩文件中
               */
              String  filePath  = "d:/bill_download";
              File file = new File(filePath);
              if (!file.exists() &&!file.isDirectory()) {
                  file.mkdirs();
              }
              String fileExt = ".txt";
              String tarExt = ".txt.gz";
              String fileName = "bill_" + day;
              File tmpFile  = new File(filePath, fileName + fileExt);
              
              
              //如果对账文件已经存在，则保留，并更名为fileName+当前时间戳+".txt.bak"
              if (tmpFile.exists()) {
                  Files.move(tmpFile, new File(filePath, fileName + "_" + System.currentTimeMillis()/1000 + fileExt + ".bak"));
              }
              Files.write(rspStr, tmpFile, Charset.defaultCharset());

              //如果选择下载gzip的压缩文件
              if(tar_type.equals("gzip")) {
                  FileInputStream in = new FileInputStream(tmpFile);
                 File tarFile = new File(filePath, fileName + tarExt);//后缀.gz前面加上.txt，使压缩文件中的内容有后缀名.txt, 解压缩后才是.txt的文本文件
                  if (tarFile.exists()) {
                      Files.move(tarFile, new File(filePath, fileName + "_" + System.currentTimeMillis()/1000 + tarExt + ".bak"));
                  }
                  GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(tarFile));
                  byte[] buf = new byte[1024];
                  int len = 0;
                  while ((len = in.read(buf,0,buf.length)) > 0) {
                      out.write(buf, 0, len);
                  }
                  in.close();
                  out.flush();
                  out.close();
                  tmpFile.delete();//删除原来生成的txt文件
              }
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  /**
   * Method main
   * Description 说明：
   *
   * @param args 说明：
   */
  public static void main(String[] args) {
      new DownloadBillDemo().downloadBill("2017-12-15", "");
  }


}
