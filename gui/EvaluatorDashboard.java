package gui;

import model.*;
import util.DataStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class EvaluatorDashboard extends JFrame {
    private static final Color BLUE = new Color(44, 57, 82);
    private static final Color GREY = new Color(181, 176, 176);
    private static final Color WHITE = new Color(250, 247, 247);
    private static final Color SHADOW = new Color(0, 0, 0, 30);
    
    private Evaluator evaluator;
    private JComboBox<Student> studentBox = new JComboBox<>();
    private JSlider[] sliders = new JSlider[4];
    private JTextArea commentArea = new JTextArea(5, 30);
    private JTextField fileInfoField = new JTextField("No student selected");
    private JButton openFileBtn;
    
    public EvaluatorDashboard(Evaluator user) {
        this.evaluator = user;
        setTitle("Evaluator Dashboard");
        setSize(1000, 850);
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
        mainContent.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        // Card Container
        JPanel card = createCard();
        card.setLayout(new BorderLayout(20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("📊 Student Evaluation Panel");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(BLUE);
        
        // Student Selection Panel
        JPanel selectionPanel = createSelectionPanel();
        
        // Rubrics Panel
        JPanel rubricsPanel = createRubricsPanel();
        
        // Comments Panel
        JPanel commentsPanel = createCommentsPanel();
        
        // Submit Button
        JButton submitBtn = createModernButton("Submit Evaluation", BLUE, WHITE);
        submitBtn.setPreferredSize(new Dimension(200, 45));
        submitBtn.addActionListener(e -> handleSubmit());
        
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.setOpaque(false);
        submitPanel.add(submitBtn);
        
        // Assemble Card
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(selectionPanel, BorderLayout.NORTH);
        centerPanel.add(rubricsPanel, BorderLayout.CENTER);
        centerPanel.add(commentsPanel, BorderLayout.SOUTH);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(submitPanel, BorderLayout.SOUTH);
        
        mainContent.add(card, BorderLayout.CENTER);
        backgroundPanel.add(mainContent, BorderLayout.CENTER);
        add(backgroundPanel);
        
        loadAssignedStudents();
        if(studentBox.getItemCount() > 0) updateFileInfo();
        
        setVisible(true);
    }
    
    private JPanel createTopBar(Evaluator user) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BLUE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel titleLabel = new JLabel("🎯 Evaluator Dashboard");
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
    
    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(GREY, 1, true),
                " Select Student ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                BLUE
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Student ComboBox
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel studentLabel = new JLabel("Student:");
        studentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        studentLabel.setForeground(BLUE);
        panel.add(studentLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        studentBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentBox.setBackground(WHITE);
        studentBox.setForeground(BLUE);
        studentBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        studentBox.addActionListener(e -> updateFileInfo());
        panel.add(studentBox, gbc);
        
        // File Info
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel fileLabel = new JLabel("File:");
        fileLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fileLabel.setForeground(BLUE);
        panel.add(fileLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        fileInfoField.setEditable(false);
        fileInfoField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        fileInfoField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        fileInfoField.setBackground(new Color(245, 248, 250));
        fileInfoField.setForeground(GREY);
        panel.add(fileInfoField, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        openFileBtn = createModernButton("Open File", GREY, WHITE);
        openFileBtn.setPreferredSize(new Dimension(110, 35));
        openFileBtn.setEnabled(false);
        openFileBtn.addActionListener(e -> openFile());
        panel.add(openFileBtn, gbc);
        
        return panel;
    }
    
    private JPanel createRubricsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(GREY, 1, true),
                " Evaluation Rubrics (1-5) ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                BLUE
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String[] labels = {"Problem Clarity", "Methodology", "Results", "Presentation"};
        
        for(int i = 0; i < 4; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(BLUE);
            
            sliders[i] = new JSlider(1, 5);
            sliders[i].setMajorTickSpacing(1);
            sliders[i].setPaintTicks(true);
            sliders[i].setPaintLabels(true);
            sliders[i].setBackground(WHITE);
            sliders[i].setForeground(BLUE);
            
            panel.add(label);
            panel.add(sliders[i]);
        }
        
        return panel;
    }
    
    private JPanel createCommentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(GREY, 1, true),
                " Comments ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                BLUE
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        commentArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        commentArea.setBackground(WHITE);
        commentArea.setForeground(BLUE);
        
        JScrollPane scrollPane = new JScrollPane(commentArea);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(0, 120));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
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
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        card.setOpaque(false);
        return card;
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
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setBackground(bg);
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
    
    private void loadAssignedStudents() {
        studentBox.removeAllItems();
        Set<String> addedIds = new HashSet<>();
        
        for(Session s : DataStore.getInstance().sessions) {
            if(s.getAssignedEvaluator() != null && 
               s.getAssignedEvaluator().getUserID().equals(evaluator.getUserID())) {
                for(Student stu : s.getAssignedStudents()) {
                    if(!addedIds.contains(stu.getUserID())) {
                        studentBox.addItem(stu);
                        addedIds.add(stu.getUserID());
                    }
                }
            }
        }
        
        if(studentBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "You have not been assigned to any sessions yet.");
        }
    }
    
    private void updateFileInfo() {
        Student s = (Student) studentBox.getSelectedItem();
        if (s == null) return;
        
        fileInfoField.setText("No file uploaded");
        fileInfoField.setForeground(Color.RED);
        openFileBtn.setEnabled(false);
        
        if (s.getSubmission() != null) {
            String path = s.getPresentationType().equals("Oral") 
                ? s.getSubmission().getSlidePath() 
                : s.getSubmission().getPosterPath();
                
            if (path != null && !path.trim().isEmpty()) {
                fileInfoField.setText(path);
                fileInfoField.setForeground(new Color(0, 120, 0));
                openFileBtn.setEnabled(true);
            }
        }
    }
    
    private void openFile() {
        String path = fileInfoField.getText();
        try {
            File file = new File(path);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this, "File not found!\nPath: " + path, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleSubmit() {
        Student s = (Student) studentBox.getSelectedItem();
        if(s != null) {
            Evaluation eval = new Evaluation(
                s.getUserID(),
                evaluator.getUserID(),
                sliders[0].getValue(),
                sliders[1].getValue(),
                sliders[2].getValue(),
                sliders[3].getValue(),
                commentArea.getText()
            );
            
            DataStore.getInstance().evaluations.removeIf(ev -> ev.getStudentId().equals(s.getUserID()));
            DataStore.getInstance().evaluations.add(eval);
            
            JOptionPane.showMessageDialog(this, "✅ Evaluation Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}