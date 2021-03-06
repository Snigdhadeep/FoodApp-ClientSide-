package com.example.diku.food.Module;

import java.util.List;

/**
 * Created by Diku on 21-05-2018.
 */

public class SingleRequest {

    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    List<Order> orders;

    public SingleRequest() {
    }

    public SingleRequest(String phone, String name, String address, String total, List<Order> orders) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.orders = orders;
        this.status="0"; //Default is 0 ; shipping =1 ; shipped =2
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
