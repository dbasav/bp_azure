package com.cache.bp.bpcashed.model;

import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;

@Entity
@Table(name = "bp_customer")
public class BPCustomer {
    long cust_id;
    String cust_name;
    String cust_address1;
    String cust_address2;
    String cust_city;
    String cust_zip;
    String cust_category;

    public BPCustomer() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long getCust_id() {
        return cust_id;
    }

    public void setCust_id(long cust_id) {
        this.cust_id = cust_id;
    }
    @Column(name="cust_name", nullable = false)
    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    @Column(name="cust_address1", nullable = false)
    public String getCust_address1() {
        return cust_address1;
    }

    public void setCust_address1(String cust_address1) {
        this.cust_address1 = cust_address1;
    }

    @Column(name="cust_address2", nullable = true)
    public String getCust_address2() {
        return cust_address2;
    }

    public void setCust_address2(String cust_address2) {
        this.cust_address2 = cust_address2;
    }

    @Column(name="cust_city", nullable = true)
    public String getCust_city() {
        return cust_city;
    }

    public void setCust_city(String cust_city) {
        this.cust_city = cust_city;
    }

    @Column(name="cust_zip", nullable = false)
    public String getCust_zip() {
        return cust_zip;
    }

    public void setCust_zip(String cust_zip) {
        this.cust_zip = cust_zip;
    }

    @Column(name="cust_category", nullable = false)
    public String getCust_category() {
        return cust_category;
    }

    public void setCust_category(String cust_category) {
        this.cust_category = cust_category;
    }

    public BPCustomer(long cust_id, String cust_name, String cust_address1, String cust_address2, String cust_city, String cust_zip, String cust_category) {
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.cust_address1 = cust_address1;
        this.cust_address2 = cust_address2;
        this.cust_city = cust_city;
        this.cust_zip = cust_zip;
        this.cust_category = cust_category;
    }
}
