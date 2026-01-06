package gui;

import model.*;
import util.DataStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {
    private static final Color BLUE = new Color(44, 57, 82);
    private static final Color GREY = new Color(181, 176, 176);
    private static final Color WHITE = new Color(250, 247, 247);
    private static final Color SHADOW = new Color(0, 0, 0, 30);
    
    private Student student;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public StudentDashboard(Student student) {
        this.student = student;
        setTitle("Student Dashboard - " + student.getName());
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Top Bar
        add(createTopBar(), BorderLayout.NORTH);
        
        // Main Content with Gradient
        JPanel contentWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, WHITE, 0, getHeight(), new Color(230, 235, 245));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentWrapper.setLayout(new BorderLayout());
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);
        
        mainPanel.add(createHomePanel(), "home");
        mainPanel.add(new StudentRegistrationPanel(student), "registration");
        mainPanel.add(new StudentUploadPanel(student), "upload");
        
        contentWrapper.add(mainPanel, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BLUE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Left: Title
        JLabel titleLabel = new JLabel("📚 Seminar Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        
        // Center: Navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        navPanel.setOpaque(false);
        
        JButton homeBtn = createNavButton("🏠 Home");
        JButton regBtn = createNavButton("📝 Registration");
        JButton uploadBtn = createNavButton("📤 Upload");
        
        homeBtn.addActionListener(e -> showPanel("home"));
        regBtn.addActionListener(e -> showPanel("registration"));
        uploadBtn.addActionListener(e -> showPanel("upload"));
        
        navPanel.add(homeBtn);
        navPanel.add(regBtn);
        navPanel.add(uploadBtn);
        
        // Right: User & Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel(student.getName());
        userLabel.setForeground(WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton logoutBtn = createTopBarButton("Logout");
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame(); });
        
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);
        
        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(navPanel, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(WHITE);
        btn.setBackground(new Color(60, 75, 100));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(new Color(100, 200, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(WHITE);
            }
        });
        
        return btn;
    }
    
    private JButton createTopBarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(BLUE);
        btn.setBackground(WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(240, 240, 240));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(WHITE);
            }
        });
        
        return btn;
    }
    
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Welcome Card
        JPanel welcomeCard = createCard();
        welcomeCard.setLayout(new BorderLayout(15, 15));
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + student.getName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(BLUE);
        
        JLabel subtitleLabel = new JLabel("Postgraduate Academic Research Seminar");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(GREY);
        
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);
        headerPanel.add(welcomeLabel);
        headerPanel.add(subtitleLabel);
        
        welcomeCard.add(headerPanel, BorderLayout.NORTH);
        
        // Status Card
        JPanel statusCard = createCard();
        statusCard.setLayout(new BorderLayout());
        statusCard.setBorder(BorderFactory.createCompoundBorder(
            statusCard.getBorder(),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel statusTitle = new JLabel("📊 Your Status & Results");
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusTitle.setForeground(BLUE);
        statusTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JTextArea statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        statusArea.setForeground(BLUE);
        statusArea.setBackground(new Color(245, 248, 250));
        statusArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        updateStatusArea(statusArea);
        
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setBorder(null);
        
        statusCard.add(statusTitle, BorderLayout.NORTH);
        statusCard.add(scrollPane, BorderLayout.CENTER);
        
        // Layout
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setOpaque(false);
        mainContent.add(welcomeCard, BorderLayout.NORTH);
        mainContent.add(statusCard, BorderLayout.CENTER);
        
        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(SHADOW);
                g2.fillRoundRect(3, 3, getWidth()-6, getHeight()-3, 15, 15);
                
                // Card
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, 15, 15);
                
                g2.dispose();
            }
        };
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        card.setOpaque(false);
        return card;
    }
    
    private void updateStatusArea(JTextArea statusArea) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═══ REGISTRATION DETAILS ═══\n");
        if (student.getResearchTitle() == null || student.getResearchTitle().isEmpty()) {
            sb.append("❌ Status: NOT REGISTERED\n");
            sb.append("→ Please complete registration to proceed.\n");
            statusArea.setText(sb.toString());
            return;
        }
        
        sb.append("✅ Status: REGISTERED\n");
        sb.append("Title:      ").append(student.getResearchTitle()).append("\n");
        sb.append("Supervisor: ").append(student.getSupervisorName()).append("\n");
        sb.append("Type:       ").append(student.getPresentationType()).append("\n");
        sb.append("Abstract:   ").append(student.getAbstractText()).append("\n\n");
        
        sb.append("═══ SESSION ASSIGNMENT ═══\n");
        DataStore db = DataStore.getInstance();
        Session assignedSession = null;
        for(Session s : db.sessions) {
            for(Student stu : s.getAssignedStudents()) {
                if(stu.getUserID().equals(student.getUserID())) assignedSession = s;
            }
        }
        
        if (assignedSession == null) {
            sb.append("⏳ Status: WAITING FOR ASSIGNMENT\n\n");
        } else {
            sb.append("✅ Status: ASSIGNED\n");
            sb.append("Date:   ").append(assignedSession.getDate()).append("\n");
            sb.append("Venue:  ").append(assignedSession.getVenue()).append("\n\n");
        }
        
        sb.append("═══ EVALUATION RESULTS ═══\n");
        Evaluation myEval = null;
        for(Evaluation e : db.evaluations) {
            if(e.getStudentId().equals(student.getUserID())) {
                myEval = e;
                break;
            }
        }
        
        if(myEval != null) {
            sb.append(myEval.toString());
            sb.append("\nComments: \"").append(myEval.getComments()).append("\"\n");
        } else {
            sb.append("⏳ Status: NO RESULTS YET\n");
        }
        
        statusArea.setText(sb.toString());
    }
    
    private void showPanel(String panelName) {
        if(panelName.equals("home")) {
            mainPanel.remove(0);
            mainPanel.add(createHomePanel(), "home", 0);
        }
        cardLayout.show(mainPanel, panelName);
    }
}