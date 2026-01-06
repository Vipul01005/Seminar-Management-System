package gui;

import model.Student;
import controller.StudentController;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class StudentUploadPanel extends JPanel {
    private static final Color BLUE = new Color(44, 57, 82);
    private static final Color GREY = new Color(181, 176, 176);
    private static final Color WHITE = new Color(250, 247, 247);
    private static final Color SHADOW = new Color(0, 0, 0, 30);
    
    private Student student;
    private StudentController controller;
    private JLabel presentationTypeLabel;
    private JTextField filePathField;
    private JButton browseButton;
    private JButton uploadButton;
    private JLabel statusLabel;
    private JTextArea infoArea;
    
    public StudentUploadPanel(Student student) {
        this.student = student;
        this.controller = new StudentController();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Main Card
        JPanel card = createCard();
        card.setLayout(new BorderLayout(20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("📤 Upload Presentation Materials");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(BLUE);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(GREY, 1, true),
                " Your Registration Details ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                BLUE
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        infoArea = new JTextArea(8, 40);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setBackground(new Color(245, 248, 250));
        infoArea.setForeground(BLUE);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        updateInfoArea();
        
        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setBorder(null);
        infoPanel.add(infoScroll, BorderLayout.CENTER);
        
        // Upload Panel
        JPanel uploadPanel = new JPanel(new GridBagLayout());
        uploadPanel.setOpaque(false);
        uploadPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(GREY, 1, true),
                " Upload File ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                BLUE
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Presentation Type
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel typeLabel = new JLabel("Presentation Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(BLUE);
        uploadPanel.add(typeLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        presentationTypeLabel = new JLabel();
        presentationTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        presentationTypeLabel.setForeground(BLUE);
        uploadPanel.add(presentationTypeLabel, gbc);
        
        // File Path
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel pathLabel = new JLabel("File Path:");
        pathLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pathLabel.setForeground(BLUE);
        uploadPanel.add(pathLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        filePathField = new JTextField(30);
        filePathField.setEditable(false);
        filePathField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        filePathField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GREY, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        filePathField.setBackground(new Color(245, 248, 250));
        filePathField.setForeground(BLUE);
        uploadPanel.add(filePathField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseButton = createModernButton("Browse...", GREY, WHITE);
        browseButton.setPreferredSize(new Dimension(120, 35));
        uploadPanel.add(browseButton, gbc);
        
        // Upload Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER;
        uploadButton = createModernButton("Upload Materials", BLUE, WHITE);
        uploadButton.setPreferredSize(new Dimension(200, 40));
        uploadPanel.add(uploadButton, gbc);
        
        // Status
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Assembly
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(infoPanel, BorderLayout.CENTER);
        centerPanel.add(uploadPanel, BorderLayout.SOUTH);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(statusLabel, BorderLayout.SOUTH);
        
        add(card, BorderLayout.CENTER);
        
        // Listeners
        browseButton.addActionListener(e -> browseFile());
        uploadButton.addActionListener(e -> handleUpload());
        
        updateUIBasedOnType();
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
    
    private void updateUIBasedOnType() {
        if (student.getPresentationType() == null) {
            presentationTypeLabel.setText("⚠️ Not registered yet");
            filePathField.setEnabled(false);
            browseButton.setEnabled(false);
            uploadButton.setEnabled(false);
            statusLabel.setText("❌ Please complete registration first");
            statusLabel.setForeground(Color.RED);
        } else {
            presentationTypeLabel.setText("✅ " + student.getPresentationType());
            filePathField.setEnabled(true);
            browseButton.setEnabled(true);
            uploadButton.setEnabled(true);
            
            if (student.getSubmission() != null) {
                String path = student.getPresentationType().equals("Oral") 
                    ? student.getSubmission().getSlidePath()
                    : student.getSubmission().getPosterPath();
                if (path != null) {
                    filePathField.setText(path);
                    statusLabel.setText("✅ Materials already uploaded");
                    statusLabel.setForeground(new Color(0, 150, 0));
                }
            }
        }
    }
    
    private void updateInfoArea() {
        StringBuilder info = new StringBuilder();
        info.append("Student: ").append(student.getName()).append("\n");
        info.append("Student ID: ").append(student.getUserID()).append("\n\n");
        
        if (student.getResearchTitle() != null) {
            info.append("Research Title: ").append(student.getResearchTitle()).append("\n\n");
            info.append("Supervisor: ").append(student.getSupervisorName()).append("\n\n");
            info.append("Presentation Type: ").append(student.getPresentationType()).append("\n\n");
            info.append("Abstract:\n").append(student.getAbstractText());
        } else {
            info.append("⚠️ Registration not completed yet.\n");
            info.append("Please complete registration before uploading materials.");
        }
        
        infoArea.setText(info.toString());
    }
    
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Presentation File");
        
        if (student.getPresentationType().equals("Oral")) {
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Presentation Files (*.ppt, *.pptx, *.pdf)", "ppt", "pptx", "pdf"));
        } else {
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Poster Files (*.pdf, *.png, *.jpg)", "pdf", "png", "jpg", "jpeg"));
        }
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            statusLabel.setText("📁 File selected: " + selectedFile.getName());
            statusLabel.setForeground(BLUE);
        }
    }
    
    private void handleUpload() {
        String filePath = filePathField.getText().trim();
        
        if (filePath.isEmpty()) {
            showError("Please select a file to upload");
            return;
        }
        
        File file = new File(filePath);
        if (!file.exists()) {
            showError("Selected file does not exist");
            return;
        }
        
        boolean success = controller.uploadMaterials(student, filePath);
        
        if (success) {
            statusLabel.setText("✅ Materials uploaded successfully!");
            statusLabel.setForeground(new Color(0, 150, 0));
            JOptionPane.showMessageDialog(this,
                "Your presentation materials have been uploaded successfully!\nFile: " + file.getName(),
                "Upload Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showError("Upload failed. Please try again.");
        }
    }
    
    private void showError(String message) {
        statusLabel.setText("❌ " + message);
        statusLabel.setForeground(Color.RED);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void refresh() {
        updateUIBasedOnType();
        updateInfoArea();
    }
}