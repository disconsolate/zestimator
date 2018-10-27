package ir.hossein_shemshadi.objects;

import ir.hossein_shemshadi.annotations.ExcelCell;
import ir.hossein_shemshadi.annotations.ExcelRecord;

@ExcelRecord
public class Property {
    @ExcelCell(number = 1)
    private long propertyId;
    @ExcelCell(number = 2)
    private String city;
    @ExcelCell(number = 3)
    private String state;
    @ExcelCell(number = 4)
    private String zipCode;
    @ExcelCell(number = 5)
    private String address;
    @ExcelCell(number = 6)
    private Zestimate zestimate;

    public Property() {
    }

    public Property(long propertyId, String city, String state, String zipCode, String address, Zestimate zestimate) {
        this.propertyId = propertyId;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.address = address;
        this.zestimate = zestimate;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Zestimate getZestimate() {
        return zestimate;
    }

    public void setZestimate(Zestimate zestimate) {
        this.zestimate = zestimate;
    }
}
