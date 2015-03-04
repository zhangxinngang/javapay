package com.zerogame.eviver.storage;

/**
 * Created by fanngyuan on 5/13/14.
 */
public interface Key {
    public String marshal();

    public void unMarshal(String keyString);

    public String getInKey();
}
