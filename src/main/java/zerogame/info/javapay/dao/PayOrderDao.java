/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import zerogame.info.javapay.entity.IntPK;
import zerogame.info.javapay.entity.PayOrder;
import zerogame.info.javapay.entity.Player;

/**
 *
 * @author zhangxingang
 */

public class PayOrderDao extends BaseDao<IntPK,PayOrder> {
    private static final Logger logger = Logger.getLogger(PayOrder.class);
    
    @Override
    public PayOrder add(final PayOrder payorder){
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            getTemplate().update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement ps = con.prepareStatement("insert into pay_order"
                                    + "(uin,account_id,order_id,product_id,product_name,product_desc,money,channel,serverId,status,order_type,create_time,user_level,vip_level) "
                                    + "values(?,?,?,?,?,?,?,?,?,?,?,now(),?,?)", Statement.RETURN_GENERATED_KEYS);
                            ps.setLong(1, payorder.getUin());
                            ps.setString(2, payorder.getAccountId());
                            ps.setString(3, payorder.getOrderId());
                            ps.setString(4, payorder.getProductId());
                            ps.setString(5, payorder.getProductName());
                            ps.setString(6, payorder.getProductDesc());
                            ps.setInt(7, payorder.getMoney());
                            ps.setInt(8, payorder.getChannel());
                            ps.setInt(9, payorder.getServerId());
                            ps.setInt(10, payorder.getStatus());
                            ps.setInt(11, payorder.getOrderType());
                            ps.setInt(12, payorder.getUserLevel());
                            ps.setInt(13, payorder.getUserVipLevel());
                            return ps;
                        }
                    }, keyHolder
            );
            return payorder;
        } catch (Exception e) {
            logger.warn("add pay order failed",e);
            return null;
        }
    }

    @Override
    public PayOrder get(IntPK t) {
        return null;
    }

    @Override
    public Map<IntPK, PayOrder> multiGet(List<IntPK> ids) {
        return null;
    }

    @Override
    public void update(final PayOrder PayOrder) {
            
    }
    
    @Override
    public void update(PayOrder oldT, PayOrder newT) {

    }

    @Override
    public void delete(final IntPK key){
         
    }


}
