package zerogame.info.javapay.web;

import com.alibaba.fastjson.JSON;
import com.kuaiyong.pay.util.Base64;
import com.kuaiyong.pay.util.RSAEncrypt;
import com.kuaiyong.pay.util.Util;
import com.muzhiwan.tools.MuzhiwanSDKUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.util.Pool;
import zerogame.info.javapay.dao.PayOrderDao;
import zerogame.info.javapay.dao.UserDao;
import zerogame.info.javapay.entity.IToolsNotify;
import zerogame.info.javapay.entity.PayOrder;
import zerogame.info.javapay.entity.Player;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zhangxingang
 */
@Controller
@Service
@RequestMapping(value = "/v1/pay")
public class OrderPayWebCallBack {
    private static final Logger logger = Logger.getLogger(OrderPayWebCallBack.class);
    
    private final int PAY_ORDER_TYPE_COMMON = 0;
    private final int PAY_ORDER_TYPE_SANDBOX = 1;
    
    private final int CHANNEL_PP = 100007;
    private final int CHANNEL_TBT = 100009;
    private final int CHANNEL_ITOOLS = 100010;
    private final int CHANNEL_KUAIYONG = 100008;
    private final int CHANNEL_DANGLE = 18;
    private final int CHANNEL_360 = 11;
    private final int CHANNEL_WANDOUJIA = 12;
    private final int CHANNEL_AISI = 100013;
    private final int CHANNEL_MUZHIWAN = 19;
    private final int CHANNEL_IPAY = 997; //暂定997
    private final int CHANNEL_91 = 100011;
    private final int CHANNEL_BAIDU = 13;
    private final int CHANNEL_OPPO = 17;
    private final int CHANNEL_XYSDK = 100014;
    private final int CHANNEL_YOUXIGONGCHANG = 996;
    private final int CHANNEL_WINPHONE_WFK = 300001;
    private final int CHANNEL_DUANDAI = 101;
    
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private Pool<MultiKeyCommands> jedisPool;
    
    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Pool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(Pool jedisPool) {
        this.jedisPool = jedisPool;
    }
    
    @Autowired
    private PayOrderDao payOrderDao;
    
    public PayOrderDao getPayOrderDao() {
        return payOrderDao;
    }

    public void setPayOrderDao(PayOrderDao payOrderDao) {
        this.payOrderDao = payOrderDao;
    }
    
