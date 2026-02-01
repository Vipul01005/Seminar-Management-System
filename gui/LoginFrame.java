package gui;

import util.DataStore;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    // Professional Corporate Palette
    private static final Color BRAND_PRIMARY = new Color(44, 57, 82);    // Navy
    private static final Color BRAND_ACCENT  = new Color(63, 114, 175);  // Brighter Blue
    private static final Color BG_LIGHT      = new Color(248, 249, 250); // Off-white
    private static final Color TEXT_DARK     = new Color(33, 37, 41);
    private static final Color TEXT_MUTED    = new Color(108, 117, 125);
    
    public LoginFrame() {
        setTitle("Seminar Management System");
        setSize(900, 600); // Widescreen format
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2)); // Split screen
        
        // LEFT SIDE: Branding Panel
        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(BRAND_PRIMARY);
        brandPanel.setLayout(new GridBagLayout());
        
        JPanel brandContent = new JPanel();
        brandContent.setOpaque(false);
        brandContent.setLayout(new BoxLayout(brandContent, BoxLayout.Y_AXIS));
        
        JLabel brandIcon = new JLabel("🎓");
        brandIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        brandIcon.setForeground(Color.WHITE);
        brandIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel brandTitle = new JLabel("Seminar System");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        brandTitle.setForeground(Color.WHITE);
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel brandSub = new JLabel("Manage Research Efficiently");
        brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        brandSub.setForeground(new Color(200, 200, 200));
        brandSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        brandContent.add(brandIcon);
        brandContent.add(Box.createRigidArea(new Dimension(0, 20)));
        brandContent.add(brandTitle);
        brandContent.add(Box.createRigidArea(new Dimension(0, 10)));
        brandContent.add(brandSub);
        
        brandPanel.add(brandContent);
        
        // RIGHT SIDE: Login Form
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new GridBagLayout());
        
        JPanel formContainer = new JPanel();
        formContainer.setOpaque(false);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setPreferredSize(new Dimension(320, 400));
        
        // Header
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel instrLabel = new JLabel("Please enter your details to sign in.");
        instrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instrLabel.setForeground(TEXT_MUTED);
        instrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Inputs
        JLabel emailLbl = createLabel("Email Address");
        JTextField emailField = createStyledTextField();
        emailField.setText("alice@mmu.edu.my"); // Demo text
        
        JLabel passLbl = createLabel("Password");
        JPasswordField passField = createStyledPasswordField();
        passField.setText("pass123");
        
        JLabel roleLbl = createLabel("I am a...");
        JComboBox<String> roleBox = createStyledComboBox(new String[]{"Student", "Coordinator", "Evaluator"});
        
        JButton loginBtn = createPrimaryButton("Sign In");
        
        // Demo helper
        JLabel demoText = new JLabel("Demo: alice@mmu.edu.my / pass123");
        demoText.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        demoText.setForeground(TEXT_MUTED);
        demoText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add to container
        formContainer.add(welcomeLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(instrLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        
        formContainer.add(emailLbl);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(emailField);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        
        formContainer.add(passLbl);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(passField);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        
        formContainer.add(roleLbl);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(roleBox);
        formContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        
        formContainer.add(loginBtn);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        formContainer.add(demoText);
        
        loginPanel.add(formContainer);
        
        add(brandPanel);
        add(loginPanel);
        
        // Logic (Kept identical to original)
        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            DataStore db = DataStore.getInstance();
            boolean found = false;
            
            if(role.equals("Coordinator")) {
                for(Coordinator c : db.coordinators) {
                    if(c.getEmail().equals(email) && c.getPassword().equals(password)) {
                        new CoordinatorDashboard(c); found = true; break;
                    }
                }
            } else if(role.equals("Evaluator")) {
                for(Evaluator ev : db.evaluators) {
                    if(ev.getEmail().equals(email) && ev.getPassword().equals(password)) {
                        new EvaluatorDashboard(ev); found = true; break;
                    }
                }
            } else if(role.equals("Student")) {
                for(Student s : db.students) {
                    if(s.getEmail().equals(email) && s.getPassword().equals(password)) {
                        new StudentDashboard(s); found = true; break;
                    }
                }
            }
            
            if(found) dispose();
            else JOptionPane.showMessageDialog(this, "Invalid credentials", "Access Denied", JOptionPane.ERROR_MESSAGE);
        });
        
        setVisible(true);
    }
    
    // UI HELPER METHODS
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(BRAND_PRIMARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    
    private JTextField createStyledTextField() {
        JTextField f = new JTextField();
        styleComponent(f);
        return f;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField f = new JPasswordField();
        styleComponent(f);
        return f;
    }
    
    private void styleComponent(JComponent c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setBackground(BG_LIGHT);
        c.setForeground(TEXT_DARK);
        c.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> b = new JComboBox<>(items);
        styleComponent(b);
        b.setBackground(Color.WHITE);
        return b;
    }
    
    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BRAND_ACCENT);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BRAND_ACCENT.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(BRAND_ACCENT); }
        });
        return btn;
    }
}