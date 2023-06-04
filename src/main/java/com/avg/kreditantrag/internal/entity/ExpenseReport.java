package com.avg.kreditantrag.internal.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Builder
@Getter
@ToString
@SuppressWarnings("unused") // Used for de-/serialization and for Lombok
public final class ExpenseReport {
    private int employeeId;
    private Date date;
    private double sum;
    private String description;

    public ExpenseReport() {}
    private ExpenseReport(
            int employeeId,
            Date date,
            double sum,
            String description) {
        this.employeeId = employeeId;
        this.date = date;
        this.sum = sum;
        this.description = description;
    }
}
