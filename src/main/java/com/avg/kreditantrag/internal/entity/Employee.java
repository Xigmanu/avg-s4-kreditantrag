package com.avg.kreditantrag.internal.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Entity class with employee data.
 */
@Getter
@Builder
@ToString
@SuppressWarnings("unused") // Used for de-/serialization and for Lombok
public final class Employee {
    private int id;
    private String prename;
    private String surname;
    private String email;

    public Employee() { }
    private Employee(int id,
                     String prename,
                     String surname,
                     String email) {
        this.id = id;
        this.prename = prename;
        this.surname = surname;
        this.email = email;
    }
}
