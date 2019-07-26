  package com.scheduling.common;

  import java.math.BigDecimal;



  public class MathUtil
  {
    public static double add(double v1, double v2)
    {
      BigDecimal b1 = new BigDecimal(Double.toString(v1));
      BigDecimal b2 = new BigDecimal(Double.toString(v2));
      return b1.add(b2).doubleValue();
    }



    public static double subtract(double v1, double v2)
    {
      BigDecimal b1 = new BigDecimal(Double.toString(v1));
      BigDecimal b2 = new BigDecimal(Double.toString(v2));
      return b1.subtract(b2).doubleValue();
    }



    public static double multiply(double v1, double v2, int scale)
    {
      BigDecimal b1 = new BigDecimal(Double.toString(v1));
      BigDecimal b2 = new BigDecimal(Double.toString(v2));
      return b1.multiply(b2).doubleValue();
    }




    public static double divide(double v1, double v2)
    {
      int DEF_DIV_SCALE = 10;
      BigDecimal b1 = new BigDecimal(Double.toString(v1));
      BigDecimal b2 = new BigDecimal(Double.toString(v2));
      return b1.divide(b2, DEF_DIV_SCALE, 4).doubleValue();
    }




    public static BigDecimal divideBig(BigDecimal v1, BigDecimal v2)
    {
      int DEF_DIV_SCALE = 10;
      return v1.divide(v2, DEF_DIV_SCALE, 5);
    }



    public static double accuracyRoundHalfUp(double v, int scale)
    {
      if (scale < 0) {
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
      }
      BigDecimal b = new BigDecimal(Double.toString(v));
      BigDecimal one = new BigDecimal("1");
      return b.divide(one, scale, 4).doubleValue();
    }



    public static double accuracyRoundDown(double v, int scale)
    {
      if (scale < 0) {
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
      }
      BigDecimal b = new BigDecimal(Double.toString(v));
      BigDecimal one = new BigDecimal("1");
      return b.divide(one, scale, 1).doubleValue();
    }



    public static BigDecimal accuracyRoundDown(BigDecimal v, int scale)
    {
      if (scale < 0) {
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
      }
      BigDecimal one = new BigDecimal("1");
      return v.divide(one, scale, 1);
    }
  }

