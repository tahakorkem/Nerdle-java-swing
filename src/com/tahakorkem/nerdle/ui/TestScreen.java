package com.tahakorkem.nerdle.ui;

import com.tahakorkem.nerdle.util.EquationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestScreen extends JFrame {
    private static final String TITLE = "Test";
    private static final Color COLOR_BUTTON = new Color(232, 232, 240);
    private static final Font FONT_BUTTON = new Font(null, Font.BOLD, 16);
    private static final Font FONT_LABEL = new Font(null, Font.PLAIN, 24);
    private JLabel lblEquation;

    public TestScreen() {
        super(TITLE);
        initialize();
        regenerate();
    }

    /**
     * Gerekli olan UI özelliklerini ayarlar.
     * */
    private void initialize() {
        setPreferredSize(new Dimension(GameScreen.APP_WIDTH, GameScreen.APP_HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        contentPane.add(Box.createVerticalGlue());

        lblEquation = new JLabel();
        lblEquation.setFont(FONT_LABEL);
        lblEquation.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(lblEquation);

        contentPane.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnRegenerate = new JButton("Yeniden Üret");
        btnRegenerate.setFont(FONT_BUTTON);
        btnRegenerate.setBackground(COLOR_BUTTON);
        btnRegenerate.setFocusPainted(false);
        btnRegenerate.addActionListener(e -> regenerate());
        btnRegenerate.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(btnRegenerate);

        contentPane.add(Box.createVerticalGlue());

        JLabel labelFooter = new JLabel(AppScreen.FOOTER_TEXT);
        labelFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(labelFooter);

        contentPane.add(Box.createRigidArea(new Dimension(0, 30)));
    }

    private void onClose() {
        // anasayfaya gidilir
        new AppScreen().setVisible(true);
    }

    /**
     * Denklemi yeniden oluşturur.
     * */
    private void regenerate() {
        String equation = EquationUtils.generateEquation();
        lblEquation.setText(equation);
    }
}
