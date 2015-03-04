/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.entity;

/**
 *
 * @author zhangxingang
 */
public class Player extends IntPK{
    private long Uin;
    private int level;
    private int vip;

    /**
     * @return the Uin
     */
    public long getUin() {
        return Uin;
    }

    /**
     * @param Uin the Uin to set
     */
    public void setUin(long Uin) {
        this.Uin = Uin;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the vip
     */
    public int getVip() {
        return vip;
    }

    /**
     * @param vip the vip to set
     */
    public void setVip(int vip) {
        this.vip = vip;
    }
    
}