    @RequestMapping(value = "/pp", method = RequestMethod.POST)
    @ResponseBody
    public String ppPayOrder(@RequestParam("app_id") String appid,
            @RequestParam("order_id") String orderid,
            @RequestParam("billno") String billno,
            @RequestParam("account") String account,
            @RequestParam("amount") String amount,
            @RequestParam("status") String status,
            @RequestParam("roleid") String roleid,
            @RequestParam("zone") String zone,
            @RequestParam("sign") String sign) {
        logger.info("pppay order_id="+orderid+" billno="+billno+",account="+account+",amount="+amount+",status="+status+",app_id="+appid+",roleid="+roleid+",zone="+zone+",sign="+sign);
        
        String[] prodIds = billno.split("-");
        String serverid = prodIds[prodIds.length -1];
        String prodId = prodIds[prodIds.length - 2];
        Player player = userDao.getPlayer(CHANNEL_PP, roleid, serverid);
        if(player == null){
            return "fail";
        }
        PayOrder payorder = addPayOrder(player.getUin(),account,orderid,prodId,Integer.valueOf(amount),CHANNEL_PP,Integer.valueOf(serverid),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        
        if (payorder != null){
            return "success";
        }
        return "fail";
    }
    
    @RequestMapping(value = "/tongbutui", method = RequestMethod.GET)
    @ResponseBody
    public String tongbutuiPayOrder(@RequestParam("source") String source,
            @RequestParam("trade_no") String tradeNo,
            @RequestParam("amount") String amount,
            @RequestParam("partner") String partner,
            @RequestParam("paydes") String paydes,
            @RequestParam("debug") String debug,
            @RequestParam("tborder") String tborder,
            @RequestParam("sign") String sign){
        logger.info("tongbutui_pay source="+source+" trace_no="+tradeNo+",amount="+amount+",partner="+partner+",paydes="+paydes+",debug="+debug+",tborder="+tborder+",sign="+sign);
        String[] prodIds = tradeNo.split("-");
        String serverid = prodIds[prodIds.length -1];
        String prodId = prodIds[prodIds.length - 2];
        String accountId = prodIds[prodIds.length - 4];
        Player player = userDao.getPlayer(CHANNEL_TBT, accountId, serverid);
        if(player == null){
            return renderTBTResult("fail");
        }
        
        PayOrder payorder = addPayOrder(player.getUin(),accountId,tradeNo,prodId,Integer.valueOf(amount),CHANNEL_TBT,Integer.valueOf(serverid),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        
        //PayOrder payorder = payOrderDao.add(order);
        
        if (payorder != null){
            return renderTBTResult("success");
        }
        return renderTBTResult("fail");
    }
    
    private String renderTBTResult(String result){
        String msg = "{\"status\":\"" + result + "\"}";
        return msg;
    }
    
    @RequestMapping(value = "/itools", method = RequestMethod.POST)
    @ResponseBody
    public String itoolsPayOrder(@RequestParam("sign") String sign,
            @RequestParam("notify_data") String notifyData){
        logger.info("sign is "+sign+" notify is "+ notifyData);
        boolean verified = false;
        
        String notifyJson = "";
        
        try{
            //公钥RSA解密后的json字符串
            //解密后的json格式: {"order_id_com":"渠道名-accountID-time-goodid-serverid","user_id":"10010","amount":"0.10","account":"test001","order_id":"2013050900000713","result":"success"}
            
            notifyJson = RSASignature.decrypt(notifyData);
            //公钥对数据进行RSA签名校验
            verified = RSASignature.verify(notifyJson, sign);
            logger.info("notify json is "+notifyJson+" verified is "+verified);
            if (verified) {
                IToolsNotify iToolsNotify = JSON.parseObject(notifyJson, IToolsNotify.class);
                String[] params=iToolsNotify.getOrderIdCom().split("-");
                String channelId =params[0];
                String accountId = params[1];
                String time=params[2];
                String goodId=params[3];
                String serverId=params[4];
                Player user=userDao.getPlayer(CHANNEL_ITOOLS,accountId,serverId);
                logger.info("user is " +user.getUin());
                if (user!=null){
                    PayOrder payOrder= new PayOrder();
                    payOrder.setUin(user.getUin());
                    payOrder.setAccountId(accountId);
                    payOrder.setChannel(CHANNEL_ITOOLS);
                    payOrder.setOrderId(iToolsNotify.getOrderIdCom());
                    payOrder.setProductId(goodId);
                    payOrder.setServerId(Integer.valueOf(serverId));
                    float money= Float.valueOf(iToolsNotify.getAmount());
                    payOrder.setMoney(Math.round(money));
                    logger.info(payOrder.getAccountId());
                    this.payOrderDao.add(payOrder);
                }else{
                    return "fail";
                }
            }
        }catch (Exception e) {
            logger.warn("pay failed.",e);
        }
        if (verified) {
            //签名校验成功, 可以解析notifyJson, 做逻辑处理, 比如给相应用户增加宝石
            return "success";
        } else {
            return "fail";
        }
    }
    
    @RequestMapping(value = "/kuaiyong", method = RequestMethod.POST)
    @ResponseBody
    public String kuaiyongPayOrder(@RequestParam("uid") String uid,
            @RequestParam("notify_data") String notify_data,
            @RequestParam("orderid") String orderid,
            @RequestParam("sign") String sign,
            @RequestParam("dealseq") String dealseq,
            @RequestParam("subject") String subject,
            @RequestParam("v") String v){
        logger.info("kuaiyong pay notigydata="+notify_data+",orderid="+orderid+",dealseq="+dealseq+",uid="+uid+",subject="+subject+",version="+v+",sign="+sign);
        Map<String, String> transformedMap = new HashMap<String, String>();
        transformedMap.put("notify_data", notify_data);
        transformedMap.put("orderid", orderid);
        transformedMap.put("sign", sign);
        transformedMap.put("dealseq", dealseq);
        transformedMap.put("uid", uid);
        transformedMap.put("subject", subject);
        transformedMap.put("v", v);
        String signData = Util.getSignData(transformedMap);
        
        String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPXB7aWe9IUh2wz0MyOqxwk3ujF5qmmRzOL4kwfVPVsnEG8d2lSbo+S0/Xm7sivsR1l/LsGWAuoWGLF0bFO5Zm+oh5W6rexuh+mJgAhZfSzrIAgD7QJIZ2TOzQFeCki3xor+62RmEqjePYWJpP0pStVexMZzFaRRFRiYXWMVCeYQIDAQAB";
        if (!com.kuaiyong.pay.util.RSASignature.doCheck(signData, sign, rsaPublicKey, "utf-8")) {
       	//RSA验签失败，数据不可信
            //开发商业务逻辑处理
            //响应给快用
            logger.warn("RSA验签失败，数据不可信");
            return "failed";
        } else {

            //"RSA验签成功，数据可信
            RSAEncrypt rsaEncrypt = new RSAEncrypt();

            //加载公钥   
            try {
                rsaEncrypt.loadPublicKey(rsaPublicKey);
                //加载公钥成功
                logger.warn("加载公钥成功");
            } catch (Exception e) {
                //加载公钥失败
                logger.warn("加载公钥失败");
            }

            //公钥解密通告加密数据
            byte[] dcDataStr = Base64.decode(notify_data);
            byte[] plainData;
            try {
                plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), dcDataStr);
            }catch (Exception e){
                logger.warn("解密失败");
                return "failed";
            }
            //获取到加密通告信息
            String notifyData;
            try{
                notifyData = new String(plainData, "UTF-8");
            }catch(Exception e){
                logger.info(e);
                return "failed";
            }
            logger.info("sign:"+notifyData);
            String[] data = notifyData.split("&");
            String fee = data[1].split("=")[1];//从解密的DATA中获得支付金额  fee

           //开发商业务逻辑处理
            String[] params = dealseq.split("-");
            String channelId = params[0];
            String accountId = params[1];
            String time = params[2];
            String goodId = params[3];
            String serverId = params[4];
            Player user = userDao.getPlayer(CHANNEL_KUAIYONG, accountId, serverId);
            //logger.info("user is " +user.toString());
            if (user != null) {
                PayOrder payOrder = new PayOrder();
                payOrder.setUin(user.getUin());
                payOrder.setAccountId(accountId);
                payOrder.setChannel(CHANNEL_KUAIYONG);
                payOrder.setOrderId(orderid);
                payOrder.setProductId(goodId);
                payOrder.setServerId(Integer.valueOf(serverId));
                float money = Float.valueOf(fee);
                payOrder.setMoney(Math.round(money));
                //logger.info(payOrder);
                this.payOrderDao.add(payOrder);
            }

            //响应给快用
            return "success";
        }
    }
    
