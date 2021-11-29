package assignment.utils.csv;

import java.math.BigDecimal;

public class Product {

    private long id;
    private String key;
    private BigDecimal bigValue;
    private int nativeInt;
    private Long boxedLong;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getBigValue() {
        return bigValue;
    }

    public void setBigValue(BigDecimal bigValue) {
        this.bigValue = bigValue;
    }

    public int getNativeInt() {
        return nativeInt;
    }

    public void setNativeInt(int nativeInt) {
        this.nativeInt = nativeInt;
    }

    public Long getBoxedLong() {
        return boxedLong;
    }

    public void setBoxedLong(Long boxedLong) {
        this.boxedLong = boxedLong;
    }
}
