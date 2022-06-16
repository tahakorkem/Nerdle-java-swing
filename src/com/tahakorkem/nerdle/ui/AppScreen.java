package com.tahakorkem.nerdle.ui;

import com.tahakorkem.nerdle.manager.GameManager;
import com.tahakorkem.nerdle.manager.StorageManager;

import javax.swing.*;
import java.awt.*;

public class AppScreen extends JFrame {
    private final static String TITLE = "Oyun";
    private static final Color COLOR_BUTTON = new Color(232, 232, 240);
    private static final Font FONT_BUTTON = new Font(null, Font.BOLD, 24);
    private static final Font FONT_STATISTICS_LABEL = new Font(null, Font.PLAIN, 16);
    private static final Font FONT_STATISTICS_TEXT = new Font(null, Font.BOLD, 16);
    public static final String FOOTER_TEXT = "Taha Körkem - 2022";

    private final StorageManager storageManager;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AppScreen frame = new AppScreen();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AppScreen() {
        super(TITLE);
        storageManager = new StorageManager();
        initialize();
    }

    /**
     * Gerekli olan UI özelliklerini ayarlar.
     * */
    private void initialize() {
        setPreferredSize(new Dimension(GameScreen.APP_WIDTH, GameScreen.APP_HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        contentPane.add(Box.createRigidArea(new Dimension(0, 30)));

        if (storageManager.hasSavedGame()) {
            JButton btnContinue = new JButton("DEVAM ET");
            btnContinue.setFont(FONT_BUTTON);
            btnContinue.setBackground(COLOR_BUTTON);
            btnContinue.setFocusPainted(false);
            btnContinue.addActionListener(e -> onContinueButtonClick());
            btnContinue.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(btnContinue);

            contentPane.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JButton btnNewGame = new JButton("YENİ OYUN");
        btnNewGame.setFont(FONT_BUTTON);
        btnNewGame.setBackground(COLOR_BUTTON);
        btnNewGame.setFocusPainted(false);
        btnNewGame.addActionListener(e -> onNewGameButtonClick());
        btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(btnNewGame);

        contentPane.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnTest = new JButton("TEST");
        btnTest.setFont(FONT_BUTTON);
        btnTest.setBackground(COLOR_BUTTON);
        btnTest.setFocusPainted(false);
        btnTest.addActionListener(e -> onTestButtonClick());
        btnTest.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(btnTest);

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelStatistics = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        //panelStatistics.setBorder(new LineBorder(Color.RED));
        //panelStatistics.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPane.add(panelStatistics);

        JLabel lbl1 = new JLabel("Yarıda Bırakılan Oyunlar: ");
        lbl1.setHorizontalAlignment(SwingConstants.LEFT);
        lbl1.setFont(FONT_STATISTICS_LABEL);
        c.gridx = 0;
        c.gridy = 0;
        panelStatistics.add(lbl1, c);

        JLabel lbl2 = new JLabel(storageManager.getStats().getInterruptedGameCount());
        lbl2.setFont(FONT_STATISTICS_TEXT);
        lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridx = 1;
        c.gridy = 0;
        panelStatistics.add(lbl2, c);

        JLabel lbl3 = new JLabel("Başarısız Olunan Oyunlar: ");
        lbl3.setHorizontalAlignment(SwingConstants.LEFT);
        lbl3.setFont(FONT_STATISTICS_LABEL);
        c.gridx = 0;
        c.gridy = 1;
        panelStatistics.add(lbl3, c);

        JLabel lbl4 = new JLabel(storageManager.getStats().getLostGameCount());
        lbl4.setFont(FONT_STATISTICS_TEXT);
        lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridx = 1;
        c.gridy = 1;
        panelStatistics.add(lbl4, c);

        JLabel lbl5 = new JLabel("Kazanılan Oyunlar: ");
        lbl5.setHorizontalAlignment(SwingConstants.LEFT);
        lbl5.setFont(FONT_STATISTICS_LABEL);
        c.gridx = 0;
        c.gridy = 2;
        panelStatistics.add(lbl5, c);

        JLabel lbl6 = new JLabel(storageManager.getStats().getWonGameCount());
        lbl6.setFont(FONT_STATISTICS_TEXT);
        lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridx = 1;
        c.gridy = 2;
        panelStatistics.add(lbl6, c);

        JLabel lbl7 = new JLabel("Ortalama Oyun Süresi: ");
        lbl7.setHorizontalAlignment(SwingConstants.LEFT);
        lbl7.setFont(FONT_STATISTICS_LABEL);
        c.gridx = 0;
        c.gridy = 3;
        panelStatistics.add(lbl7, c);

        JLabel lbl8 = new JLabel(storageManager.getStats().getWonGameTimeAverage());
        lbl8.setFont(FONT_STATISTICS_TEXT);
        lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridx = 1;
        c.gridy = 3;
        panelStatistics.add(lbl8, c);

        JLabel lbl9 = new JLabel("Ortalama Satır Sayısı: ");
        lbl9.setHorizontalAlignment(SwingConstants.LEFT);
        lbl9.setFont(FONT_STATISTICS_LABEL);
        c.gridx = 0;
        c.gridy = 4;
        panelStatistics.add(lbl9, c);

        JLabel lbl10 = new JLabel(storageManager.getStats().getWonGameTryAverage());
        lbl10.setFont(FONT_STATISTICS_TEXT);
        lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridx = 1;
        c.gridy = 4;
        panelStatistics.add(lbl10, c);

        contentPane.add(Box.createVerticalGlue());

        JLabel labelFooter = new JLabel(FOOTER_TEXT);
        labelFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(labelFooter);

        contentPane.add(Box.createRigidArea(new Dimension(0, 30)));
    }

    private void onContinueButtonClick() {
        // oyun ekranı oyun verisiyle beraber açılır
        GameManager gameManager = storageManager.getSavedGame();
        new GameScreen(gameManager, true).setVisible(true);
        dispose();
    }

    private void onNewGameButtonClick() {
        // oyun ekranı sıfırdan açılır
        new GameScreen().setVisible(true);
        dispose();
    }

    private void onTestButtonClick() {
        // test ekranı açılır
        new TestScreen().setVisible(true);
        dispose();
    }
}
