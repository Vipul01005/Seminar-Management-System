package util;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;
    
    // Shared Lists
    public List<Student> students = new ArrayList<>();
    public List<Evaluator> evaluators = new ArrayList<>();
    public List<Coordinator> coordinators = new ArrayList<>();
    public List<Session> sessions = new ArrayList<>();
    public List<Evaluation> evaluations = new ArrayList<>(); 
    
    private DataStore() {
        initializeData();
    }
    
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
    
    private void initializeData() {
        // 1. Admin Users
        coordinators.add(new Coordinator("C001", "Dr. Admin", "admin@mmu.edu.my", "admin123"));
        evaluators.add(new Evaluator("E001", "Dr. Panel", "panel@mmu.edu.my", "panel123"));
        
        // 2. Student A: Alice (The "Demo" Student)
        // She is a FRESH account. No title, no abstract, no submission.
        Student s1 = new Student("S001", "Alice Wong", "alice@mmu.edu.my", "pass123");
        students.add(s1);

        // 3. Student B: Bob (The "Competitor")
        // He is PRE-REGISTERED so you don't have to waste time typing his details.
        Student s2 = new Student("S002", "Bob Smith", "bob@mmu.edu.my", "pass123");
        s2.setResearchTitle("Network Security in IoT");
        s2.setSupervisorName("Dr. Who");
        s2.setAbstractText("Securing smart home devices against botnets.");
        s2.setPresentationType("Poster");
        
        // Bob has "uploaded" a file already
        Submission sub2 = new Submission("SUB-002");
        sub2.setPosterPath("C:/Dummy/Path/bob_poster.pdf"); 
        s2.setSubmission(sub2);
        
        students.add(s2);
        
        // NOTE: No Sessions, No Evaluations. Everything else is created LIVE.
    }
}