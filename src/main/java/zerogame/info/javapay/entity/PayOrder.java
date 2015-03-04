/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.entity;

/**
 *
 * @author zhangxingang
 */
public class PayOrder extends IntPK{
    private long uin;
    private String accountId;
    private String orderId;
    private String productName;
    private String productId;
    private String productDesc;
    private int money;
    private int channel;
    private int status;
    private int serverId;
    private int orderType;
    private int userLevel;
    private int userVipLevel;

    /**
     * @return the uin
     */
    public long getUin() {
        return uin;
    }

    /**
     * @param uin the uin to set
     */
    public void setUin(long uin) {
        this.uin = uin;
    }

    /**
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return the productDesc
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * @param productDesc the productDesc to set
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    /**
     * @return the money
     */
    public int getMoney() {
        return money;
    }

    /**
     * @param money the money to set
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * @return the channel
     */
    public int getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(int channel) {
        this.channel = channel;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the serverId
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * @param serverId the serverId to set
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    /**
     * @return the orderType
     */
    public int getOrderType() {
        return orderType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    /**
     * @return the userLevel
     */
    public int getUserLevel() {
        return userLevel;
    }

    /**
     * @param userLevel the userLevel to set
     */
    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    /**
     * @return the userVipLevel
     */
    public int getUserVipLevel() {
        return userVipLevel;
    }

    /**
     * @param userVipLevel the userVipLevel to set
     */
    public void setUserVipLevel(int userVipLevel) {
        this.userVipLevel = userVipLevel;
    }
}
