package com.cache.bp.bpcashed.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bp_categories")
public class BpCategory {

    String category_id;
    String category_name;
    String category_owner;

    @Id
    @Column(name="category_id", nullable = false)
    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    @Column(name="category_name", nullable = false)
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Column(name="category_owner", nullable = true)
    public String getCategory_owner() {
        return category_owner;
    }

    public void setCategory_owner(String category_owner) {
        this.category_owner = category_owner;
    }

    public BpCategory() {
    }

    public BpCategory(String category_id, String category_name, String category_owner) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_owner = category_owner;
    }
}
