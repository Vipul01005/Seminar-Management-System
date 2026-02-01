package gui;

import controller.CoordinatorController;
import controller.AwardController;
import model.*;
import util.DataStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CoordinatorDashboard extends JFrame {
    private static final Color BLUE = new Color(44, 57, 82);
    private static final Color GREY = new Color(181, 176, 176);
    private static final Color WHITE = new Color(250, 247, 247);
    private static final Color SHADOW = new Color(0, 0, 0, 30);
    
    private CoordinatorController controller = new CoordinatorController();
    private AwardController awardController = new AwardController();
    
    private JComboBox<Evaluator> evalBox = new JComboBox<>();
    private JComboBox<Student> studBox = new JComboBox<>();
    private JList<Session> sessionList;
    private DefaultListModel<Session> listModel = new DefaultListModel<>();
    
    public CoordinatorDashboard(Coordinator user) {
        setTitle("Coordinator Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Background Panel with Gradient
        JPanel backgroundPanel = new JPanel() {
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
        backgroundPanel.setLayout(new BorderLayout());
        
        // Top Bar
        backgroundPanel.add(createTopBar(user), BorderLayout.NORTH);
        
        // Main Content
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Left Panel: Session Creation
        mainContent.add(createLeftPanel(), BorderLayout.WEST);
        
        // Center Panel: Session List
        mainContent.add(createCenterPanel(), BorderLayout.CENTER);
        
        // Right Panel: Assignment & Actions
        mainContent.add(createRightPanel(), BorderLayout.EAST);
        
        backgroundPanel.add(mainContent, BorderLayout.CENTER);
        add(backgroundPanel);
        
        refreshData();
        setVisible(true);
    }
    
    private JPanel createTopBar(Coordinator user) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BLUE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel titleLabel = new JLabel("🎓 Coordinator Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(WHITE);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel(user.getName());
        userLabel.setForeground(WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton logoutBtn = createTopBarButton("Logout");
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame(); });
        
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);
        
        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
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
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(240, 240, 240)); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(WHITE); }
        });
        
        return btn;
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(280, 0));
        
        JLabel title = new JLabel("➕ Create Session");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(BLUE);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 12));
        formPanel.setOpaque(false);
        
        JTextField dateField = createStyledField("Date (YYYY-MM-DD)");
        JTextField venueField = createStyledField("Venue");
        JComboBox<String> typeBox = createStyledCombo(new String[]{"Oral", "Poster"});
        
        JButton createBtn = createModernButton("Create Session", BLUE, WHITE);
        createBtn.addActionListener(e -> {
            controller.createSession(dateField.getText(), venueField.getText(), (String)typeBox.getSelectedItem());
            refreshData();
            dateField.setText("");
            venueField.setText("");
        });
        
        formPanel.add(dateField);
        formPanel.add(venueField);
        formPanel.add(typeBox);
        formPanel.add(createBtn);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel title = new JLabel("📋 Session List");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(BLUE);
        
        sessionList = new JList<>(listModel);
        sessionList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sessionList.setBackground(new Color(245, 248, 250));
        sessionList.setForeground(BLUE);
        sessionList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JScrollPane scrollPane = new JScrollPane(sessionList);
        scrollPane.setBorder(new LineBorder(GREY, 1, true));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(10, 15));
        panel.setPreferredSize(new Dimension(280, 0));
        
        JLabel title = new JLabel("⚙️ Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(BLUE);
        
        JPanel actionsPanel = new JPanel(new GridLayout(8, 1, 0, 12));
        actionsPanel.setOpaque(false);
        
        // Assignment Section
        JLabel assignLabel = new JLabel("Assign to Session:");
        assignLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        assignLabel.setForeground(BLUE);
        
        JButton assignEvalBtn = createModernButton("Assign Evaluator", GREY, WHITE);
        assignEvalBtn.addActionListener(e -> {
            Session s = sessionList.getSelectedValue();
            if(s != null) {
                controller.assignEvaluatorToSession(s, (Evaluator)evalBox.getSelectedItem());
                refreshData();
            }
        });
        
        JButton assignStudBtn = createModernButton("Add Student", GREY, WHITE);
        assignStudBtn.addActionListener(e -> {
            Session s = sessionList.getSelectedValue();
            Student stu = (Student)studBox.getSelectedItem();
            if(s != null && stu != null) {
                if(controller.addStudentToSession(s, stu)) {
                    JOptionPane.showMessageDialog(this, "Student assigned!");
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed (Conflict or Type Mismatch)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Report & Awards Section
        JButton reportBtn = createModernButton("📊 Generate Report", BLUE, WHITE);
        reportBtn.addActionListener(e -> {
            JTextArea area = new JTextArea(controller.generateReport());
            area.setRows(20);
            area.setColumns(50);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Session Report", JOptionPane.PLAIN_MESSAGE);
        });
        
        JButton awardBtn = createModernButton("🏆 View Awards", new Color(255, 165, 0), WHITE);
        awardBtn.addActionListener(e -> {
            String agenda = awardController.generateAwardAgenda();
            JTextArea area = new JTextArea(agenda);
            area.setRows(15);
            area.setColumns(40);
            area.setFont(new Font("Monospaced", Font.BOLD, 13));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Award Ceremony", JOptionPane.INFORMATION_MESSAGE);
        });
        
        actionsPanel.add(assignLabel);
        actionsPanel.add(evalBox);
        actionsPanel.add(assignEvalBtn);
        actionsPanel.add(studBox);
        actionsPanel.add(assignStudBtn);
        actionsPanel.add(new JLabel("")); // Spacer
        actionsPanel.add(reportBtn);
        actionsPanel.add(awardBtn);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(actionsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SHADOW);
                g2.fillRoundRect(3, 3, getWidth()-6, getHeight()-3, 15, 15);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, 15, 15);
                g2.dispose();
            }
        };
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);
        return card;
    }
    
    private JTextField createStyledField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(WHITE);
        field.setForeground(BLUE);
        field.setText(placeholder);
        field.setForeground(GREY);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if(field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(BLUE);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if(field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(GREY);
                }
            }
        });
        
        return field;
    }
    
    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
                
                if(hover) {
                    g2.setColor(SHADOW);
                    g2.fillRoundRect(2, 4, getWidth()-4, getHeight()-2, 20, 20);
                }
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, hover ? 0 : 2, getWidth(), getHeight()-(hover ? 2 : 4), 20, 20);
                
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y - (hover ? 1 : 0));
                
                g2.dispose();
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(0, 35));
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
        
        return btn;
    }
    
    private void refreshData() {
        listModel.clear();
        DataStore.getInstance().sessions.forEach(listModel::addElement);
        evalBox.removeAllItems();
        DataStore.getInstance().evaluators.forEach(evalBox::addItem);
        studBox.removeAllItems();
        DataStore.getInstance().students.forEach(studBox::addItem);
    }
}