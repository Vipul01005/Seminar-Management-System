package controller;

import model.*;
import util.DataStore;

public class CoordinatorController {
    private DataStore db = DataStore.getInstance();

    public void createSession(String date, String venue, String type) {
        String id = "SESS-" + System.currentTimeMillis();
        Session session = new Session(id, date, venue, type);
        db.sessions.add(session);
    }

    public void assignEvaluatorToSession(Session session, Evaluator evaluator) {
        if(session != null && evaluator != null) {
            session.setAssignedEvaluator(evaluator);
        }
    }

    public boolean addStudentToSession(Session session, Student student) {
        if(session == null || student == null) return false;

        // 1. Check if student is ALREADY in THIS specific session
        if(session.getAssignedStudents().contains(student)) {
            return false;
        }

        // 2. LOGIC UPDATE: Check for Date Conflicts only
        // Student cannot be in two places at once (same date)
        for(Session s : db.sessions) {
            // Only check sessions on the SAME DATE
            if(s.getDate().equals(session.getDate())) {
                for(Student existing : s.getAssignedStudents()) {
                    if(existing.getUserID().equals(student.getUserID())) {
                        System.out.println("Conflict: Student is already booked on " + s.getDate());
                        return false; 
                    }
                }
            }
        }

        // 3. Check presentation type match (Oral student -> Oral session)
        if(student.getPresentationType() != null && 
           student.getPresentationType().equals(session.getType())) {
            session.addStudent(student);
            return true;
        }
        
        return false;
    }
    
    public String generateReport() {
        StringBuilder sb = new StringBuilder("=== FINAL SEMINAR REPORT ===\n\n");
        for (Session s : db.sessions) {
            sb.append("Session: ").append(s.toString()).append("\n");
            sb.append("Evaluator: ").append(s.getAssignedEvaluator() != null ? s.getAssignedEvaluator().getName() : "None").append("\n");
            sb.append("Students: ").append(s.getAssignedStudents().size()).append("\n");
            for(Student stu : s.getAssignedStudents()) {
                sb.append(" - ").append(stu.getName()).append(" (").append(stu.getResearchTitle()).append(")\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}