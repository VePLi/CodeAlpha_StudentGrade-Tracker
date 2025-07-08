import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Student class to store student information
class Student {
    private String name;
    private int id;
    private ArrayList<Double> grades;
    
    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.grades = new ArrayList<>();
    }
    
    public void addGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            grades.add(grade);
        } else {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }
    }
    
    public double getAverage() {
        if (grades.isEmpty()) return 0.0;
        return grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    public double getHighestGrade() {
        if (grades.isEmpty()) return 0.0;
        return grades.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }
    
    public double getLowestGrade() {
        if (grades.isEmpty()) return 0.0;
        return grades.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }
    
    public String getLetterGrade() {
        double avg = getAverage();
        if (avg >= 90) return "A";
        else if (avg >= 80) return "B";
        else if (avg >= 70) return "C";
        else if (avg >= 60) return "D";
        else return "F";
    }
    
    // Getters
    public String getName() { return name; }
    public int getId() { return id; }
    public ArrayList<Double> getGrades() { return new ArrayList<>(grades); }
    public int getGradeCount() { return grades.size(); }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Grades: %d | Average: %.2f | Letter: %s",
                id, name, grades.size(), getAverage(), getLetterGrade());
    }
}

// Grade Manager class to handle all operations
class GradeManager {
    private ArrayList<Student> students;
    private int nextId;
    
    public GradeManager() {
        students = new ArrayList<>();
        nextId = 1;
    }
    
    public void addStudent(String name) {
        students.add(new Student(name, nextId++));
    }
    
