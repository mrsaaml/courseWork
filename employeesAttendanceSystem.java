package org.example;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;



class Employee {
    private String id, name, department, email;

    public Employee(String id, String name, String department, String email) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String toCSV() {
        return String.join(",", id, name, department, email);
    }
}

class AttendanceRecord {
    private String employeeId;
    private LocalDateTime clockIn, clockOut;
    private String status;

    public AttendanceRecord(String employeeId, LocalDateTime clockIn, LocalDateTime clockOut, String status) {
        this.employeeId = employeeId;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.status = status;
    }

    public String getEmployeeId() { return employeeId; }
    public LocalDateTime getClockIn() { return clockIn; }
    public LocalDateTime getClockOut() { return clockOut; }
    public String getStatus() { return status; }
    public void setClockOut(LocalDateTime clockOut) { this.clockOut = clockOut; }
    public void setStatus(String status) { this.status = status; }

    public String toCSV() {
        return String.join(",",
                employeeId,
                clockIn.toString(),
                clockOut != null ? clockOut.toString() : "null",
                status);
    }
}

public class AttendanceSystem {

    private static List<Employee> employees = new ArrayList<>();
    private static List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    private static final String EMPLOYEE_FILE = "employees.csv";
    private static final String ATTENDANCE_FILE = "attendance.csv";
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n=== Employee Attendance System ===");
            System.out.println("1. Employee Management");
            System.out.println("2. Attendance Tracking");
            System.out.println("3. Reports & Import/Export");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput(1, 4);

