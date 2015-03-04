/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.web;

import com.alibaba.fastjson.JSON;
import javax.sql.DataSource;
import javax.ws.rs.core.MediaType;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import zerogame.info.javapay.dao.UserDao;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

/**
 *
 * @author zhangxingang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations={"classpath:context.xml","classpath:ds-context.xml" })
public class PayOrderCallBackTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    private UserDao userDao;
    //private PayOrderDao payOrderDao;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    private JdbcTemplate template;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }
    
    @Before
    public void cleanData() {
        String script = "classpath:ds-cleanup.sql";
        Resource resource = wac.getResource(script);
        JdbcTestUtils.executeSqlScript(template, resource, true);
    }
    
    @Test
    public void testPP() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/pp")
                .param("app_id", "app_id")
                .param("order_id","2013032813296437")
                .param("billno","PP-1232-123123213-1-12")
                .param("account","wpfun")
                .param("amount", "10")
                .param("status", "0")
                .param("roleid", "1232")
                .param("zone", "12")
                .param("sign",""))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"success");
    }
    
    @Test
    public void testTBT() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/pay/tongbutui")
                .param("source", "tongbutui")
                .param("trade_no","TBT-1232-123123213-1-12")
                .param("amount","600")
                .param("paydes","{\"server_id\":\"1\",\"uin\":\"1\"}")
                .param("partner","1232")
                .param("sign","b6861b808f285a2e90acc62f8d8820bd")
                .param("debug","")
                .param("tborder",""))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(TestUtils.getTBTStatus(result.getResponse().getContentAsString()),"success");
    }
    
    @Test
    public void testITools() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/itools")
                .param("sign", "I4cXlMEQ7wSMClT2Skogehxi9Fh2iOR6JTDGoTUtfS7s4hra5t1mi"
                        + "x0MYnL+++d4LrEbK+CYXDcKOPg+nvNl+AWoLa7jNlb574LXeemrhKnSkj"
                        + "1Ei3c9OGJjtaaY6xfFDma9mxJwygZoOGimK5/wNemISUkEnui/CNgbweCTcb4=")
                .param("notify_data","XNTMkywjZJL1QogHxmy06e+nukjW+OlaBCeR5Qeapz3KIy"
                        + "oIF+jcDlGdUBIF+bXniY79vWfhKefzI/EU4h3HtvVgUEQMnsYaTEOy44P"
                        + "05kHUD6JWphYlEFbbMwlV0jVdq1xQTMImQS1PCdbJ7Czj7jbAINumoP7o"
                        + "qvzxgM9iUpRcZ4aLFc5avWoX7PgEliwHwWSm3GyatRPSBSZSQVFi964t9"
                        + "syduTQxrsCgzDoI1jfXPXgUr6CKQ4ZBWLVaI72meCRPpjmDBmk9oHmN9g"
                        + "pMy6I5hwoaOhWZOXU3tym1afhsKJ/nfKrpyFLnYebM9SQdrnyKb+oxJXMSpbFQrSJGWA=="))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"success");
    }

    @Test
    public void testWandoujia() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/wandoujia")
                .param("sign", "I4cXlMEQ7wSMClT2Skogehxi9Fh2iOR6JTDGoTUtfS7s4hra5t1mi")
                .param("signType","RSA")
                .param("content","{\"timeStamp\":\"123\",\"orderId\":\"123\",\"money\":\"64800\",\"chargeType\":\"ALIPAY\",\"appKeyId\":\"123456789\",\"buyerId\":\"123456\",\"out_trade_no\":\"WDJ-12-123123213-1-1\",\"cardNo\":\"123\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"success");
    }
    
    
    @Test
    public void testAISI() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/aisi")
                .param("app_id", "app_id")
                .param("order_id", "2013032813296437")
                .param("billno", "AISI-1232-123123213-1-12")
                .param("account", "wpfun")
                .param("amount", "10")
                .param("status", "0")
                .param("role", "1232")
                .param("zone", "12")
                .param("sign", "sign"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"success");
    }
    
    @Test
    public void testDangle() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/pay/dangle")
                .param("ext", "DANGLE-1232-123123213-1-12")
                .param("time", "123123213")
                .param("result", "1")
                .param("order", "order")
                .param("money", "10")
                .param("signature", "861a97acf8e822e6703b75d7cfc10927")
                .param("mid", "1111"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"success");
    }
    
    @Test
    public void testMuzhiwan() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/pay/muzhiwan")
                .param("appkey","test")
                .param("orderID","242323")
                .param("productDesc","sds")
                .param("uid","1232")
                .param("sign","f3470667ba80eb1d0eb5958367107cc5")
                .param("productName","goods")
                .param("productID","3434")
                .param("money","100")
                .param("extern","1"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"SUCCESS");
    }
    
    @Test
    public void testIPay() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/ipay")
                .param("exorderno","aa")
                .param("transid","2013032813296437")
                .param("appid","appid")
                .param("waresid","10")
                .param("feetype","1111")
                .param("money","10")
                .param("count","12")
                .param("result","0")
                .param("transtype","0")
                .param("transtime","2014-12-24 22:11:11")
                .param("cpprivate","IPAY-accountId-123123213-1-1")
                .param("paytype","1"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"SUCCESS");
    }
    
    @Test
    public void testBaidu() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/baidu")
                .param("cardtype"," 1")
                .param("orderid","2013032813296437")
                .param("aid","BAIDU-123123123-123123213-1-1")
                .param("amount","10")
                .param("result","0")
                .param("timestamp","123123213")
                .param("client_secret","12"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"SUCCESS");
    }
    
    @Test
    public void testOPPO() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/oppo")
                .param("notifyId","1")
                .param("productDesc","2013032813296437")
                .param("attach","OPPO-accountId-123123213-1-1")
                .param("price","10")
                .param("count","0")
                .param("partnerOrder","123123213")
                .param("productName","12")
                .param("sign","aaa"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(JSON.parseObject(result.getResponse().getContentAsString()).get("result").toString(),"OK");
    }
    
    @Test
    public void testXYSDK() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/xysdk")
                .param("uid","aa")
                .param("orderid","2013032813296437")
                .param("extra","XY-accountId-123123213-1-1")
                .param("amount","10")
                .param("serverid", "1")
                .param("ts", "1111")
                .param("sign", "1232")
                .param("sig", "12"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"success");
    }
 
    @Test
    public void testYOUXIGONGCHANG() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/youxigongchang")
                .param("serial_number", "111111")
                .param("cp", "91")
                .param("timestamp", "11111111111")
                .param("result", "0")
                .param("server", "12")
                .param("product_id", "3")
                .param("game_orderno", "aaa")
                .param("amount", "100")
                .param("extend", "91-1232-11111111111-3-12")
                .param("sign", "9d62546a635691735744a4295ab2c172")
                .param("product_num","1"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        
        assertEquals(JSON.parseObject(result.getResponse().getContentAsString()).get("result").toString(),"0");
    }
    
    @Test
    public void testWFK() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/winphonewfk")
                .param("accountid", "4PnhJ67kBd1QF1WK00ZmgfYR93Y=")
                .param("goodid", "1")
                .param("serverid","")
                .param("tradeid", "50287169")
                .param("sign", "6a3ecec75368a78a929599fa701ba928"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"success");
    }
    
    @Test
    public void testDuandai() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/pay/duandai")
                .param("orderid","11502040011093807518-3265961")
                .param("channel","cyzyddyx3000")
                .param("merid","1055")
                .param("sign","413bf6ed5ede26bc25b4103e0da369c9")
                .param("yys","0")
                .param("feecode","200")
                .param("feetypeid","16")
                .param("ordertime","20150204205616")
                .param("privstr","451CCF57687B313CBCEF8B4BF9DC36A4-1")
                .param("feestatus","2")
                .param("imei","356261059034312"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"ok");
    }
    
    @Test
    public void testCMCC() throws Exception{
        String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SyncAppOrderReq xmlns=\"http://www.monternet.com/dsmp/schemas/\"><TransactionID>CSSP16122856</TransactionID><MsgType>SyncAppOrderReq</MsgType><Version>1.0.0</Version><Send_Address><DeviceType>200</DeviceType><DeviceID>CSSP</DeviceID></Send_Address><Dest_Address><DeviceType>400</DeviceType><DeviceID>SPSYN</DeviceID></Dest_Address><OrderID>11130619144434998192</OrderID><CheckID>0</CheckID><ActionTime>20130619144435</ActionTime><ActionID>1</ActionID><MSISDN></MSISDN><FeeMSISDN>ECAD2EVFADF3AE2A</FeeMSISDN><AppID>300001489326</AppID><PayCode>30000148932601</PayCode><TradeID>L0IF7AF2J4L5IF1B</TradeID><Price>100</Price><TotalPrice>100</TotalPrice><SubsNumb>1</SubsNumb><SubsSeq>1</SubsSeq><ChannelID>2000000032</ChannelID><ExData>10000000-1</ExData><OrderType>1</OrderType><MD5Sign>ABCDEFGHIKDFIEJFLAKDJFSIDF</MD5Sign></SyncAppOrderReq>";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cmcc/mmiap").contentType(org.springframework.http.MediaType.APPLICATION_XML)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print()) 
                .andReturn();
        //assertTrue(HttpMessageNotReadableException.class.isAssignableFrom(result.getResolvedException().getClass()));
        //assertEquals(result.getResponse().getContentAsString(),"ok");
        /*
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cmcc/mmiap")
                .param("MsgType", "aa")
                .param("Version","aa")
                .param("Send_Address","aa")
                .param("Dest_Address", "aa")
                .param("OrderID", "AA")
                .param("CheckID", "AA")
                .param("TradeID","AA")
                .param("Price", "aa")
                .param("ActionTime", "aa")
                .param("ActionID", "aa")
                .param("MSISDN", "AA")
                .param("FeeMSISDN", "AA")
                .param("AppID","aa")
                .param("ProgramID", "AA")
                .param("PayCode", "aa")
                .param("TotalPrice", "aa")
                .param("SubsNumb","aa")
                .param("SubsSeq","aa")
                .param("ChannelID","AA")
                .param("ExData", "aa")
                .param("aa","aa"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"ok");
        */
    }
    
}