    @RequestMapping(value = "/wandoujia", method = RequestMethod.POST)
    @ResponseBody
    public String wandoujiaPayOrder(@RequestParam("signType") String signType,
            @RequestParam("sign") String sign,
            @RequestParam("content") String content){
        logger.info("pay wandoujia content:"+content+" signType:"+signType+" sign:"+sign);
        String money = JSON.parseObject(content).get("money").toString();
        String outTradeNo = JSON.parseObject(content).get("out_trade_no").toString();
        
        String[] tokens = outTradeNo.split("-");
        String accountId = tokens[1];
        String serverId = tokens[4];
        String productId = tokens[3];
        
        Player player = userDao.getPlayer(CHANNEL_WANDOUJIA, accountId, serverId);
        if(player == null){
            return "fail";
        }
        //PayOrder payorder = payOrderDao.add(order);
        PayOrder payorder = addPayOrder(player.getUin(),accountId,outTradeNo,productId,Integer.valueOf(money)/100,CHANNEL_WANDOUJIA,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder != null){
            return "success";
        }
        return "fail";
    }
    
    @RequestMapping(value = "/aisi", method = RequestMethod.POST)
    @ResponseBody
    public String aisiPayOrder(@RequestParam("sign") String sign,
            @RequestParam("order_id") String orderId,
            @RequestParam("billno") String billNo,
            @RequestParam("account") String account,
            @RequestParam("amount") String amount,
            @RequestParam("status") String status,
            @RequestParam("app_id") String appid,
            @RequestParam("role") String accountId,
            @RequestParam("zone") String zone){
        logger.info("aisi pay order_id="+orderId+" billno="+billNo+",account="+account+",amount="+amount+",status="+status+",appid="+appid+",roleid="+accountId+",zone="+zone+",sign="+sign);
        String[] prodIds = billNo.split("-");
        String serverId = prodIds[prodIds.length-1];
        String prodId = prodIds[prodIds.length-2];
        
        Player player = userDao.getPlayer(CHANNEL_AISI, accountId, serverId);
        if(player == null){
            return "fail";
        }
        PayOrder payorder = addPayOrder(player.getUin(),accountId,billNo,prodId,Integer.valueOf(amount),CHANNEL_AISI,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder != null){
            return "success";
        }
        return "fail";
    }
    
