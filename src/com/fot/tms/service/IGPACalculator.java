package com.fot.tms.service;

import java.util.List;


public interface IGPACalculator {
    GPACalculatorService.StudentGPASummary calculateGPAForStudent(int studentId);
    GPACalculatorService.StudentGPASummary calculateGPAForStudent(int studentId, Integer semester);
    List<GPACalculatorService.StudentGPASummary> getBatchGPASummaries();
    List<GPACalculatorService.StudentGPASummary> getBatchGPASummaries(Integer semester);
}
