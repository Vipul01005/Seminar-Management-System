package gui;

import model.Student;
import controller.StudentController;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class StudentRegistrationPanel extends JPanel {
    private static final Color BLUE = new Color(44, 57, 82);
    private static final Color GREY = new Color(181, 176, 176);
    private static final Color WHITE = new Color(250, 247, 247);
    private static final Color SHADOW = new Color(0, 0, 0, 30);
    
    private Student student;
    private StudentController controller;
    
    private JTextField researchTitleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> presentationTypeCombo;
    private JButton registerButton;
    private JButton clearButton;
    private JLabel statusLabel;
    
    public StudentRegistrationPanel(Student student) {
        this.student = student;
        this.controller = new StudentController();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Main Card
        JPanel card = createCard();
        card.setLayout(new BorderLayout(20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("📝 Seminar Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(BLUE);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Research Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("Research Title"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        researchTitleField = createStyledTextField();
        if (student.getResearchTitle() != null) researchTitleField.setText(student.getResearchTitle());
        formPanel.add(researchTitleField, gbc);
        
        // Abstract
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createLabel("Abstract"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        abstractArea = new JTextArea(6, 30);
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        abstractArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        abstractArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        abstractArea.setBackground(WHITE);
        abstractArea.setForeground(BLUE);
        if (student.getAbstractText() != null) abstractArea.setText(student.getAbstractText());
        
        JScrollPane scrollPane = new JScrollPane(abstractArea);
        scrollPane.setBorder(new LineBorder(GREY, 1, true));
        formPanel.add(scrollPane, gbc);
        
        // Supervisor
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(createLabel("Supervisor Name"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        supervisorField = createStyledTextField();
        if (student.getSupervisorName() != null) supervisorField.setText(student.getSupervisorName());
        formPanel.add(supervisorField, gbc);
        
        // Presentation Type
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(createLabel("Presentation Type"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        presentationTypeCombo = createStyledComboBox(new String[]{"Oral", "Poster"});
        if (student.getPresentationType() != null) 
            presentationTypeCombo.setSelectedItem(student.getPresentationType());
        formPanel.add(presentationTypeCombo, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        registerButton = createModernButton("Register for Seminar", BLUE, WHITE);
        clearButton = createModernButton("Clear Form", GREY, WHITE);
        
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        
        // Status
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Assembly
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 15));
        bottomPanel.setOpaque(false);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(formPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        add(card, BorderLayout.CENTER);
        
        // Listeners
        registerButton.addActionListener(e -> handleRegistration());
        clearButton.addActionListener(e -> clearForm());
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
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(BLUE);
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(25);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(180, 40));
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
    
    private void handleRegistration() {
        String title = researchTitleField.getText().trim();
        String abstractText = abstractArea.getText().trim();
        String supervisor = supervisorField.getText().trim();
        String type = (String) presentationTypeCombo.getSelectedItem();
        
        if (title.isEmpty()) {
            showError("Please enter research title");
            return;
        }
        if (abstractText.isEmpty()) {
            showError("Please enter abstract");
            return;
        }
        if (supervisor.isEmpty()) {
            showError("Please enter supervisor name");
            return;
        }
        
        student.setResearchTitle(title);
        student.setAbstractText(abstractText);
        student.setSupervisorName(supervisor);
        student.setPresentationType(type);
        
        boolean success = controller.registerStudent(student);
        
        if (success) {
            statusLabel.setText("✅ Registration successful!");
            statusLabel.setForeground(new Color(0, 150, 0));
            JOptionPane.showMessageDialog(this,
                "Registration completed successfully!\nYou can now upload your presentation materials.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showError("Registration failed. Please try again.");
        }
    }
    
    private void clearForm() {
        researchTitleField.setText("");
        abstractArea.setText("");
        supervisorField.setText("");
        presentationTypeCombo.setSelectedIndex(0);
        statusLabel.setText(" ");
    }
    
    private void showError(String message) {
        statusLabel.setText("❌ " + message);
        statusLabel.setForeground(Color.RED);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void refresh() {
        if (student.getResearchTitle() != null) {
            researchTitleField.setText(student.getResearchTitle());
            abstractArea.setText(student.getAbstractText());
            supervisorField.setText(student.getSupervisorName());
            presentationTypeCombo.setSelectedItem(student.getPresentationType());
        }
    }
}