package com.scheduling.entity;

/**
 * Class Payment
 * Description 支付方式实体类
 * Create 2017-04-26 19:12:03
 * @author Benny.YEE
 */
public class Payment {

    /**
     * Field paymentName
     * Description
     */
    private String paymentName;

    /**
     * Field category
     * Description
     */
    private String category;

    /**
     * Field fee
     * Description
     */
    private float fee;

    /**
     * Constructs Payment
     * Description
     *
     * @param paymentName 说明：
     * @param category 说明：
     * @param fee 说明：
     */
    public Payment(String paymentName, String category, float fee) {
        this.paymentName = paymentName;
        this.category    = category;
        this.fee         = fee;
    }

    /**
     * Method getPaymentName
     * Description 说明：
     *
     * @return 返回值说明：
     */
    public String getPaymentName() {
        return paymentName;
    }

    /**
     * Method setPaymentName
     * Description 说明：
     *
     * @param paymentName 说明：
     */
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    /**
     * Method getCategory
     * Description 说明：
     *
     * @return 返回值说明：
     */
    public String getCategory() {
        return category;
    }

    /**
     * Method setCategory
     * Description 说明：
     *
     * @param category 说明：
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Method getFee
     * Description 说明：
     *
     * @return 返回值说明：
     */
    public float getFee() {
        return fee;
    }

    /**
     * Method setFee
     * Description 说明：
     *
     * @param fee 说明：
     */
    public void setFee(float fee) {
        this.fee = fee;
    }

    /**
     * Method toString
     * Description 说明：
     *
     * @return 返回值说明：
     */
    @Override
    public String toString() {
        return "\"" + this.paymentName + "\":{\"category\":\"" + this.category + "\",\"fee\":\"" + this.fee + "\"}";
    //	return this.paymentName + ":{\"category\":\"" + this.category + "\",\"fee\":\"" + this.fee + "\"}";
    
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
