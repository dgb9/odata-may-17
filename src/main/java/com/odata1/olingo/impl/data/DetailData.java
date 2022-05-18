package com.odata1.olingo.impl.data;

import java.util.Objects;

public class DetailData {
    private String id;
    private String crtId;
    private String name;
    private String anotherName;
    private int val1;
    private int val2;

    public DetailData() {
    }

    public DetailData(String id, String crtId, String name, String anotherName, int val1, int val2) {
        this.id = id;
        this.crtId = crtId;
        this.name = name;
        this.anotherName = anotherName;
        this.val1 = val1;
        this.val2 = val2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCrtId() {
        return crtId;
    }

    public void setCrtId(String crtId) {
        this.crtId = crtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    public int getVal1() {
        return val1;
    }

    public void setVal1(int val1) {
        this.val1 = val1;
    }

    public int getVal2() {
        return val2;
    }

    public void setVal2(int val2) {
        this.val2 = val2;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DetailData{");
        sb.append("id='").append(id).append('\'');
        sb.append(", crtId='").append(crtId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", anotherName='").append(anotherName).append('\'');
        sb.append(", val1=").append(val1);
        sb.append(", val2=").append(val2);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailData that = (DetailData) o;
        return val1 == that.val1 && val2 == that.val2 && Objects.equals(id, that.id) && Objects.equals(crtId, that.crtId) && Objects.equals(name, that.name) && Objects.equals(anotherName, that.anotherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, crtId, name, anotherName, val1, val2);
    }
}
