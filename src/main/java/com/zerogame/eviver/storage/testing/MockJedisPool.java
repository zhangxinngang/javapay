/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zerogame.eviver.storage.testing;

import redis.clients.jedis.JedisCommands;
import redis.clients.util.Pool;

/**
 *
 * @author zhangxingang
 */
public class MockJedisPool extends Pool<JedisCommands> {
    
    private MockJedis redis = new MockJedis();
    
    public JedisCommands getResource() {
        return redis;
    }

    public void returnBrokenResource(JedisCommands resource) {
    }

    public void returnResource(JedisCommands resource) {
        
    }
}
