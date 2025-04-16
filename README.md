# Project Report: Employee Attendance System

Student Name: Mitalipova Saiida  

Project Title: Employee Attendance System

Description:
The Employee Attendance System is a Java-based console application designed to manage employee data and track daily attendance efficiently. The system allows to perform CRUD (Create, Read, Update, Delete) operations on employee records, monitor clock-in and clock-out times, and manage attendance statuses. Additionally, it supports importing and exporting data via CSV files and provides summary reporting functionalities to assist in evaluating attendance trends.

Objectives:

Employee Management:
Enable efficient handling of employee data including adding, viewing, updating, and deleting employee information.
Attendance Tracking:
Facilitate accurate recording of clock-in and clock-out times with status tracking for each employee.
Data Persistence:
Ensure all employee and attendance data is saved persistently through CSV file storage, maintaining data across sessions.
Reporting and Analysis:
Generate summary reports that give insights into employee attendance patterns, such as the number currently clocked in or total records.
Data Import/Export:
Provide functionality for exporting current data and importing existing employee or attendance records from external CSV files to maintain flexibility and continuity.
User-Friendly Interface:
Offer a clear and interactive command-line interface with robust input validation and helpful feedback messages to guide users through different operations.
Error Handling:
Implement exception handling and validations to ensure smooth operation and clear error messages in case of invalid input or file issues.


Project Requirement List

1. Create Employee
   - Add new employees with unique ID, name, department, and email
   - Validate for duplicate employee IDs
   - Store employee data in memory and persist to file

2. Read/View Employees
   - Display all employees in formatted table
   - Show employee details (ID, name, department, email)
   - Handle empty employee list scenario

3. Update Employee
   - Modify existing employee details (name, department, email)
   - Partial updates (change only specified fields)
   - Validate employee exists before updating

4. Delete Employee
   - Remove employee by ID
   - Validate employee exists before deletion
   - Handle related attendance records (optional: cascade delete)

5. Clock In
   - Record employee check-in with timestamp
   - Validate employee exists
   - Set initial status to "Present"
   - Prevent duplicate clock-ins without clock-out

6. Clock Out
   - Record employee check-out with timestamp
   - Validate corresponding clock-in exists
   - Update status (can be modified to "Late" etc.)
   - Prevent clock-out without prior clock-in

7. View Attendance Records
   - Display all attendance records in formatted table
   - Show employee name (join with employee data)
   - Format timestamps for readability
 
8. Export Data
   - Export employees to CSV with headers
   - Export attendance records to CSV with headers
   - Allow custom filenames
   - Handle file write errors

9.Import Data
  - Import employees from CSV (skip header)
   - Import attendance records from CSV (skip header)
   - Validate data format
   - Report success/error counts

10.Generate Summary Report
    - Show total employee count
    - Display currently clocked-in employees
- Show total completed sessions



Documentation


Data Structures:
- ArrayList<Employee>: Dynamic list holding all employee records.
- ArrayList<AttendanceRecord>: Maintains all clock-in/out records in order of entry.
File Paths and Constants:
The project uses constants for file paths to maintain organized file handling and easy future changes to paths.
EMPLOYEE_FILE = "employees.csv": Path for storing employee data.
ATTENDANCE_FILE = "attendance.csv": Path for storing attendance records.

Java Stream API:
- The Stream API is used primarily for searching and filtering employee or attendance records.
- Example use: `employees.stream().filter(e -> e.getId().equals(id)).findFirst();` to locate an employee by ID.
- This approach simplifies filtering and improves code readability compared to traditional loops.

Error Handling:
- The system uses `try-catch` blocks extensively to manage runtime exceptions.
- This includes:
  - Handling `IOException` and `FileNotFoundException` for file operations.
  - Catching `NumberFormatException` when parsing numeric input.
  - General `Exception` handling to prevent application crashes and provide friendly feedback.

Switch Statements:
- Switch-case constructs are used in all major menus (`mainMenu()`, `employeeMenu()`, `attendanceMenu()`, and `reportsMenu()`).
- This design simplifies menu navigation logic and improves readability.
- Each case handles a user-selected operation, such as adding an employee, viewing records, clocking in/out, etc.

LocalTime and LocalDate:
- The application uses `java.time.LocalDate` and `java.time.LocalTime` to record date and time data.
- This avoids legacy `Date` and `Calendar` classes and ensures compatibility with modern date/time handling.
- The clockIn() and clockOut() methods record the current time using LocalTime.now(), which represents the local time of the system.
Example:LocalTime clockInTime = LocalTime.now();

String Processing for CSV Serialization/Deserialization:

String manipulation is crucial for converting in-memory Java objects into CSV format for file storage (serialization) and vice versa (deserialization).

Serialization (Object to CSV String):
    
    public String toCSV() {
        return String.join(",", id, name, department, email);
    }
    
   The `toCSV()` method in the `Employee` and `AttendanceRecord` classes uses `String.join()` to concatenate the object's attributes into a comma-separated string.

Parsing (CSV String to Object Attributes):
    
    String[] data = line.split(",");
    
   When reading data from a CSV file, the `split()` method of the `String` class is used to divide each line into an array of strings based on the comma delimiter. These individual strings are then used to populate the attributes of the corresponding Java object.
 String Processing
Used for serialization/deserialization with CSV files.

 File Import/Export with `BufferedReader` and `PrintWriter`:

The system utilizes `BufferedReader` for efficiently reading data line by line from CSV files and `PrintWriter` for writing formatted data to CSV files.
Reading from CSV (`BufferedReader`):
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            // Process each line of the CSV file
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }
    
   The `BufferedReader` allows for buffered reading, which is more efficient for handling larger files. The `readLine()` method reads one line at a time until the end of the file is reached.

Writing to CSV (`PrintWriter`):
    
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
        // Write data to the CSV file using writer.println()
    } catch (IOException e) {
        System.out.println("Error writing to file: " + e.getMessage());
    }
    
  The `PrintWriter` provides methods for writing formatted output to a file. The `try-with-resources` statement ensures that the `BufferedReader` and `PrintWriter` are automatically closed after use, preventing resource leaks.

Import and Export Functions:

The `AttendanceSystem` class includes dedicated functions for importing and exporting both employee and attendance data to and from CSV files. These functions handle file reading, data parsing, object creation, and file writing.

Example (Importing Employee Data):
    
    private static void importEmployeeData() {
        System.out.print("Enter Enter the file path for employee data: ");
        String filename = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine(); // Skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    employees.add(new Employee(data[0], data[1], data[2], data[3]));
                    // ... (rest of import logic)
                }
            }
        } catch (IOException e) {
            System.out.println("Error importing employee data: " + e.getMessage());
        }
    }
  
   The `importEmployeeData()` function prompts the user for a CSV file, reads it line by line (skipping the header), splits each line into data fields, and creates new `Employee` objects, adding them to the `employees` list. Similar logic is implemented for exporting employee and attendance data and importing attendance data.





