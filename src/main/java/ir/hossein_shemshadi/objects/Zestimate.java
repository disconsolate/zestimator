package ir.hossein_shemshadi.objects;

import java.util.Date;

public class Zestimate {
    private long amount;
    private Date lastUpdated;
    private long valueChange;
    private long lowValuationRange;
    private long highValuationRange;
    private float percentile;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getValueChange() {
        return valueChange;
    }

    public void setValueChange(long valueChange) {
        this.valueChange = valueChange;
    }

    public long getLowValuationRange() {
        return lowValuationRange;
    }

    public void setLowValuationRange(long lowValuationRange) {
        this.lowValuationRange = lowValuationRange;
    }

    public long getHighValuationRange() {
        return highValuationRange;
    }

    public void setHighValuationRange(long highValuationRange) {
        this.highValuationRange = highValuationRange;
    }

    public float getPercentile() {
        return percentile;
    }

    public void setPercentile(float percentile) {
        this.percentile = percentile;
    }

    @Override
    public String toString() {
        return "Zestimate{" +
                "amount=" + amount +
                ", lastUpdated=" + lastUpdated +
                ", valueChange=" + valueChange +
                ", lowValuationRange=" + lowValuationRange +
                ", highValuationRange=" + highValuationRange +
                ", percentile=" + percentile +
                '}';
    }
}