    public Student findStudent(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public Student findStudent(String name) {
        return students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    public void addGrade(int studentId, double grade) {
        Student student = findStudent(studentId);
        if (student != null) {
            student.addGrade(grade);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }
    }
    
    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    public double getClassAverage() {
        if (students.isEmpty()) return 0.0;
        return students.stream()
                .mapToDouble(Student::getAverage)
                .average()
                .orElse(0.0);
    }
    
    public double getClassHighest() {
        return students.stream()
                .mapToDouble(Student::getHighestGrade)
                .max()
                .orElse(0.0);
    }
    
    public double getClassLowest() {
        return students.stream()
                .mapToDouble(Student::getLowestGrade)
                .min()
                .orElse(0.0);
    }
    
    public String getClassSummary() {
        if (students.isEmpty()) {
            return "No students in the system.";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("=== CLASS SUMMARY REPORT ===\n");
        summary.append(String.format("Total Students: %d\n", students.size()));
        summary.append(String.format("Class Average: %.2f\n", getClassAverage()));
        summary.append(String.format("Highest Grade: %.2f\n", getClassHighest()));
        summary.append(String.format("Lowest Grade: %.2f\n", getClassLowest()));
        summary.append("\n=== INDIVIDUAL STUDENT REPORTS ===\n");
        
        for (Student student : students) {
            summary.append(student.toString()).append("\n");
        }
        
        return summary.toString();
    }
}

// Console Interface
class ConsoleInterface {
    private GradeManager gradeManager;
    private Scanner scanner;
    
    public ConsoleInterface() {
        gradeManager = new GradeManager();
        scanner = new Scanner(System.in);
    }
    
    public void run() {
        System.out.println("=== STUDENT GRADE MANAGEMENT SYSTEM ===");
        
        while (true) {
            printMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addGrade();
                    break;
                case 3:
                    viewAllStudents();
                    break;
                case 4:
                    viewStudentDetails();
                    break;
                case 5:
                    viewClassSummary();
                    break;
                case 6:
                    System.out.println("Thank you for using the Grade Management System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add New Student");
        System.out.println("2. Add Grade to Student");
        System.out.println("3. View All Students");
        System.out.println("4. View Student Details");
        System.out.println("5. View Class Summary");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private int getChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }
    
    private void addStudent() {
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        
        if (!name.isEmpty()) {
            gradeManager.addStudent(name);
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Invalid name. Please try again.");
        }
    }
    
    private void addGrade() {
        System.out.print("Enter student ID: ");
        int id = getChoice();
        
        if (gradeManager.findStudent(id) == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.print("Enter grade (0-100): ");
        try {
            double grade = scanner.nextDouble();
            gradeManager.addGrade(id, grade);
            System.out.println("Grade added successfully!");
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            System.out.println("Invalid grade format!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void viewAllStudents() {
        ArrayList<Student> students = gradeManager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        System.out.println("\n--- ALL STUDENTS ---");
        for (Student student : students) {
            System.out.println(student);
        }
    }
    
    private void viewStudentDetails() {
        System.out.print("Enter student ID: ");
        int id = getChoice();
        
        Student student = gradeManager.findStudent(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\n--- STUDENT DETAILS ---");
        System.out.println("Name: " + student.getName());
        System.out.println("ID: " + student.getId());
        System.out.println("Grades: " + student.getGrades());
        System.out.println("Number of Grades: " + student.getGradeCount());
        System.out.println("Average: " + String.format("%.2f", student.getAverage()));
        System.out.println("Highest Grade: " + String.format("%.2f", student.getHighestGrade()));
        System.out.println("Lowest Grade: " + String.format("%.2f", student.getLowestGrade()));
        System.out.println("Letter Grade: " + student.getLetterGrade());
    }
    
    private void viewClassSummary() {
        System.out.println("\n" + gradeManager.getClassSummary());
    }
}

// GUI Interface
class GUIInterface extends JFrame {
    private GradeManager gradeManager;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JTextArea summaryArea;
    
    public GUIInterface() {
        gradeManager = new GradeManager();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Student Grade Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addStudentBtn = new JButton("Add Student");
        JButton addGradeBtn = new JButton("Add Grade");
        JButton refreshBtn = new JButton("Refresh");
        JButton summaryBtn = new JButton("Show Summary");
        
        buttonPanel.add(addStudentBtn);
        buttonPanel.add(addGradeBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(summaryBtn);
        
        // Create table
        String[] columns = {"ID", "Name", "Grades Count", "Average", "Highest", "Lowest", "Letter Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        
        // Create summary area
        summaryArea = new JTextArea(10, 30);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane summaryScrollPane = new JScrollPane(summaryArea);
        
        // Add components
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(summaryScrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add action listeners
        addStudentBtn.addActionListener(e -> addStudent());
        addGradeBtn.addActionListener(e -> addGrade());
        refreshBtn.addActionListener(e -> refreshTable());
        summaryBtn.addActionListener(e -> showSummary());
        
        // Set window properties
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Initialize with sample data
        addSampleData();
        refreshTable();
    }
    
    private void addSampleData() {
        gradeManager.addStudent("John Smith");
        gradeManager.addStudent("Jane Doe");
        gradeManager.addStudent("Bob Johnson");
        
        try {
            gradeManager.addGrade(1, 85.5);
            gradeManager.addGrade(1, 92.0);
            gradeManager.addGrade(1, 78.5);
            
            gradeManager.addGrade(2, 95.0);
            gradeManager.addGrade(2, 87.5);
            gradeManager.addGrade(2, 91.0);
            
            gradeManager.addGrade(3, 72.0);
            gradeManager.addGrade(3, 68.5);
            gradeManager.addGrade(3, 75.0);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error adding sample data: " + e.getMessage());
        }
    }
    
    private void addStudent() {
        String name = JOptionPane.showInputDialog(this, "Enter student name:");
        if (name != null && !name.trim().isEmpty()) {
            gradeManager.addStudent(name.trim());
            refreshTable();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
        }
    }
    
    private void addGrade() {
        String idStr = JOptionPane.showInputDialog(this, "Enter student ID:");
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            Student student = gradeManager.findStudent(id);
            
            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }
            
            String gradeStr = JOptionPane.showInputDialog(this, 
                "Enter grade for " + student.getName() + " (0-100):");
            if (gradeStr == null) return;
            
            double grade = Double.parseDouble(gradeStr);
            gradeManager.addGrade(id, grade);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Grade added successfully!");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student student : gradeManager.getAllStudents()) {
            Object[] row = {
                student.getId(),
                student.getName(),
                student.getGradeCount(),
                String.format("%.2f", student.getAverage()),
                String.format("%.2f", student.getHighestGrade()),
                String.format("%.2f", student.getLowestGrade()),
                student.getLetterGrade()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showSummary() {
        summaryArea.setText(gradeManager.getClassSummary());
    }
}

// Main class
public class StudentGradeManager {
    public static void main(String[] args) {
        System.out.println("Choose interface:");
        System.out.println("1. Console Interface");
        System.out.println("2. GUI Interface");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter choice (1 or 2): ");
        
        try {
            int choice = scanner.nextInt();
            
            if (choice == 1) {
                new ConsoleInterface().run();
            } else if (choice == 2) {
                SwingUtilities.invokeLater(() -> new GUIInterface());
            } else {
                System.out.println("Invalid choice. Starting GUI interface...");
                SwingUtilities.invokeLater(() -> new GUIInterface());
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Starting GUI interface...");
            SwingUtilities.invokeLater(() -> new GUIInterface());
        }
    }
}