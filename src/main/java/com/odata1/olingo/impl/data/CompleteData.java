package com.odata1.olingo.impl.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompleteData {
    private String firstName;
    private String lastName;
    private final List<PairValueData> items;

    public CompleteData() {
        items = new ArrayList<>();
    }

    public CompleteData(String firstName, String lastName, List<PairValueData> items) {
        this();

        this.firstName = firstName;
        this.lastName = lastName;
        this.items.clear();

        if (items != null) {
            this.items.addAll(items);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<PairValueData> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompleteData that = (CompleteData) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, items);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompleteData{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", items=").append(items);
        sb.append('}');
        return sb.toString();
    }
}
