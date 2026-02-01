package model;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private String id, date, venue, type;
    private List<Student> assignedStudents = new ArrayList<>();
    private Evaluator assignedEvaluator;

    public Session(String id, String date, String venue, String type) {
        this.id = id; 
        this.date = date; 
        this.venue = venue; 
        this.type = type;
    }

    public void addStudent(Student s) { assignedStudents.add(s); }
    public void setAssignedEvaluator(Evaluator e) { this.assignedEvaluator = e; }
    
    // Getters
    public String getType() { return type; }
    public String getDate() { return date; } // <--- Added this
    public String getVenue() { return venue; } // <--- Added this
    public List<Student> getAssignedStudents() { return assignedStudents; }
    public Evaluator getAssignedEvaluator() { return assignedEvaluator; }

    @Override
    public String toString() { return date + " - " + venue + " (" + type + ")"; }
}