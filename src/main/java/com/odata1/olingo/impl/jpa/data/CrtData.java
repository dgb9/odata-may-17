package com.odata1.olingo.impl.jpa.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "crt")
public class CrtData {

    @Id
    @Column(name = "crt_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "info1")
    private String info1;

    @Column(name = "val1")
    private double val1;

    @Column(name = "val2")
    private double val2;

    @Column(name = "val3")
    private double val3;

    public CrtData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public double getVal1() {
        return val1;
    }

    public void setVal1(double val1) {
        this.val1 = val1;
    }

    public double getVal2() {
        return val2;
    }

    public void setVal2(double val2) {
        this.val2 = val2;
    }

    public double getVal3() {
        return val3;
    }

    public void setVal3(double val3) {
        this.val3 = val3;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CrtData{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", info1='").append(info1).append('\'');
        sb.append(", val1=").append(val1);
        sb.append(", val2=").append(val2);
        sb.append(", val3=").append(val3);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrtData crtData = (CrtData) o;
        return Double.compare(crtData.val1, val1) == 0 && Double.compare(crtData.val2, val2) == 0 && Double.compare(crtData.val3, val3) == 0 && Objects.equals(id, crtData.id) && Objects.equals(name, crtData.name) && Objects.equals(info1, crtData.info1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, info1, val1, val2, val3);
    }
}
