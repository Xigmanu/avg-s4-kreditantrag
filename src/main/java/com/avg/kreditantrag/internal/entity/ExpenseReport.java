package com.avg.kreditantrag.internal.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Entity class with expense report data.
 */
@Builder
@Getter
@ToString
@SuppressWarnings("unused") // Used for de-/serialization and for Lombok
public final class ExpenseReport {
    private int id;
    private int employee_id;
    private String date;
    private double sum;
    private String description;

    public ExpenseReport() {}
    private ExpenseReport(
            int id,
            int employeeId,
            String date,
            double sum,
            String description) {
        this.id = id;
        this.employee_id = employeeId;
        this.date = date;
        this.sum = sum;
        this.description = description;
    }
}
