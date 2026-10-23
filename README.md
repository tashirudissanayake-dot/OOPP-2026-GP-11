# Member 3: Session Attendance & Medical Tracking Module
**Module Lead:** Member 3 (Sithum Dilshan - TG/2024/2146)  
**System:** Faculty of Technology Management System (FoT-TMS)  
**Course Unit:** ICT2132 - Object Oriented Programming Practicum  

---

## 1. Module Overview & Responsibilities
Member 3 is responsible for tracking undergraduate student presence and medical documentation. This module implements the 15-theory and 15-practical session engine per module (where 1 credit = 2-hour session), daily attendance logging by Technical Officers, student medical submissions (defaulting to "Pending"), lecturer medical evaluation workflows ("Approved" or "Rejected"), raw vs. effective attendance percentage computation, and categorization into 5 specific SRS threshold scenarios.

---

## 2. Layer-by-Layer Responsibilities

### A. User Interface (UI) Layer
Located in: `Member_Division/Member_3/UI/`
- **`TODashboardFrame.java`**:
  - Technical Officer desktop portal.
  - Daily session attendance logger: Course/Timetable dropdown, session number (1 to 15) selector, dynamic student table with editable Present/Absent status dropdown, "Mark All Present" shortcut, and batch save button.
  - Medical certificate submission form: Undergraduate selector, timetable session, medical reason input, with automated "Pending" status assignment.
- **Attendance & Medical Review Panels**:
  - Medical Review & Approval interface in Lecturer Dashboard: Displays pending submissions with student details and reasons, enabling one-click Approve / Reject actions.
  - Student Attendance History & Status Panel in Undergraduate Dashboard: Interactive metrics banner displaying Theory %, Practical %, Combined %, Raw vs Effective %, and eligibility status badges.
  - Attendance 5 Scenario Summary Table: Displays batch breakdown across Theory-only, Practical-only, and Combined formats.

### B. Process / Business Logic Layer
Located in: `Member_Division/Member_3/Process/`
- **`AttendanceService.java`**:
  - Computation of Raw Attendance: $\frac{\text{Present Count}}{\text{Total Sessions}} \times 100\%$
  - Computation of Effective Attendance (excusing approved medicals): $\frac{\text{Present Count} + \text{Approved Medicals}}{\text{Total Sessions}} \times 100\%$
  - Attendance Eligibility Check: Combined Effective Attendance $\ge 80.0\%$.
  - Classification into the 5 mandatory SRS scenarios:
    1. `>80% Attendance (Raw)`
    2. `Exactly 80% Attendance`
    3. `<80% Attendance without Medicals`
    4. `>80% Attendance with Medicals`
    5. `<80% Attendance with Medicals`
- **`Attendance.java`**:
  - Model encapsulating session number (1-15), date, timetable reference, status (Present/Absent), medical status (None/Pending/Approved/Rejected), and medical reason.
- **`StudentAttendanceSummary.java`**:
  - Comprehensive DTO calculating Theory, Practical, and Combined metrics, percentages, scenario label, and eligibility flag.

### C. Database Layer
Located in: `Member_Division/Member_3/Database/`
- **`AttendanceDAO.java`**:
  - PreparedStatement data operations for `attendance`:
    - `recordOrUpdateAttendance()`: Upserts daily presence.
    - `logMedicalSubmission()`: Logs medical certificates with status 'Pending'.
    - `updateMedicalStatus()`: Updates to 'Approved' or 'Rejected'.
    - `getPendingMedicals()`: Fetches submissions awaiting lecturer review.
    - `getAllMedicals()`: Fetches all medical records.
    - `getAttendanceForStudent()`: Fetches course-wise history for students.
    - `getAttendanceByTimetableAndSession()`: Fetches session roster for TO.
- **`member3_attendance_medicals.sql`**:
  - Dedicated DDL and stored procedure seeding 15 theory and 15 practical sessions for all 20 students, accurately covering all 5 SRS attendance scenarios.

---

## 3. How UI, Process Logic, and Database Work Together
```
[Technical Officer Logs Attendance / Submits Medical in TODashboardFrame]
                                  │
                                  ▼
                      [AttendanceService]
          (Calculates Raw % & Effective %, classifies scenario)
                                  │
                                  ▼
                          [AttendanceDAO]
                                  │
                                  ▼
           [MySQL Database: attendance table (15 sessions)]
                                  │
                                  ▼
 [Lecturer Dashboard: Review Medical] ──> [Student Dashboard: View Eligibility]
```

---

## 4. Dependencies on Other Members
- **Dependencies on Member 1:** References `student_id` (FK to `users.user_id`).
- **Dependencies on Member 2:** References `timetable_id` (FK to `timetables.timetable_id`) to identify the module, session type, and venue.
- **Provided to Member 4:** Supplies the `effectiveAttendancePercentage` and attendance eligibility status required by Member 4 to determine final exam sitting eligibility.

---

## 5. How to Run & Test Member 3's Module
1. Execute `member3_attendance_medicals.sql` in MySQL.
2. Log in as `to_nuwan` / `password123`.
3. Select a session (e.g., Session 10) for ICT2123, change a student's status to Absent, and click Save.
4. Submit a medical record for a student.
5. Log in as `lec_sunil` / `password123`, navigate to "Review Medicals", and approve the medical.
6. Check the student's dashboard to observe their effective percentage and scenario change!
