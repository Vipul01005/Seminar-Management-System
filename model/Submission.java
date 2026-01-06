package model;

/**
 * Submission class represents presentation materials uploaded by students
 * Stores file paths for slides (Oral) or posters (Poster)
 */
public class Submission {
    private String submissionID;
    private String slidePath;      // For Oral presentations
    private String posterPath;     // For Poster presentations
    private String boardID;        // For Poster board assignment
    
    // Constructor
    public Submission(String submissionID) {
        this.submissionID = submissionID;
        this.slidePath = null;
        this.posterPath = null;
        this.boardID = null;
    }
    
    // Full constructor
    public Submission(String submissionID, String slidePath, String posterPath, String boardID) {
        this.submissionID = submissionID;
        this.slidePath = slidePath;
        this.posterPath = posterPath;
        this.boardID = boardID;
    }
    
    // Getters and Setters
    public String getSubmissionID() {
        return submissionID;
    }
    
    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }
    
    public String getSlidePath() {
        return slidePath;
    }
    
    public void setSlidePath(String slidePath) {
        this.slidePath = slidePath;
    }
    
    public String getPosterPath() {
        return posterPath;
    }
    
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    
    public String getBoardID() {
        return boardID;
    }
    
    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }
    
    /**
     * Check if submission has any materials uploaded
     * @return true if either slides or poster is uploaded
     */
    public boolean hasUploadedMaterials() {
        return (slidePath != null && !slidePath.trim().isEmpty()) ||
               (posterPath != null && !posterPath.trim().isEmpty());
    }
    
    @Override
    public String toString() {
        return "Submission{" +
                "submissionID='" + submissionID + '\'' +
                ", slidePath='" + slidePath + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", boardID='" + boardID + '\'' +
                '}';
    }
}