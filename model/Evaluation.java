package model;

public class Evaluation {
    private String studentId;
    private String evaluatorId;
    private int clarity;      // Problem Clarity
    private int methodology;  // Methodology
    private int results;      // Results
    private int presentation; // Presentation
    private String comments;

    public Evaluation(String studentId, String evaluatorId, int clarity, int methodology, int results, int presentation, String comments) {
        this.studentId = studentId;
        this.evaluatorId = evaluatorId;
        this.clarity = clarity;
        this.methodology = methodology;
        this.results = results;
        this.presentation = presentation;
        this.comments = comments;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getEvaluatorId() { return evaluatorId; }
    public String getComments() { return comments; }
    public int getPresentationScore() { return presentation; }
    
    public int getTotalScore() { 
        return clarity + methodology + results + presentation; 
    }
    
    @Override
    public String toString() {
        return "Problem Clarity: " + clarity + "/5\n" +
               "Methodology:     " + methodology + "/5\n" +
               "Results:         " + results + "/5\n" +
               "Presentation:    " + presentation + "/5\n" +
               "--------------------------\n" +
               "TOTAL: " + getTotalScore() + "/20";
    }
}