    @RequestMapping(value = "/dangle", method = RequestMethod.GET)
    @ResponseBody
    public String danglePayOrder(@RequestParam("ext") String billNo,
            @RequestParam("time") String time,
            @RequestParam("result") String result,
            @RequestParam("money") String money,
            @RequestParam("order") String order,
            @RequestParam("signature") String sign,
            @RequestParam("mid") String mid){
        String merchantId = "846";
        String payKey = "1hW5KGDiYptZ";
        String[] billno =billNo.split("-");
        String prodId = billno[billno.length-2];
        String serverId = billno[billno.length-1];
        String accountId = billno[1];
        if (result != "1"){
            logger.info("result is not 1!");
            return "fail";
        }
        Player player = userDao.getPlayer(CHANNEL_DANGLE, accountId, serverId);
        if(player == null){
            return "fail";
        }
        logger.info(String.format("dangle pay order=%s,money=%s,mid=%s,time=%s,result=%s,ext=%s,key=%s,signature=%s", order, money, mid, time, result, billNo, payKey,sign));
        String str = String.format("order=%s&money=%s&mid=%s&time=%s&result=%s&ext=%s&key=%s", order, money, mid, time, result, billNo, payKey); 

        if(!WebUtils.string2MD5(str).equals(sign)){
            logger.info("sign is wrong");
            return "fail";
        }
        PayOrder payorder = addPayOrder(player.getUin(),accountId,billNo,prodId,Integer.valueOf(money),CHANNEL_DANGLE,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder != null){
            return "success";
        }
        return "fail";
    }
    
    @RequestMapping(value = "/muzhiwan", method = RequestMethod.GET)
    @ResponseBody
    public String muzhiwanPayOrder(@RequestParam("appkey") String appKey,
            @RequestParam("orderID") String orderId,
            @RequestParam("productName") String productName,
            @RequestParam("productDesc") String productDesc,
            @RequestParam("productID") String productId,
            @RequestParam("uid") String uid,
            @RequestParam("extern") String serverId,  //extern :serverid
            @RequestParam("money") String money,
            @RequestParam("sign") String sign){
        String signStr;
        
        try{
            signStr = MuzhiwanSDKUtils.sign(appKey, orderId, productName, productDesc, productId, money, uid, serverId);
        }catch(Throwable t){
            logger.info(t);
            return "failed";
        }
        if(!sign.equals(signStr)){
            logger.info("sign is wrong"+sign+"  "+signStr);
            return "failed";
        }
        Player player = userDao.getPlayer(CHANNEL_MUZHIWAN, uid, serverId);
        if(player == null){
            return "fail";
        }
        PayOrder payorder = addPayOrder(player.getUin(),uid,orderId,productId,Integer.valueOf(money),CHANNEL_MUZHIWAN,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "fail";
        }
        
        return "SUCCESS";
    }
    
