package gui;

import util.DataStore;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    // Modern Color Palette
    private static final Color BLUE = new Color(44, 57, 82);      // #2C3952
    private static final Color GREY = new Color(181, 176, 176);   // #B5B0B0
    private static final Color WHITE = new Color(250, 247, 247);  // #FAF7F7
    private static final Color SHADOW = new Color(0, 0, 0, 30);
    
    public LoginFrame() {
        setTitle("Seminar Management System");
        setSize(500, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Gradient Background Panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, WHITE, 0, h, new Color(230, 235, 245));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Login Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setPreferredSize(new Dimension(400, 550));
        
        // Header
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(BLUE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Login to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(GREY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Email Field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setForeground(BLUE);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField emailField = createStyledTextField();
        emailField.setText("alice@mmu.edu.my");
        
        cardPanel.add(emailLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(emailField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password Field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(BLUE);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField passField = createStyledPasswordField();
        passField.setText("pass123");
        
        cardPanel.add(passLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(passField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Role Selection
        JLabel roleLabel = new JLabel("Select Role");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roleLabel.setForeground(BLUE);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JComboBox<String> roleBox = createStyledComboBox(new String[]{"Student", "Coordinator", "Evaluator"});
        
        cardPanel.add(roleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(roleBox);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Login Button
        JButton loginBtn = createModernButton("Login", BLUE, WHITE);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(loginBtn);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Helper text
        JLabel helperLabel = new JLabel("Use demo credentials: alice@mmu.edu.my / pass123");
        helperLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        helperLabel.setForeground(GREY);
        helperLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(helperLabel);
        
        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);
        
        // Login Action
        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            DataStore db = DataStore.getInstance();
            boolean found = false;
            
            if(role.equals("Coordinator")) {
                for(Coordinator c : db.coordinators) {
                    if(c.getEmail().equals(email) && c.getPassword().equals(password)) {
                        new CoordinatorDashboard(c);
                        found = true;
                        break;
                    }
                }
            } else if(role.equals("Evaluator")) {
                for(Evaluator ev : db.evaluators) {
                    if(ev.getEmail().equals(email) && ev.getPassword().equals(password)) {
                        new EvaluatorDashboard(ev);
                        found = true;
                        break;
                    }
                }
            } else if(role.equals("Student")) {
                for(Student s : db.students) {
                    if(s.getEmail().equals(email) && s.getPassword().equals(password)) {
                        new StudentDashboard(s);
                        found = true;
                        break;
                    }
                }
            }
            
            if(found) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        setVisible(true);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(WHITE);
        field.setForeground(BLUE);
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(WHITE);
        field.setForeground(BLUE);
        return field;
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setMaximumSize(new Dimension(300, 40));
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBackground(WHITE);
        box.setForeground(BLUE);
        box.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return box;
    }
    
    private JButton createModernButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            private boolean hover = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow effect on hover
                if(hover) {
                    g2.setColor(SHADOW);
                    g2.fillRoundRect(2, 4, getWidth()-4, getHeight()-2, 20, 20);
                }
                
                // Button background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, hover ? 0 : 2, getWidth(), getHeight()-(hover ? 2 : 4), 20, 20);
                
                // Text
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y - (hover ? 1 : 0));
                
                g2.dispose();
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.putClientProperty("hover", true);
                btn.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.putClientProperty("hover", false);
                btn.repaint();
            }
        });
        
        btn.addChangeListener(e -> {
            btn.repaint();
        });
        
        return btn;
    }
}