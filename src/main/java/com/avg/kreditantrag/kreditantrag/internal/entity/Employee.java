package com.avg.kreditantrag.kreditantrag.internal.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@SuppressWarnings("unused") // Used for de-/serialization and for Lombok
public final class Employee {
    private int id;
    private String prename;
    private String surname;

    public Employee() { }
    private Employee(int id, String prename, String surname) {
        this.id = id;
        this.prename = prename;
        this.surname = surname;
    }
}