            switch (choice) {
                case 1: employeeMenu(); break;
                case 2: attendanceMenu(); break;
                case 3: reportsMenu(); break;
                case 4: saveData(); saveAttendanceData(); System.exit(0);
            }
        }
    }


    private static void employeeMenu() {
        System.out.println("\n=== Employee Management ===");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Update Employee");
        System.out.println("4. Delete Employee");
        System.out.println("5. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = getIntInput(1, 5);

        switch (choice) {
            case 1: addEmployee(); break;
            case 2: viewEmployees(); break;
            case 3: updateEmployee(); break;
            case 4: deleteEmployee(); break;
            case 5: return;
        }
    }

    private static void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        System.out.print("ID: ");
        String id = scanner.nextLine();

        if (employees.stream().anyMatch(e -> e.getId().equals(id))) {
            System.out.println("Error: Employee ID already exists!");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        employees.add(new Employee(id, name, department, email));
        System.out.println("Employee added successfully!");
    }

    private static void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found!");
            return;
        }

        System.out.println("\n--- Employee List ---");
        System.out.printf("%-10s %-20s %-15s %-20s%n", "ID", "Name", "Department", "Email");
        employees.forEach(emp ->
                System.out.printf("%-10s %-20s %-15s %-20s%n",
                        emp.getId(), emp.getName(), emp.getDepartment(), emp.getEmail()));
    }

    private static void updateEmployee() {
        System.out.print("Enter Employee ID to update: ");
        String id = scanner.nextLine();

        Optional<Employee> employee = employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();

        if (employee.isEmpty()) {
            System.out.println("Error: Employee not found!");
            return;
        }

        System.out.print("New Name (leave blank to keep unchanged): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) employee.get().setName(name);

        System.out.print("New Department (leave blank to keep unchanged): ");
        String department = scanner.nextLine();
        if (!department.isEmpty()) employee.get().setDepartment(department);

        System.out.print("New Email (leave blank to keep unchanged): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) employee.get().setEmail(email);

        System.out.println("Employee updated successfully!");
    }

    private static void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        String id = scanner.nextLine();

        Optional<Employee> employee = employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();

        if (employee.isEmpty()) {
            System.out.println("Error: Employee not found!");
            return;
        }

        employees.remove(employee.get());
        System.out.println("Employee deleted successfully!");
    }


    private static void attendanceMenu() {
        System.out.println("\n=== Attendance Tracking ===");
        System.out.println("1. Clock In");
        System.out.println("2. Clock Out");
        System.out.println("3. View Attendance Records");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = getIntInput(1, 4);

        switch (choice) {
            case 1: clockIn(); break;
            case 2: clockOut(); break;
            case 3: viewAttendance(); break;
            case 4: return;
        }
    }


    private static final LocalTime WORK_START = LocalTime.of(9, 0); // 9:00 AM
    private static final LocalTime WORK_END = LocalTime.of(17, 0); // 5:00 PM
    private static void clockIn() {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();

        Optional<Employee> employee = employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();

        if (employee.isEmpty()) {
            System.out.println("Error: Employee not found!");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String status = "Present";


        if (now.toLocalTime().isAfter(WORK_START.plusMinutes(15))) {
            status = "Late";
        }

        attendanceRecords.add(new AttendanceRecord(id, now, null, status));
        System.out.println("Clocked in at: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("Status: " + status);
    }

    private static void clockOut() {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();

        Optional<AttendanceRecord> record = attendanceRecords.stream()
                .filter(a -> a.getEmployeeId().equals(id) && a.getClockOut() == null)
                .findFirst();

        if (record.isEmpty()) {
            System.out.println("Error: No clock-in record found for this employee or already clocked out!");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        record.get().setClockOut(now);


        if (now.toLocalTime().isBefore(WORK_END)) {
            record.get().setStatus("Early Departure");
        }


        System.out.println("Clocked out at: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("Final Status: " + record.get().getStatus());
    }

    private static void viewAttendance() {
        if (attendanceRecords.isEmpty()) {
            System.out.println("No attendance records found!");
            return;
        }

        System.out.println("\n--- Attendance Records ---");
        System.out.printf("%-10s %-20s %-20s %-20s %-10s%n", "ID", "Employee", "Clock In", "Clock Out", "Status");
        attendanceRecords.forEach(record -> {
            String employeeName = employees.stream()
                    .filter(e -> e.getId().equals(record.getEmployeeId()))
                    .map(Employee::getName)
                    .findFirst()
                    .orElse("Unknown");
            System.out.printf("%-10s %-20s %-20s %-20s %-10s%n",
                    record.getEmployeeId(),
                    employeeName,
                    record.getClockIn().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    record.getClockOut() != null ? record.getClockOut().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A",
                    record.getStatus());
        });
    }


    private static void reportsMenu() {
        System.out.println("\n=== Reports & Import/Export ===");
        System.out.println("1. Export Employee Data");
        System.out.println("2. Export Attendance Data");
        System.out.println("3. Import Employee Data");
        System.out.println("4. Import Attendance Data");
        System.out.println("5. Generate Summary Report");
        System.out.println("6. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = getIntInput(1, 6);

        switch (choice) {
            case 1: exportEmployeeData(); break;
            case 2: exportAttendanceData(); break;
            case 3: importEmployeeData(); break;
            case 4: importAttendanceData(); break;
            case 5: generateSummary(); break;
            case 6: return;
        }
    }

    private static void exportEmployeeData() {
        System.out.print("Enter filename for employee data (without extension): ");
        String filename = scanner.nextLine();

        try (PrintWriter writer = new PrintWriter(filename + ".csv")) {
            writer.println("ID,Name,Department,Email");
            employees.forEach(emp -> writer.println(emp.toCSV()));
            System.out.println("Employee data exported to " + filename + ".csv successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not export employee data!");
        }
    }

    private static void exportAttendanceData() {
        System.out.print("Enter filename for attendance data (without extension): ");
        String filename = scanner.nextLine();

        try (PrintWriter writer = new PrintWriter(filename + ".csv")) {
            writer.println("Employee ID,Clock In,Clock Out,Status");
            attendanceRecords.forEach(record -> writer.println(record.toCSV()));
            System.out.println("Attendance data exported to " + filename + ".csv successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not export attendance data!");
        }
    }

    private static void importEmployeeData() {
        System.out.print("Enter path to employee data to import: ");
        String path = scanner.nextLine();

        List<Employee> importedEmployees = new ArrayList<>();
        int importedCount = 0;
        int errorCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    if (data.length >= 4) {
                        importedEmployees.add(new Employee(data[0], data[1], data[2], data[3]));
                        importedCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                }
            }


            if (importedCount > 0) {
                employees = importedEmployees;
                System.out.println("Successfully imported " + importedCount + " employees.");
                if (errorCount > 0) {
                    System.out.println("Encountered " + errorCount + " errors during import.");
                }
            } else {
                System.out.println("No valid employee data found in file.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: Could not read file!");
        }
    }

    private static void importAttendanceData() {
        System.out.print("Enter path to attendance data to import: ");
        String path = scanner.nextLine();

        List<AttendanceRecord> importedRecords = new ArrayList<>();
        int importedCount = 0;
        int errorCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    if (data.length >= 4) {
                        LocalDateTime clockIn = LocalDateTime.parse(data[1]);
                        LocalDateTime clockOut = data[2].equals("null") ? null : LocalDateTime.parse(data[2]);
                        importedRecords.add(new AttendanceRecord(data[0], clockIn, clockOut, data[3]));
                        importedCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                }
            }


            if (importedCount > 0) {
                attendanceRecords = importedRecords;
                System.out.println("Successfully imported " + importedCount + " attendance records.");
                if (errorCount > 0) {
                    System.out.println("Encountered " + errorCount + " errors during import.");
                }
            } else {
                System.out.println("No valid attendance data found in file.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: Could not read file!");
        }
    }

    private static void generateSummary() {
        long totalEmployees = employees.size();
        long totalClockedIn = attendanceRecords.stream().filter(r -> r.getClockOut() == null).count();
        long totalClockedOut = attendanceRecords.size() - totalClockedIn;

        System.out.println("\n=== Summary Report ===");
        System.out.println("Total Employees: " + totalEmployees);
        System.out.println("Currently Clocked In: " + totalClockedIn);
        System.out.println("Total Clocked Out: " + totalClockedOut);
    }


    private static void loadData() {

        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                employees.add(new Employee(data[0], data[1], data[2], data[3]));
            }
        } catch (IOException e) {
            System.out.println("No existing employee data found. Starting fresh.");
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                LocalDateTime clockIn = LocalDateTime.parse(data[1]);
                LocalDateTime clockOut = data[2].equals("null") ? null : LocalDateTime.parse(data[2]);
                attendanceRecords.add(new AttendanceRecord(data[0], clockIn, clockOut, data[3]));
            }
        } catch (IOException e) {
            System.out.println("No existing attendance data found. Starting fresh.");
        }
    }

    private static void saveData() {
        try (PrintWriter writer = new PrintWriter(EMPLOYEE_FILE)) {
            employees.forEach(emp -> writer.println(emp.toCSV()));
        } catch (FileNotFoundException e) {
            System.out.println("Error saving employee data!");
        }
    }

    private static void saveAttendanceData() {
        try (PrintWriter writer = new PrintWriter(ATTENDANCE_FILE)) {
            attendanceRecords.forEach(record -> writer.println(record.toCSV()));
        } catch (FileNotFoundException e) {
            System.out.println("Error saving attendance data!");
        }
    }

    
    private static int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.printf("Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}
