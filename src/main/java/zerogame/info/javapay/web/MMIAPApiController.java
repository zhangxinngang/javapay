/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.web;


import com.alibaba.fastjson.JSON;

import java.io.ByteArrayInputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import me.gall.ten.control.api.bean.SyncAppOrderReq;
import me.gall.ten.control.api.bean.SyncAppOrderResp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.util.Pool;
import zerogame.info.javapay.dao.PayOrderDao;
import zerogame.info.javapay.dao.UserDao;
import zerogame.info.javapay.entity.PayOrder;
import zerogame.info.javapay.entity.Player;
/**
 *
 * @author zhangxingang
 */
@Controller
@RequestMapping("/cmcc/")
public class MMIAPApiController {
    private Logger log = Logger.getLogger(getClass());

    private static final Logger logger = Logger.getLogger(MMIAPApiController.class);
    /**
     * MM¼Æ·Ñ¶©µ¥½á¹ûÍ¨Öª½Ó¿Ú
     * 
     * @author kimi
     * @dateTime 2012-6-18 ÏÂÎç8:21:33
     * @param result
     * @param request
     * @param response
     * @param model
     * @return ¿ª·¢Õß·þÎñÆ÷ -> M-MarketÆ½Ì¨ Ó¦´ð½á¹û
     * @throws Exception
     */
    private final int CHANNEL_CMCC = 103;
    private final int PAY_ORDER_TYPE_COMMON = 0;
    private final int PAY_ORDER_TYPE_SANDBOX = 1;


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


    @RequestMapping(value = "mmiap", method = RequestMethod.POST,consumes = "application/xml")
    @ResponseBody
    protected SyncAppOrderResp mmiap(@RequestBody String resultString, HttpServletRequest request,
                    HttpServletResponse response, Map<String, Object> model) throws Exception {
        if (null == resultString || "".equals(resultString)) {
                response.setStatus(400);
                return null;
        }
        JAXBContext context = JAXBContext.newInstance(SyncAppOrderReq.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        SyncAppOrderReq result=(SyncAppOrderReq)unmarshaller.unmarshal(new ByteArrayInputStream(resultString.getBytes()));
        logger.info(String.format("cmcc pay order MsgType=%s, Version=%s,OrderID=%s,CheckID=%s,TradeID=%s,Price=%s,ActionTime=%s,"
                + "ActionID=%s,MSISDN=%s,FeeMSISDN=%s,AppID=%s,ProgramID=%s,PayCode=%s,TotalPrice=%s,SubsNumb=%s,SubsSeq=%s,"
                + "ChannelID=%s,ExData=%s",
        result.getMsgType(), result.getVersion(), result.getOrderID(), result.getCheckID(), result.getTradeID(),result.getPrice(),result.getActionTime(),
        result.getActionID(),result.getMSISDN(),result.getFeeMSISDN(),result.getAppID(),result.getProgramID(),result.getPayCode(),result.getTotalPrice(),result.getSubsNumb(),result.getSubsSeq(),
        result.getChannelID(),result.getExData()));
        //log.info(JSONObject.fromObject(result).toString());
        SyncAppOrderResp syncAppOrderResp = new SyncAppOrderResp();
        syncAppOrderResp.setMsgType("SyncAppOrderResp");
        syncAppOrderResp.setVersion("1.0.0");
        
        String[] billNos = result.getExData().split("-");
        String accountId = billNos[0];
        String goodId = billNos[1];

        Player player = userDao.getPlayer(CHANNEL_CMCC, accountId, "1");
        if(player == null){
            logger.warn("no user");
            syncAppOrderResp.sethRet(1);
            return syncAppOrderResp;
        }

        PayOrder payorder = addPayOrder(player.getUin(),accountId,result.getOrderID(),goodId,Integer.valueOf(Integer.valueOf(result.getPrice())/100),CHANNEL_CMCC,Integer.valueOf("1"),PAY_ORDER_TYPE_COMMON,0,player.getLevel(),player.getVip());
        if (payorder == null){
            logger.warn("add payorder fail!");
            syncAppOrderResp.sethRet(1);
            return syncAppOrderResp;
        }

        MultiKeyCommands jedis = jedisPool.getResource();
        jedis.publish("tap_hero_1_1", "{\"type\":101,\"uin\":\""+String.valueOf(player.getUin())+"\"}");
        jedisPool.returnResource(jedis);
        syncAppOrderResp.sethRet(0);
        return syncAppOrderResp;
    }
    
    @RequestMapping(value = "aa", method = RequestMethod.POST)
    @ResponseBody
    protected String test(@RequestParam("aa") String aa){
        logger.info(aa);
        MultiKeyCommands jedis = jedisPool.getResource();
        jedis.publish("tap_hero_1_1", "{\"type\":101,\"uin\":\""+String.valueOf(11)+"\"}");
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
