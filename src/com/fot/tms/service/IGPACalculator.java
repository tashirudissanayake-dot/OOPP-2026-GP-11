package com.fot.tms.service;

import java.util.List;

/**
 * Interface defining GPA, SGPA, and CGPA computation contracts (FR-23).
 * Demonstrates OOP Abstraction & Interface design.
 */
public interface IGPACalculator {
    GPACalculatorService.StudentGPASummary calculateGPAForStudent(int studentId);
    GPACalculatorService.StudentGPASummary calculateGPAForStudent(int studentId, Integer semester);
    List<GPACalculatorService.StudentGPASummary> getBatchGPASummaries();
    List<GPACalculatorService.StudentGPASummary> getBatchGPASummaries(Integer semester);
}
