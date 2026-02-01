package gui;

import model.*;
import util.DataStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {
    private static final Color SIDEBAR_BG = new Color(44, 57, 82);
    private static final Color CONTENT_BG = new Color(248, 249, 250);
    private static final Color ACCENT     = new Color(63, 114, 175);
    
    private Student student;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JButton activeNavButton = null;
    
    public StudentDashboard(Student student) {
        this.student = student;
        setTitle("Student Portal - " + student.getName());
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // 1. SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // Profile Summary in Sidebar
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        userIcon.setForeground(Color.WHITE);
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userName = new JLabel(student.getName());
        userName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userName.setForeground(Color.WHITE);
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebar.add(userIcon);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(userName);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Navigation Buttons
        JButton btnHome = createSidebarButton("Dashboard", "home");
        JButton btnReg  = createSidebarButton("Registration", "registration");
        JButton btnUp   = createSidebarButton("Uploads", "upload");
        
        sidebar.add(btnHome);
        sidebar.add(btnReg);
        sidebar.add(btnUp);
        
        sidebar.add(Box.createVerticalGlue()); // Push logout to bottom
        
        JButton btnLogout = createSidebarButton("Logout", "logout");
        btnLogout.addActionListener(e -> { dispose(); new LoginFrame(); });
        sidebar.add(btnLogout);
        
        // 2. MAIN CONTENT
        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);
        mainContentPanel.setBackground(CONTENT_BG);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add Views
        mainContentPanel.add(createHomeView(), "home");
        mainContentPanel.add(new StudentRegistrationPanel(student), "registration");
        mainContentPanel.add(new StudentUploadPanel(student), "upload");
        
        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Set default
        setActiveButton(btnHome);
        
        setVisible(true);
    }
    
    private JButton createSidebarButton(String text, String actionCommand) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(new Color(170, 180, 200)); // Dimmed white
        btn.setBackground(SIDEBAR_BG);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(btn != activeNavButton) btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                if(btn != activeNavButton) btn.setForeground(new Color(170, 180, 200));
            }
        });
        
        if(!actionCommand.equals("logout")) {
            btn.addActionListener(e -> {
                cardLayout.show(mainContentPanel, actionCommand);
                if(actionCommand.equals("home")) {
                    // Refresh home data if needed
                    mainContentPanel.add(createHomeView(), "home"); 
                    cardLayout.show(mainContentPanel, "home");
                }
                setActiveButton(btn);
            });
        }
        
        return btn;
    }
    
    private void setActiveButton(JButton btn) {
        if(activeNavButton != null) {
            activeNavButton.setBackground(SIDEBAR_BG);
            activeNavButton.setForeground(new Color(170, 180, 200));
            // Remove border indicator
            activeNavButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        }
        activeNavButton = btn;
        activeNavButton.setBackground(new Color(35, 45, 65)); // Slightly darker
        activeNavButton.setForeground(Color.WHITE);
        // Add left accent border
        activeNavButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, ACCENT),
            BorderFactory.createEmptyBorder(12, 26, 12, 30)
        ));
    }
    
    private JPanel createHomeView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Header
        JLabel title = new JLabel("Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(44, 57, 82));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Status Card
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JTextArea statusArea = new JTextArea();
        statusArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        statusArea.setEditable(false);
        statusArea.setBackground(Color.WHITE);
        updateStatusText(statusArea);
        
        card.add(statusArea, BorderLayout.CENTER);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateStatusText(JTextArea area) {
        // Reuse your logic here, just format it cleanly
        StringBuilder sb = new StringBuilder();
        sb.append("📋 REGISTRATION STATUS\n");
        sb.append("----------------------\n");
        if(student.getResearchTitle() == null || student.getResearchTitle().isEmpty()) {
            sb.append("Status: 🔴 Not Registered\n");
            sb.append("Action: Please navigate to 'Registration' tab.\n");
        } else {
            sb.append("Status: 🟢 Registered\n");
            sb.append("Title:  ").append(student.getResearchTitle()).append("\n");
            sb.append("Type:   ").append(student.getPresentationType()).append("\n\n");
            
            // Check submission
            if(student.getSubmission() != null) sb.append("File:   🟢 Uploaded\n");
            else sb.append("File:   🔴 Pending Upload\n");
        }
        sb.append("\n\n⚖️ EVALUATION RESULTS\n");
        sb.append("----------------------\n");
        // Add evaluation logic...
        boolean evalFound = false;
        for(Evaluation e : DataStore.getInstance().evaluations) {
            if(e.getStudentId().equals(student.getUserID())) {
                sb.append("Total Score: ").append(e.getTotalScore()).append("/20\n");
                sb.append("Comments:    ").append(e.getComments()).append("\n");
                evalFound = true;
            }
        }
        if(!evalFound) sb.append("Status: ⏳ Pending Evaluation");
        
        area.setText(sb.toString());
    }
}