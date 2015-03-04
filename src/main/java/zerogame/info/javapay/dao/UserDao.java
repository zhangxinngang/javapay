/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.dao;

import zerogame.info.javapay.entity.IntPK;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.KeyHolder;
import zerogame.info.javapay.entity.Player;

/**
 *
 * @author zhangxingang
 */

public class UserDao extends BaseDao<IntPK,Player>{ 
    private static final Logger logger = Logger.getLogger(UserDao.class);
    
    @Override
    public Player add(final Player player){
        return null;
    }
    
    public Player add(final long uin,final int channel ,final String accountId,final String accountName) {
        Player user = new Player();
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            getTemplate().update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement ps = con.prepareStatement("insert into player(uin,channel,serverid,accountId,accountName) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                            ps.setString(1, String.valueOf(uin));
                            ps.setString(2, String.valueOf(channel));
                            ps.setString(3, accountId);
                            ps.setString(4, accountName);
                            return ps;
                        }
                    }, keyHolder
            );
            user.setUin(keyHolder.getKey().intValue());
            return user;
        } catch (Exception e) {
            logger.warn("add account failed",e);
            return null;
        }
    }

    @Override
    public Player get(IntPK t) {
        return null;
    }

    @Override
    public Map<IntPK, Player> multiGet(List<IntPK> ids) {
        return null;
    }

    @Override
    public void update(final Player user) {
            
    }
    
    @Override
    public void update(Player oldT, Player newT) {

    }

    @Override
    public void delete(final IntPK key){
         
    }

    public Player getPlayer(final int channel,final String accountId,final String serverId){
        try{
            Player user = this.template.queryForObject("select uin,vip,level from player where accountId=? and channel=? and serverid=?", new UserRowMapper(),accountId,channel,serverId);
            //Player user = this.template.queryForObject("select uin,vip,level from player where serverid=12", new UserRowMapper());
            
            return user;
        }catch(EmptyResultDataAccessException e){//
            logger.warn("get failed",e);
            return null;
        }
    }
}

class UserRowMapper implements RowMapper<Player>{

    @Override
    public Player mapRow(ResultSet rs, int i) throws SQLException {
        Player user = new Player();
        user.setUin(rs.getLong("uin"));
        user.setVip(rs.getInt("vip"));
        user.setLevel(rs.getInt("level"));
        return user;
    }
}
