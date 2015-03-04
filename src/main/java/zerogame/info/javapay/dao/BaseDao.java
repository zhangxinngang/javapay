/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.dao;

/**
 *
 * @author zhangxingang
 */
import com.zerogame.eviver.storage.Key;
import com.zerogame.eviver.storage.Storage;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by fanngyuan on 4/15/14.
 */
public abstract class BaseDao<PK extends Key,T extends Key> implements Storage<PK,T> {
    JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public abstract T add(T t);

    public abstract T get(PK t);

    public abstract Map<PK,T> multiGet(List<PK> ids) ;

    public abstract void update(T t);

    public abstract void update(T oldT, T newT) ;

    public abstract void delete(PK key) ;

}