    @RequestMapping(value = "/ipay", method = RequestMethod.POST)
    @ResponseBody
    public String ipayPayOrder(@RequestParam("exorderno") String exorderno,
            @RequestParam("transid") String transid,
            @RequestParam("appid") String appid,
            @RequestParam("waresid") String waresid,
            @RequestParam("feetype") String feetype,
            @RequestParam("money") String money,
            @RequestParam("count") String count,
            @RequestParam("result") String result,
            @RequestParam("transtype") String transtype,
            @RequestParam("cpprivate") String cpprivate,
            @RequestParam("paytype") String paytype){
        String[] prodIds = cpprivate.split("-");
        String prodId = prodIds[prodIds.length-2];
        String accountId = prodIds[prodIds.length-4];
        String serverId = prodIds[prodIds.length-1];
        Player player = userDao.getPlayer(CHANNEL_IPAY, accountId, serverId);
        if(player == null){
            return "FAILURE";
        }
        PayOrder payorder = addPayOrder(player.getUin(),accountId,transid,prodId,Integer.valueOf(money),CHANNEL_IPAY,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "FAILURE";
        }
        return "SUCCESS";
    }
    
    @RequestMapping(value = "/baidu", method = RequestMethod.POST)
    @ResponseBody
    public String baiduPayOrder(@RequestParam("aid") String billNo,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("result") String result,
            @RequestParam("amount") String amount,
            @RequestParam("cardtype") String cardType,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("orderid") String orderId){
        String[] prodIds = billNo.split("-");
        String prodId = prodIds[prodIds.length-2];
        String serverId = prodIds[prodIds.length-1];
        String accountId = prodIds[1];
        String APP_SECRET = "";
        logger.info(String.format("oppo pay aid=%s,timestamp=%s,result=%s,amount=%s,cardtype=%s,client_secret=%s,orderid=%s", billNo, timestamp, result, amount, cardType, clientSecret, orderId));
        
        Player player = userDao.getPlayer(CHANNEL_BAIDU, accountId, serverId);
        if (player == null){
            return "ERROR_FAIL";
        }
        String str = String.format("%s%s%s%s%s%s%s", amount,cardType,orderId,result,timestamp,APP_SECRET,billNo);
        String authStr = WebUtils.string2MD5(str);
        if (authStr.equals(str)){
            logger.info(String.format("AUTH IS WRONE %s %s",authStr,str));
            return "ERROR_FAIL";
        }
        
        PayOrder payorder = addPayOrder(player.getUin(),accountId,billNo,prodId,Integer.valueOf(amount),CHANNEL_BAIDU,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "ERROR_FAIL";
        }
        return "SUCCESS";
    }
    
    @RequestMapping(value = "/oppo", method = RequestMethod.POST)
    @ResponseBody
    public String oppoPayOrder(@RequestParam("notifyId") String notifyId,
            @RequestParam("attach") String attach,
            @RequestParam("price") String price,
            @RequestParam("count") String count,
            @RequestParam("partnerOrder") String partnerOrder,
            @RequestParam("productName") String productName,
            @RequestParam("productDesc") String productDesc,
            @RequestParam("sign") String sign){
        logger.info(String.format("oppo pay notigyId=%s,attach=%s,price=%s,count=%s,partnerOrder=%s,productName=%s,productDesc=%s,sign=%s", notifyId, attach, price, count, partnerOrder, productName, productDesc,sign));
        
        String[] prodIds = attach.split("-");
        String prodId = prodIds[prodIds.length - 2];
        String serverId = prodIds[prodIds.length - 1];
        String accountId = prodIds[prodIds.length - 4];
        
        Player player = userDao.getPlayer(CHANNEL_OPPO,accountId,serverId);
        if(player == null){
            return "{\"result\":\"Fail\"}";
        }
        
        PayOrder payorder = addPayOrder(player.getUin(),accountId,notifyId,prodId,Integer.valueOf(price),CHANNEL_OPPO,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "{\"result\":\"Fail\"}";
        }
        
        return "{\"result\":\"OK\"}";
    }
    
    @RequestMapping(value = "/xysdk", method = RequestMethod.POST)
    @ResponseBody
    public String xysdkPayOrder(@RequestParam("uid") String uid,
            @RequestParam("orderid") String orderid,
            @RequestParam("extra") String extra,
            @RequestParam("serverid") String serverId,
            @RequestParam("amount") String amount,
            @RequestParam("ts") String ts,
            @RequestParam("sign") String sign,
            @RequestParam("sig") String sig){
        logger.info(String.format("xysdk pay orderid= %s uid=%s serverid=%s amount=%s extra=%s ts=%s sign=%s sig=%s",
		orderid, uid, serverId, amount, extra, ts, sign, sig));
        String[] billNo = extra.split("-");
        String prodId = billNo[billNo.length - 2];
        String accountId = billNo[billNo.length - 4];
        Player player = userDao.getPlayer(CHANNEL_XYSDK, accountId, serverId);
        if (player == null){
            return "fail";
        }
        PayOrder payorder = addPayOrder(player.getUin(),accountId,extra,prodId,Integer.valueOf(amount),CHANNEL_XYSDK,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "fail";
        }
        
        return "success";
    }
    
