package controller;

import model.Evaluation;
import model.Student;
import util.DataStore;
import java.util.Comparator;

public class AwardController {
    private DataStore db = DataStore.getInstance();

    // Helper: Get total score for a student
    private int getStudentScore(String studentId) {
        return db.evaluations.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .mapToInt(Evaluation::getTotalScore)
                .findFirst()
                .orElse(0); // Return 0 if not graded yet
    }
    
    // Helper: Get just the presentation score (For People's Choice)
    private int getPresentationScore(String studentId) {
        return db.evaluations.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .mapToInt(Evaluation::getPresentationScore)
                .findFirst()
                .orElse(0);
    }

    public String generateAwardAgenda() {
        StringBuilder sb = new StringBuilder("=== SEMINAR AWARD CEREMONY AGENDA ===\n\n");
        
        // --- 1. Best Oral Presenter ---
        // Filter for "Oral" students, then find max score
        Student bestOral = db.students.stream()
                .filter(s -> "Oral".equals(s.getPresentationType()))
                .filter(s -> getStudentScore(s.getUserID()) > 0) // Must be graded
                .max(Comparator.comparingInt(s -> getStudentScore(s.getUserID())))
                .orElse(null);

        sb.append("🏆 BEST ORAL PRESENTER\n");
        if (bestOral != null) {
            sb.append("   Winner: ").append(bestOral.getName()).append("\n");
            sb.append("   Score:  ").append(getStudentScore(bestOral.getUserID())).append("/20\n");
            sb.append("   Title:  ").append(bestOral.getResearchTitle()).append("\n");
        } else {
            sb.append("   (No eligible candidates scored yet)\n");
        }
        sb.append("\n");

        // --- 2. Best Poster Presenter ---
        // Filter for "Poster" students
        Student bestPoster = db.students.stream()
                .filter(s -> "Poster".equals(s.getPresentationType()))
                .filter(s -> getStudentScore(s.getUserID()) > 0)
                .max(Comparator.comparingInt(s -> getStudentScore(s.getUserID())))
                .orElse(null);

        sb.append("🏆 BEST POSTER PRESENTER\n");
        if (bestPoster != null) {
            sb.append("   Winner: ").append(bestPoster.getName()).append("\n");
            sb.append("   Score:  ").append(getStudentScore(bestPoster.getUserID())).append("/20\n");
            sb.append("   Title:  ").append(bestPoster.getResearchTitle()).append("\n");
        } else {
            sb.append("   (No eligible candidates scored yet)\n");
        }
        sb.append("\n");

        // --- 3. People's Choice (Simulated by Presentation Score) ---
        // Open to ALL students
        Student peoplesChoice = db.students.stream()
                .filter(s -> getPresentationScore(s.getUserID()) > 0)
                .max(Comparator.comparingInt(s -> getPresentationScore(s.getUserID())))
                .orElse(null);

        sb.append("⭐ PEOPLE'S CHOICE AWARD\n");
        sb.append("   (Highest 'Presentation Skill' Score)\n");
        if (peoplesChoice != null) {
            sb.append("   Winner: ").append(peoplesChoice.getName()).append("\n");
            sb.append("   Score:  ").append(getPresentationScore(peoplesChoice.getUserID())).append("/5\n");
        } else {
            sb.append("   (No eligible candidates scored yet)\n");
        }

        return sb.toString();
    }
}