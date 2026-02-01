package model;

/**
 * User class - Base class for all system users
 * Provides common authentication and user management functionality
 */
public class User {
    private String userID;
    private String name;
    private String email;
    private String password;
    private String role; // "Student", "Evaluator", or "Coordinator"
    
    // Constructor
    public User(String userID, String name, String email, String password, String role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    /**
     * Authenticate user login
     * @return true if login successful
     */
    public boolean login() {
        // Validation
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // In a real system, this would verify against a database
        // For now, we assume valid credentials
        System.out.println("User logged in: " + name + " (" + role + ")");
        return true;
    }
    
    /**
     * Logout user
     */
    public void logout() {
        System.out.println("User logged out: " + name);
        // Additional logout logic can be added here
    }
    
    // Getters and Setters
    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}