    @RequestMapping(value = "/youxigongchang", method = RequestMethod.POST)
    @ResponseBody
    public String youxigongchangPayOrder(@RequestParam("serial_number") String SerialNumber,
            @RequestParam("cp") String cp,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("result") String result,
            @RequestParam("extend") String extend,
            @RequestParam("server") String server,
            @RequestParam("product_id") String productId,
            @RequestParam("product_num") String productNum,
            @RequestParam("game_orderno") String gameOrderno,
            @RequestParam("amount") String amount,
            @RequestParam("sign") String sign){
        logger.info(String.format("youxigongchangpay serial_number=%s, cp=%s,timestamp=%s,result=%s,extend=%s,server=%s,product_id=%s,product_num=%s,game_orderno=%s,amount=%s,sign=%s",
		SerialNumber, cp, timestamp, result, extend, server, productId,
		productNum, gameOrderno, amount, sign));
        Map<String, String> transformedMap = new HashMap<String, String>();
        transformedMap.put("serial_number", SerialNumber);
        transformedMap.put("cp", cp);
        transformedMap.put("timestamp", timestamp);
        transformedMap.put("result", result);
        transformedMap.put("extend", extend);
        transformedMap.put("server", server);
        transformedMap.put("product_id", productId);
        transformedMap.put("product_num", productNum);
        transformedMap.put("game_orderno", gameOrderno);
        transformedMap.put("amount", amount);
        transformedMap.put("sign", sign);
        String authSign = WebUtils.getSignData(transformedMap);
        String auth = WebUtils.string2MD5(authSign);
        if (!auth.equals(sign)){
            logger.info("sign is wrong"+auth+" "+sign);
            return "{\"result\":\"1\",\"result_desc\":\"fail\"}";
        }
        String[] billNo = extend.split("-");
        String accountId = billNo[1];
        String prodId = billNo[billNo.length - 2];
        Player player = userDao.getPlayer(CHANNEL_YOUXIGONGCHANG, accountId, server);
        if(player == null){
            logger.info("no user");
            return "{\"result\":\"1\",\"result_desc\":\"fail\"}";
        }
        PayOrder payorder = addPayOrder(player.getUin(),accountId,extend,prodId,Integer.valueOf(amount),CHANNEL_YOUXIGONGCHANG,Integer.valueOf(server),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "{\"result\":\"1\",\"result_desc\":\"fail\"}";
        }
        return "{\"result\":\"0\",\"result_desc\":\"ok\"}";
    }
    
    @RequestMapping(value = "/360", method = RequestMethod.POST)
    @ResponseBody
    public String payOrder360(@RequestParam("pay_ext") String payExt,
            @RequestParam("amount") String amount){
        String[] billNo = payExt.split("-");
        String prodId = billNo[billNo.length -2];
        String serverId = billNo[billNo.length - 1];
        String accountId = billNo[1];
        Player player = userDao.getPlayer(CHANNEL_360, accountId, serverId);
        if(player == null){
            logger.info("no user");
            return "{\"error_code\":\"1\",\"result_desc\":\"支付失败\"}";
        }
        PayOrder payorder = addPayOrder(player.getUin(),accountId,payExt,prodId,Integer.valueOf(amount),CHANNEL_360,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "{\"error_code\":\"1\",\"result_desc\":\"支付失败\"}";
        }
        return "{\"error_code\":\"0\",\"result_desc\":\"支付成功\"}";
    }
    
