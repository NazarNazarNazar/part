package com.example.part.model;

import java.io.Serializable;
import java.util.Date;

public class Part implements Serializable {

    private String name;

    private String number;

    private String vendor;

    private Integer qty;

    private Date shipped;

    private Date receive;

    public Part() {
    }

    public Part(String name, String number, String vendor, Integer qty, Date shipped, Date receive) {
        this.name = name;
        this.number = number;
        this.vendor = vendor;
        this.qty = qty;
        this.shipped = shipped;
        this.receive = receive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Date getShipped() {
        return shipped;
    }

    public void setShipped(Date shipped) {
        this.shipped = shipped;
    }

    public Date getReceive() {
        return receive;
    }

    public void setReceive(Date receive) {
        this.receive = receive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Part part = (Part) o;

        return number != null ? number.equals(part.number) : part.number == null;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Part{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", vendor='" + vendor + '\'' +
                ", qty=" + qty +
                ", shipped=" + shipped +
                ", receive=" + receive +
                '}';
    }
}
