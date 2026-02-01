package controller;

import model.Student;
import model.Submission;
import util.DataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentController {
    // Access the central database
    private DataStore db = DataStore.getInstance();
    
    public boolean registerStudent(Student student) {
        if (student == null) return false;
        
        // Check if already registered in DataStore
        for (Student s : db.students) {
            if (s.getUserID().equals(student.getUserID())) {
                // Update existing
                s.setResearchTitle(student.getResearchTitle());
                s.setAbstractText(student.getAbstractText());
                s.setSupervisorName(student.getSupervisorName());
                s.setPresentationType(student.getPresentationType());
                return true;
            }
        }
        
        // Register new
        if (student.registerSeminar()) {
            db.students.add(student);
            return true;
        }
        return false;
    }
    
    public boolean uploadMaterials(Student student, String filePath) {
        if (student == null || filePath == null || filePath.trim().isEmpty()) return false;
        if (student.getResearchTitle() == null) return false; // Not registered
        
        student.uploadMaterials(filePath);
        return student.getSubmission() != null;
    }
    
    public boolean isStudentRegistered(String studentID) {
        return db.students.stream().anyMatch(s -> s.getUserID().equals(studentID));
    }
    
    public Student getRegisteredStudent(String studentID) {
        return db.students.stream()
                .filter(s -> s.getUserID().equals(studentID))
                .findFirst().orElse(null);
    }
    
    public List<Student> getAllRegisteredStudents() {
        return new ArrayList<>(db.students);
    }
    
    public List<Student> getStudentsByPresentationType(String type) {
        return db.students.stream()
                .filter(s -> type.equals(s.getPresentationType()))
                .collect(Collectors.toList());
    }
    
    public List<Student> getStudentsWithSubmissions() {
        return db.students.stream()
                .filter(s -> s.getSubmission() != null)
                .collect(Collectors.toList());
    }
    
    public String getRegistrationStatistics() {
        long total = db.students.size();
        long oral = getStudentsByPresentationType("Oral").size();
        long poster = getStudentsByPresentationType("Poster").size();
        long submitted = getStudentsWithSubmissions().size();
        
        return String.format("Total: %d\nOral: %d\nPoster: %d\nSubmitted: %d", 
                total, oral, poster, submitted);
    }
}