    @RequestMapping(value = "/winphonewfk", method = RequestMethod.POST)
    @ResponseBody
    public String winphonewfkPayOrder(@RequestParam("accountid") String accountId,
            @RequestParam("goodid") String goodId,
            @RequestParam("serverid") String serverId,
            @RequestParam("tradeid") String tradeId,
            @RequestParam("sign") String sign){
        logger.info(String.format("winhphonewfk accountId=%s, goodid=%s,serverid=%s,tradeid=%s,sign=%s",
		accountId, goodId, serverId, tradeId, sign));
        String APPKEY = "3cffbb50485f5fbd";
        Map<String, String> transformedMap = new HashMap<String, String>();
        transformedMap.put("accountid", accountId);
        transformedMap.put("goodid", goodId);
        transformedMap.put("serverid", serverId);
        transformedMap.put("tradeid", tradeId);
        transformedMap.put("appkey", APPKEY);
        
        String authSign = WebUtils.getSignData(transformedMap);
        String auth = WebUtils.string2MD5(authSign);
        if (!auth.equals(sign)){
            logger.info("auth fail");
            return "fail";
        }
        if (serverId.equals("")){
            serverId = "1";
        }
        Player player = userDao.getPlayer(CHANNEL_WINPHONE_WFK, accountId, serverId);
        if(player == null){
            logger.info("no user");
            return "fail";
        }
        
        String orderId = String.format("WP_WFK-%s-%s-%s-%s", accountId,tradeId,goodId,serverId);
        PayOrder payorder = addPayOrder(player.getUin(),accountId,orderId,goodId,Integer.valueOf("0"),CHANNEL_WINPHONE_WFK,Integer.valueOf(serverId),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "fail";
        }
        
        return "success";
    }
    
    @RequestMapping(value = "/duandai", method = RequestMethod.POST)
    @ResponseBody
    public String duandaiPayOrder(@RequestParam("merid") String merId,
            @RequestParam("orderid") String orderid,
            @RequestParam("ordertime") String ordertime,
            @RequestParam("feecode") String feecode,
            @RequestParam("privstr") String privstr,
            @RequestParam("feestatus") String feeStatus,
            @RequestParam("sign") String sign){
        String DUANDAIMIYAO = "sfkdkfurvlrflkd2dk5jv";
        logger.info(String.format("duandai pay order merid=%s, orderid=%s,ordertime=%s,feecode=%s,privstr=%s,feestatus=%s,sign=%s",
		merId, orderid, ordertime, feecode, privstr,feeStatus,sign));
        String str = "&merid=" + merId + "&orderid=" + orderid + "&ordertime=" + ordertime + "&feecode=" + feecode + "&privstr=" + privstr + "&feestatus=" + feeStatus;
        String auth = WebUtils.string2MD5(DUANDAIMIYAO+str);
        if (!auth.equals(sign)){
            logger.warn("auth fail");
            return "fail";
        }
        String[] billNos = privstr.split("-");
        String accountId = billNos[0];
        String goodId = billNos[1];
        Player player = userDao.getPlayer(CHANNEL_DUANDAI, accountId, "1");
        if(player == null){
            logger.warn("no user");
            return "fail";
        }
        String orderId = orderid.split("-")[0];
        PayOrder payorder = addPayOrder(player.getUin(),accountId,orderId,goodId,Integer.valueOf(Integer.valueOf(feecode)/100),CHANNEL_DUANDAI,Integer.valueOf("1"),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            return "fail";
        }
        //''
        MultiKeyCommands jedis = jedisPool.getResource();
        jedis.publish("tap_hero_1_1", "{\"type\":101,\"uin\":\""+String.valueOf(player.getUin())+"\"}");
        jedisPool.returnResource(jedis);
        return "ok";
    }
    
    private PayOrder addPayOrder(long uin,String accountId,String orderid,String productid,int money,int channel,int serverid,int ordertype,int status,int level,int vip){
        PayOrder order = new PayOrder();
        order.setUin(uin);
        order.setAccountId(accountId);
        order.setOrderId(orderid);
        order.setProductId(productid);
        order.setMoney(money);
        order.setChannel(channel);
        order.setServerId(serverid);
        order.setOrderType(ordertype);
        order.setStatus(status);
        order.setUserLevel(level);
        order.setUserVipLevel(vip);
        order.setProductName("");
        order.setProductDesc("");
        
        return payOrderDao.add(order);
    }
    
}
