# Project Title: Employee Attendance System

**Presentation:** [https://sg.docworkspace.com/d/sIGvCruxckdr9vwY](https://sg.docworkspace.com/d/sIGvCruxckdr9vwY)

## Student Name: Mitalipova Saiida

---

## Description

The **Employee Attendance System** is a Java-based console application designed to manage employee data and track daily attendance efficiently. It allows performing CRUD (Create, Read, Update, Delete) operations on employee records, monitoring clock-in and clock-out times, and managing attendance statuses.

Additional features include CSV data import/export and summary reporting to assist in evaluating attendance trends.

---

## Objectives

### Employee Management
Efficient handling of employee data:
- Add, view, update, and delete employee information.

### Attendance Tracking
Accurate recording of clock-in/out times:
- Status tracking for each employee.

### Data Persistence
- All data is stored persistently in CSV files across sessions.

### Reporting and Analysis
- Generate reports on attendance patterns:
  - Total employee count
  - Currently clocked-in employees
  - Completed sessions

### Data Import/Export
- Export/import employee and attendance data from/to CSV files.

### User-Friendly Interface
- Command-line interface with:
  - Robust input validation
  - Helpful user feedback

### Error Handling
- Exception handling for smooth operation:
  - Invalid inputs
  - File read/write issues

---

## Project Requirement List

### 1. Create Employee
- Add new employees with unique ID, name, department, and email.
- Validate against duplicate employee IDs.
- Store data in memory and persist to file.

### 2. Read/View Employees
- Display all employees in a formatted table.
- Show ID, name, department, and email.
- Handle empty list scenarios.

### 3. Update Employee
- Modify name, department, and/or email.
- Support partial updates.
- Validate employee existence before updating.

### 4. Delete Employee
- Remove employee by ID.
- Validate existence before deletion.
- Optionally handle related attendance records.

### 5. Clock In
- Record check-in time for an employee.
- Set status to "Present".
- Prevent duplicate clock-ins without clock-out.

### 6. Clock Out
- Record check-out time.
- Validate corresponding clock-in exists.
- Prevent clock-out without prior clock-in.

### 7. View Attendance Records
- Display attendance records in a table format.
- Join employee name with each record.
- Format timestamps for readability.

### 8. Export Data
- Export employee and attendance data to CSV with headers.
- Allow custom filenames.
- Handle file write errors.

### 9. Import Data
- Import employee/attendance data from CSV (skip header).
- Validate format.
- Report success and error counts.

### 10. Generate Summary Report
- Display total employee count.
- Show currently clocked-in employees.
- Show total completed sessions.

---

## Documentation

### Data Structures
- `ArrayList<Employee>` – Stores all employee records.
- `ArrayList<AttendanceRecord>` – Stores all attendance logs in chronological order.

### File Paths and Constants
- `EMPLOYEE_FILE = "employees.csv"` – Stores employee data.
- `ATTENDANCE_FILE = "attendance.csv"` – Stores attendance records.

### Java Stream API
Used for searching and filtering collections:
```java
employees.stream().filter(e -> e.getId().equals(id)).findFirst();
```
Stream API improves readability and simplifies logic compared to traditional loops.

### Error Handling
Implemented using `try-catch` blocks:
- Handles file errors (`IOException`, `FileNotFoundException`)
- Catches input errors like `NumberFormatException`
- Prevents crashes with generic `Exception` handling

### Switch Statements
Used in menu navigation methods:
- `mainMenu()`, `employeeMenu()`, `attendanceMenu()`, `reportsMenu()`
Each case handles a specific operation.

### LocalDate and LocalTime
Utilized for time-sensitive data:
```java
LocalTime clockInTime = LocalTime.now();
```
These classes provide modern and accurate handling of date/time.

### String Processing for CSV

**Serialization (Object → CSV String):**
```java
public String toCSV() {
    return String.join(",", id, name, department, email);
}
```

**Deserialization (CSV String → Object):**
```java
String[] data = line.split(",");
```
Used to convert CSV lines into Java object fields.

### File Import/Export

**Reading CSV (BufferedReader):**
```java
try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
    String line;
    while ((line = reader.readLine()) != null) {
        // Process line
    }
} catch (IOException e) {
    System.out.println("Error reading file: " + e.getMessage());
}
```

**Writing CSV (PrintWriter):**
```java
try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
    // Write data
} catch (IOException e) {
    System.out.println("Error writing to file: " + e.getMessage());
}
```
The try-with-resources block ensures proper resource closure.

### Import and Export Functions

**Example: Importing Employee Data**
```java
private static void importEmployeeData() {
    System.out.print("Enter the file path for employee data: ");
    String filename = scanner.nextLine();

    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        reader.readLine(); // Skip header
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 4) {
                employees.add(new Employee(data[0], data[1], data[2], data[3]));
                // Additional validation and logging
            }
        }
    } catch (IOException e) {
        System.out.println("Error importing employee data: " + e.getMessage());
    }
}
```
Similar logic is implemented for exporting employee and attendance data.

---

Хочешь, могу сохранить это как `.md` файл и отправить, чтобы ты мог сразу использовать его на GitHub или где потребуется.
