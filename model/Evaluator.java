package model;
public class Evaluator extends User {
    public Evaluator(String id, String name, String email, String pass) {
        super(id, name, email, pass, "Evaluator");
    }
    
    // Nice string format for dropdowns
    @Override
    public String toString() { return getName(); }
}