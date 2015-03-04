/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zerogame.eviver.storage;

import java.util.List;
import java.util.Map;

/**
 *
 * @author fanngyuan
 */
public interface Storage<PK extends Key,T extends Key> {

    public T get(PK id) ;
    
    public Map<PK,T> multiGet(List<PK> ids) ;

    public T add(T t) ;
    
    public void delete(PK key) ;

    public void update(T t) ;

    public void update(T oldT, T newT) ;
}
