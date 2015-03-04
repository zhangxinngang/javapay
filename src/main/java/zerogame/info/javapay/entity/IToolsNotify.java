package zerogame.info.javapay.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by fanngyuan on 6/12/14.
 */
public class IToolsNotify {

    //{"order_id_com":"渠道名-accountID-time-goodid-serverid","user_id":"10010","amount":"0.10","account":"test001","order_id":"2013050900000713","result":"success"}

    @JSONField(name = "order_id_com")
    private String orderIdCom;
    @JSONField(name="user_id")
    private String userId;
    private String amount;
    private String account;
    @JSONField(name="order_id")
    private String orderId;
    private String result;

    public String getOrderIdCom() {
        return orderIdCom;
    }

    public void setOrderIdCom(String orderIdCom) {
        this.orderIdCom = orderIdCom;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "IToolsNotify{" +
                "orderIdCom='" + orderIdCom + '\'' +
                ", userId='" + userId + '\'' +
                ", amount='" + amount + '\'' +
                ", account='" + account + '\'' +
                ", orderId='" + orderId + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
