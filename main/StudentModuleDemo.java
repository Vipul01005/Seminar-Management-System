package main;

import model.Student;
import gui.StudentDashboard;
import controller.StudentController;

import javax.swing.*;

/**
 * Demo/Test class for Student Module
 * Demonstrates the complete Student functionality
 */
public class StudentModuleDemo {
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create a sample student user
            Student student = new Student(
                "S001",                          // userID
                "Alice Wong",                    // name
                "alice.wong@student.mmu.edu.my", // email
                "password123"                    // password
            );
            
            // Simulate login
            if (student.login()) {
                System.out.println("Student logged in successfully!");
                System.out.println("Opening Student Dashboard...\n");
                
                // Open Student Dashboard
                new StudentDashboard(student);
            } else {
                System.out.println("Login failed!");
            }
        });
        
        // Optional: Test controller methods directly
        testControllerMethods();
    }
    
    /**
     * Test Student Controller methods independently
     */
    private static void testControllerMethods() {
        System.out.println("\n=== Testing StudentController Methods ===\n");
        
        StudentController controller = new StudentController();
        
        // Create test students
        Student student1 = new Student("S001", "Alice Wong", "alice@mmu.edu.my", "pass123");
        student1.setResearchTitle("Deep Learning for Medical Diagnosis");
        student1.setAbstractText("This research explores the application of deep learning...");
        student1.setSupervisorName("Dr. Lee");
        student1.setPresentationType("Oral");
        
        Student student2 = new Student("S002", "Bob Tan", "bob@mmu.edu.my", "pass456");
        student2.setResearchTitle("Blockchain in Supply Chain Management");
        student2.setAbstractText("This study investigates blockchain technology...");
        student2.setSupervisorName("Prof. Ahmad");
        student2.setPresentationType("Poster");
        
        // Test registration
        System.out.println("Test 1: Register Students");
        boolean reg1 = controller.registerStudent(student1);
        boolean reg2 = controller.registerStudent(student2);
        System.out.println("Student 1 registered: " + reg1);
        System.out.println("Student 2 registered: " + reg2);
        
        // Test material upload
        System.out.println("\nTest 2: Upload Materials");
        boolean upload1 = controller.uploadMaterials(student1, "/path/to/slides.pptx");
        boolean upload2 = controller.uploadMaterials(student2, "/path/to/poster.pdf");
        System.out.println("Student 1 upload: " + upload1);
        System.out.println("Student 2 upload: " + upload2);
        
        // Test retrieval methods
        System.out.println("\nTest 3: Check Registration Status");
        System.out.println("Is S001 registered? " + controller.isStudentRegistered("S001"));
        System.out.println("Is S999 registered? " + controller.isStudentRegistered("S999"));
        
        // Test statistics
        System.out.println("\nTest 4: Statistics");
        System.out.println(controller.getRegistrationStatistics());
        
        // Test filtering by type
        System.out.println("Test 5: Filter by Presentation Type");
        System.out.println("Oral presentations: " + 
            controller.getStudentsByPresentationType("Oral").size());
        System.out.println("Poster presentations: " + 
            controller.getStudentsByPresentationType("Poster").size());
        
        // Test students with submissions
        System.out.println("\nTest 6: Students with Submissions");
        System.out.println("Students who uploaded materials: " + 
            controller.getStudentsWithSubmissions().size());
        
        System.out.println("\n=== Controller Tests Completed ===\n");
    }
    
    /**
     * Create a demo student with pre-filled data
     * Useful for testing the UI
     */
    public static Student createDemoStudent() {
        Student demo = new Student(
            "S001",
            "Alice Wong",
            "alice.wong@student.mmu.edu.my",
            "password123"
        );
        
        // Pre-fill registration data
        demo.setResearchTitle("Deep Learning Approaches for Early Detection of Alzheimer's Disease");
        demo.setAbstractText("This research investigates the application of deep learning models, " +
            "particularly convolutional neural networks and recurrent neural networks, " +
            "for early detection of Alzheimer's disease using MRI brain scans. " +
            "The study aims to improve diagnostic accuracy and enable earlier intervention.");
        demo.setSupervisorName("Dr. Sarah Lee");
        demo.setPresentationType("Oral");
        
        // Simulate material upload
        demo.uploadMaterials("/home/alice/research/presentation_slides.pptx");
        
        return demo;
    }
}