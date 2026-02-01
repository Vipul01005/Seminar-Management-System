package model;

/**
 * Student class represents a presenter in the seminar system
 * Extends User to inherit authentication properties
 */
public class Student extends User {
    private String researchTitle;
    private String abstractText;
    private String supervisorName;
    private String presentationType; // "Oral" or "Poster"
    private Submission submission;
    
    // Constructor
    public Student(String userID, String name, String email, String password) {
        super(userID, name, email, password, "Student");
        this.submission = null;
    }
    
    // Full constructor with research details
    public Student(String userID, String name, String email, String password,
                   String researchTitle, String abstractText, 
                   String supervisorName, String presentationType) {
        super(userID, name, email, password, "Student");
        this.researchTitle = researchTitle;
        this.abstractText = abstractText;
        this.supervisorName = supervisorName;
        this.presentationType = presentationType;
        this.submission = null;
    }
    
    /**
     * Register student for seminar with research details
     * @return true if registration successful
     */
    public boolean registerSeminar() {
        // Validation
        if (researchTitle == null || researchTitle.trim().isEmpty()) {
            return false;
        }
        if (abstractText == null || abstractText.trim().isEmpty()) {
            return false;
        }
        if (supervisorName == null || supervisorName.trim().isEmpty()) {
            return false;
        }
        if (presentationType == null || 
            (!presentationType.equals("Oral") && !presentationType.equals("Poster"))) {
            return false;
        }
        
        // Registration logic would be handled by controller
        return true;
    }
    
    /**
     * Upload presentation materials
     * @param path File path to slides or poster
     */
    public void uploadMaterials(String path) {
        if (path != null && !path.trim().isEmpty()) {
            if (submission == null) {
                // Generate unique submission ID
                String submissionID = "SUB-" + this.getUserID() + "-" + System.currentTimeMillis();
                submission = new Submission(submissionID);
            }
            
            if (presentationType.equals("Oral")) {
                submission.setSlidePath(path);
            } else if (presentationType.equals("Poster")) {
                submission.setPosterPath(path);
            }
        }
    }
    
    // Getters and Setters
    public String getResearchTitle() {
        return researchTitle;
    }
    
    public void setResearchTitle(String researchTitle) {
        this.researchTitle = researchTitle;
    }
    
    public String getAbstractText() {
        return abstractText;
    }
    
    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }
    
    public String getSupervisorName() {
        return supervisorName;
    }
    
    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }
    
    public String getPresentationType() {
        return presentationType;
    }
    
    public void setPresentationType(String presentationType) {
        this.presentationType = presentationType;
    }
    
    public Submission getSubmission() {
        return submission;
    }
    
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "userID='" + getUserID() + '\'' +
                ", name='" + getName() + '\'' +
                ", researchTitle='" + researchTitle + '\'' +
                ", presentationType='" + presentationType + '\'' +
                '}';
    }
}