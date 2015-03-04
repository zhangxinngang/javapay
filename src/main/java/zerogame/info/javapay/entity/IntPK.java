package zerogame.info.javapay.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.zerogame.eviver.storage.Key;

/**
 *
 * @author fanngyuan
 */
public class IntPK implements Key,Comparable<IntPK>{

    private long id;

    public IntPK(long id) {
        this.id = id;
    }

    public IntPK(){}
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String marshal() {
        return String.valueOf(id);
    }


    @Override
    public void unMarshal(String keyString) {
        this.id = Long.valueOf(keyString);
    }

    @Override
    @JSONField(serialize = false, deserialize = false)
    public String getInKey() {
        return String.valueOf(this.id);
    }

    @Override
    public int compareTo(IntPK o) {
        if(id-o.getId()>0)
            return 1;
        else if (id-o.getId()==0){
            return 0;
        }else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPK)) return false;

        IntPK intPK = (IntPK) o;

        if (id != intPK.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
