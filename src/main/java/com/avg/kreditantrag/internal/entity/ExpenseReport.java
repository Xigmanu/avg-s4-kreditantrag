package com.avg.kreditantrag.internal.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@SuppressWarnings("unused") // Used for de-/serialization and for Lombok
public final class ExpenseReport {
    private int employee_id;
    private String date;
    private double sum;
    private String description;

    public ExpenseReport() {}
    private ExpenseReport(
            int employeeId,
            String date,
            double sum,
            String description) {
        this.employee_id = employeeId;
        this.date = date;
        this.sum = sum;
        this.description = description;
    }
}
