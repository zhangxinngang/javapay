/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zerogame.eviver.storage.testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

/**
 *
 * @author zhangxingang
 */
public class MockJedis implements JedisCommands,MultiKeyCommands{
    private Map<String,Object> objectMap =new HashMap<>();
    
    private BlockingQueue queue= new ArrayBlockingQueue(10);
    
    public MockJedis(String host, int port) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        /* 0: aload_0
         * 1: aload_1
         * 2: iload_2
         * 3: invokespecial redis/clients/jedis/BinaryJedis."<init>":(Ljava/lang/String;I)V
         * 6: aload_0
         * 7: aconst_null
         * 8: putfield      redis/clients/jedis/Jedis.dataSource:Lredis/clients/util/Pool;
         * 11: return
         *  */
        // </editor-fold>
    }

    MockJedis() {
    }

    @Override
    public String set(String s, String s1) {
        return null;
    }

    @Override
    public String set(String s, String s1, String s2, String s3, long l) {
        return null;
    }

    @Override
    public String get(String s) {
        return null;
    }

    @Override
    public Boolean exists(String s) {
        return null;
    }

    @Override
    public Long persist(String s) {
        return null;
    }

    @Override
    public String type(String s) {
        return null;
    }

    @Override
    public Long expire(String s, int i) {
        return null;
    }

    @Override
    public Long expireAt(String s, long l) {
        return null;
    }

    @Override
    public Long ttl(String s) {
        return null;
    }

    @Override
    public Boolean setbit(String s, long l, boolean b) {
        return null;
    }

    @Override
    public Boolean setbit(String s, long l, String s1) {
        return null;
    }

    @Override
    public Boolean getbit(String s, long l) {
        return null;
    }

    @Override
    public Long setrange(String s, long l, String s1) {
        return null;
    }

    @Override
    public String getrange(String s, long l, long l1) {
        return null;
    }

    @Override
    public String getSet(String s, String s1) {
        return null;
    }

    @Override
    public Long setnx(String s, String s1) {
        return null;
    }

    @Override
    public String setex(String s, int i, String s1) {
        return null;
    }

    @Override
    public Long decrBy(String s, long l) {
        return null;
    }

    @Override
    public Long decr(String s) {
        return null;
    }

    @Override
    public Long incrBy(String s, long l) {
        return null;
    }

    @Override
    public Long incr(String s) {
        return null;
    }

    @Override
    public Long append(String s, String s1) {
        return null;
    }

    @Override
    public String substr(String s, int i, int i1) {
        return null;
    }

    @Override
    public Long hset(String s, String s1, String s2) {
        return null;
    }

    @Override
    public String hget(String s, String s1) {
        return null;
    }

    @Override
    public Long hsetnx(String s, String s1, String s2) {
        return null;
    }

    @Override
    public String hmset(String s, Map<String, String> map) {
        return null;
    }

    @Override
    public List<String> hmget(String s, String... strings) {
        return null;
    }

    @Override
    public Long hincrBy(String s, String s1, long l) {
        Map<String,String> valueMap =null;
        if (objectMap.get(s)!=null){
            valueMap=(Map<String,String>)this.objectMap.get(s);
        }else{
            valueMap = new HashMap<>();
            objectMap.put(s,valueMap);
        }
        if(valueMap.get(s1)!=null){
            String value=valueMap.get(s1);
            valueMap.put(s1,String.valueOf(Integer.valueOf(value)+l));
        }else{
            valueMap.put(s1,String.valueOf(l));
        }
        return 0l;
    }

    @Override
    public Boolean hexists(String s, String s1) {
        return null;
    }

    @Override
    public Long hdel(String s, String... strings) {
        return null;
    }

    @Override
    public Long hlen(String s) {
        return null;
    }

    @Override
    public Set<String> hkeys(String s) {
        return null;
    }

    @Override
    public List<String> hvals(String s) {
        return null;
    }

    @Override
    public Map<String, String> hgetAll(String s) {
        if (this.objectMap.get(s)==null)
            return null;
        else{
            Map<String,String> valueMap =(Map<String,String>)this.objectMap.get(s);
            return valueMap;
        }
    }

    @Override
    public Long rpush(String s, String... strings) {
        List<String> stringList=null;
        if(objectMap.get(s)==null){
            stringList= new ArrayList<>();
            objectMap.put(s,stringList);
        }else{
            stringList =(List<String>) objectMap.get(s);
        }
        for(String string:strings){
            stringList.add(0,string);
        }
        return null;
    }

    @Override
    public Long lpush(String s, String... strings) {
        return null;
    }

    @Override
    public Long llen(String s) {
        return null;
    }

    @Override
    public List<String> lrange(String s, long startLong, long endLong) {
        int start = Integer.valueOf(String.valueOf(startLong));
        int end = Integer.valueOf(String.valueOf(endLong));
        if(objectMap.get(s)==null){
            return null;
        }else{
            List<String> stringList =(List<String>) objectMap.get(s);
            if(start>stringList.size()){
                return null;
            }
            if(start<stringList.size()&&end<stringList.size()){
                return stringList.subList(start,end);
            }
            if(start<stringList.size()&&end>stringList.size()){
                return stringList.subList(start,stringList.size());
            }
        }
        return null;
    }

    @Override
    public String ltrim(String s, long l, long l1) {
        return null;
    }

    @Override
    public String lindex(String s, long l) {
        return null;
    }

    @Override
    public String lset(String s, long l, String s1) {
        return null;
    }

    @Override
    public Long lrem(String s, long l, String s1) {
        return null;
    }

    @Override
    public String lpop(String s) {
        return null;
    }

    @Override
    public String rpop(String s) {
        return null;
    }

    @Override
    public Long sadd(String s, String... strings) {
        return null;
    }

    @Override
    public Set<String> smembers(String s) {
        return null;
    }

    @Override
    public Long srem(String s, String... strings) {
        return null;
    }

    @Override
    public String spop(String s) {
        return null;
    }

    @Override
    public Long scard(String s) {
        return null;
    }

    @Override
    public Boolean sismember(String s, String s1) {
        return null;
    }

    @Override
    public String srandmember(String s) {
        return null;
    }

    @Override
    public List<String> srandmember(String s, int i) {
        return null;
    }

    @Override
    public Long strlen(String s) {
        return null;
    }

    @Override
    public Long zadd(String s, double v, String s1) {
        return null;
    }

    @Override
    public Long zadd(String s, Map<String, Double> map) {
        return null;
    }

    @Override
    public Set<String> zrange(String s, long l, long l1) {
        return null;
    }

    @Override
    public Long zrem(String s, String... strings) {
        return null;
    }

    @Override
    public Double zincrby(String s, double v, String s1) {
        return null;
    }

    @Override
    public Long zrank(String s, String s1) {
        return null;
    }

    @Override
    public Long zrevrank(String s, String s1) {
        return null;
    }

    @Override
    public Set<String> zrevrange(String s, long l, long l1) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String s, long l, long l1) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String s, long l, long l1) {
        return null;
    }

    @Override
    public Long zcard(String s) {
        return null;
    }

    @Override
    public Double zscore(String s, String s1) {
        return null;
    }

    @Override
    public List<String> sort(String s) {
        return null;
    }

    @Override
    public List<String> sort(String s, SortingParams sortingParams) {
        return null;
    }

    @Override
    public Long zcount(String s, double v, double v1) {
        return null;
    }

    @Override
    public Long zcount(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String s, double v, double v1) {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String s, double v, double v1) {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String s, double v, double v1, int i, int i1) {
        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String s, String s1, String s2, int i, int i1) {
        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String s, double v, double v1, int i, int i1) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String s, double v, double v1) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String s, double v, double v1) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String s, double v, double v1, int i, int i1) {
        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String s, String s1, String s2, int i, int i1) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String s, String s1, String s2, int i, int i1) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String s, double v, double v1, int i, int i1) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String s, String s1, String s2, int i, int i1) {
        return null;
    }

    @Override
    public Long zremrangeByRank(String s, long l, long l1) {
        return null;
    }

    @Override
    public Long zremrangeByScore(String s, double v, double v1) {
        return null;
    }

    @Override
    public Long zremrangeByScore(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Long zlexcount(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<String> zrangeByLex(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<String> zrangeByLex(String s, String s1, String s2, int i, int i1) {
        return null;
    }

    @Override
    public Long zremrangeByLex(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Long linsert(String s, BinaryClient.LIST_POSITION list_position, String s1, String s2) {
        return null;
    }

    @Override
    public Long lpushx(String s, String... strings) {
        return null;
    }

    @Override
    public Long rpushx(String s, String... strings) {
        return null;
    }

    @Override
    public List<String> blpop(String s) {
        return null;
    }

    @Override
    public List<String> blpop(int i, String s) {
        return null;
    }

    @Override
    public List<String> brpop(String s) {
        return null;
    }

    @Override
    public List<String> brpop(int i, String s) {
        return null;
    }

    @Override
    public Long del(String s) {
        return null;
    }

    @Override
    public String echo(String s) {
        return null;
    }

    @Override
    public Long move(String s, int i) {
        return null;
    }

    @Override
    public Long bitcount(String s) {
        return null;
    }

    @Override
    public Long bitcount(String s, long l, long l1) {
        return null;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String s, int i) {
        return null;
    }

    @Override
    public ScanResult<String> sscan(String s, int i) {
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(String s, int i) {
        return null;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String s, String s1) {
        return null;
    }

    @Override
    public ScanResult<String> sscan(String s, String s1) {
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(String s, String s1) {
        return null;
    }

    @Override
    public Long pfadd(String s, String... strings) {
        return null;
    }

    @Override
    public long pfcount(String s) {
        return 0;
    }
    
    @Override
    public Long publish(String channel, String message) {
        return null;
    }

    @Override
    public Long del(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> blpop(int i, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> brpop(int i, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> blpop(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> brpop(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> keys(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> mget(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String mset(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long msetnx(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String rename(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long renamenx(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String rpoplpush(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> sdiff(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long sdiffstore(String string, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> sinter(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long sinterstore(String string, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long smove(String string, String string1, String string2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long sort(String string, SortingParams sp, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long sort(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> sunion(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long sunionstore(String string, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String watch(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String unwatch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long zinterstore(String string, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long zinterstore(String string, ZParams zp, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long zunionstore(String string, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long zunionstore(String string, ZParams zp, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String brpoplpush(String string, String string1, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(JedisPubSub jps, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void psubscribe(JedisPubSub jps, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String randomKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long bitop(BitOP bitop, String string, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ScanResult<String> scan(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ScanResult<String> scan(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String pfmerge(String string, String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long pfcount(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
