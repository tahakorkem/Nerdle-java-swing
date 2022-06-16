package com.tahakorkem.nerdle.ui;

import com.tahakorkem.nerdle.util.TimeUtils;
import com.tahakorkem.nerdle.manager.GameManager;
import com.tahakorkem.nerdle.manager.StorageManager;
import com.tahakorkem.nerdle.data.Cell;
import com.tahakorkem.nerdle.data.State;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class GameScreen extends JFrame implements GameListener {
    private static final String TITLE = "Nerdle";
    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;
    private static final int GRID_GAP = 10;
    private static final int CELL_SIZE = 50;

    private static final Color COLOR_INITIAL = new Color(166, 166, 170);
    private static final Color COLOR_CORRECT = new Color(49, 163, 117);
    private static final Color COLOR_INCORRECT = new Color(234, 70, 70);
    private static final Color COLOR_PARTIALLY_CORRECT = new Color(252, 203, 26);
    private static final Color COLOR_BUTTON_INITIAL = new Color(232, 232, 240);

    private static final Font FONT_TIMER = new Font(null, Font.PLAIN, 20);
    private static final Font FONT_BUTTON = new Font(null, Font.BOLD, 14);
    private static final Font FONT_CELL = new Font(null, Font.BOLD, 18);

    private static final char[] DIGITS = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    private static final char[] OPERATIONS = {'+', '-', '*', '/', '='};

    private final StorageManager storageManager;
    private final GameManager gameManager;
    private JLabel[][] labels;
    private JLabel labelTimer;
    private final Map<Character, JButton> digitAndOperationButtonTable;

    /**
     * Sıfırdan başlanacak oyunlar için çağrılacak constructor.
     */
    public GameScreen() {
        this(new GameManager(), false);
    }

    /**
     * @param gameManager Oyun için gerekli nerdle.GameManager nesnesi
     * @param populate    Oyunun başlangıçta doldurulmasını istiyorsa true, değilse false
     */
    public GameScreen(GameManager gameManager, boolean populate) {
        // gerekli ayarlamalar yapılır
        super(TITLE);
        storageManager = new StorageManager();
        this.gameManager = gameManager;
        this.gameManager.setListener(this);
        // oyun zamanlayıcısını başlatır
        Timer timer = new Timer();
        timer.schedule(new Chronometer(gameManager, timer), 1000, 1000);
        digitAndOperationButtonTable = new HashMap<>();
        initialize();
        // oyunun başlangıçta doldurulması isteniyorsa
        // gerekli ayarlamalar yapılır
        if (populate) {
            populateGame();
        }
        // oyun başlatılır
        gameManager.startGame();
    }

    /**
     * Gerekli olan UI özelliklerini ayarlar.
     */
    private void initialize() {
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        labelTimer = new JLabel("00:00");
        labelTimer.setFont(FONT_TIMER);
        labelTimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(labelTimer);

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelGrid = new JPanel(new GridLayout(GameManager.ROW_COUNT, gameManager.getColumnCount(), GRID_GAP, GRID_GAP));
        int gridPanelWidth = gameManager.getColumnCount() * CELL_SIZE + (gameManager.getColumnCount() - 1) * GRID_GAP;
        int gridPanelHeight = GameManager.ROW_COUNT * CELL_SIZE + (GameManager.ROW_COUNT - 1) * GRID_GAP;
        Dimension gridPanelDimension = new Dimension(gridPanelWidth, gridPanelHeight);
        panelGrid.setMinimumSize(gridPanelDimension);
        panelGrid.setPreferredSize(gridPanelDimension);
        panelGrid.setMaximumSize(gridPanelDimension);
        contentPane.add(panelGrid);

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelNumbers = new JPanel();
        panelNumbers.setLayout(new BoxLayout(panelNumbers, BoxLayout.X_AXIS));
        contentPane.add(panelNumbers);

        for (char digit : DIGITS) {
            JButton btn = new JButton(String.valueOf(digit));
            btn.setFont(FONT_BUTTON);
            btn.setBackground(COLOR_BUTTON_INITIAL);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> onDigitOrOperationClick(digit));
            panelNumbers.add(btn);
            panelNumbers.add(Box.createRigidArea(new Dimension(5, 0)));
            digitAndOperationButtonTable.put(digit, btn);
        }

        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel panelOperations = new JPanel();
        panelOperations.setLayout(new BoxLayout(panelOperations, BoxLayout.X_AXIS));
        contentPane.add(panelOperations);

        for (char operation : OPERATIONS) {
            JButton btn = new JButton(String.valueOf(operation));
            btn.setFont(FONT_BUTTON);
            btn.setBackground(COLOR_BUTTON_INITIAL);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> onDigitOrOperationClick(operation));
            panelOperations.add(btn);
            panelOperations.add(Box.createRigidArea(new Dimension(5, 0)));
            digitAndOperationButtonTable.put(operation, btn);
        }

        JButton btnGuess = new JButton("TAHMİN ET");
        btnGuess.setFont(FONT_BUTTON);
        btnGuess.setBackground(COLOR_BUTTON_INITIAL);
        btnGuess.setFocusPainted(false);
        btnGuess.addActionListener(e -> onGuessButtonClick());
        panelOperations.add(btnGuess);

        panelOperations.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton btnClear = new JButton("SİL");
        btnClear.setFont(FONT_BUTTON);
        btnClear.setBackground(COLOR_BUTTON_INITIAL);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> onClearButtonClick());
        panelOperations.add(btnClear);

        panelOperations.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton btnFinishLater = new JButton("SONRA BİTİR");
        btnFinishLater.setFont(FONT_BUTTON);
        btnFinishLater.setBackground(COLOR_BUTTON_INITIAL);
        btnFinishLater.setFocusPainted(false);
        btnFinishLater.addActionListener(e -> onFinishLaterButtonClick());
        panelOperations.add(btnFinishLater);

        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPane.add(Box.createVerticalGlue());

        JLabel labelFooter = new JLabel(AppScreen.FOOTER_TEXT);
        labelFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(labelFooter);

        contentPane.add(Box.createRigidArea(new Dimension(0, 15)));

        labels = new JLabel[GameManager.ROW_COUNT][gameManager.getColumnCount()];

        for (int i = 0; i < GameManager.ROW_COUNT; i++) {
            for (int j = 0; j < gameManager.getColumnCount(); j++) {
                int row = i;
                int column = j;
                JLabel label = new JLabel();
                label.setFont(FONT_CELL);
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
                label.setBackground(COLOR_INITIAL);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        onCellClick(row, column);
                    }
                });
                panelGrid.add(label);
                labels[i][j] = label;
            }
        }
    }

    /**
     * Oyunu başlangıçta doldurur.
     * nerdle.GameManager objesi kullanılarak ilk değişiklikler yapılır.
     * Tüm hücreler doğru renklerle doldurulur.
     * Seçili olan hğcre varsa seçili hale getirilir.
     * */
    private void populateGame() {
        // zaman bilgisi yazdırılır
        onTimeChange(gameManager.getTimeInSeconds());
        // tam dolu hücreler gerekli değerlerle doldurulur
        for (int i = 0; i < gameManager.getActiveRow(); i++) {
            for (int j = 0; j < gameManager.getColumnCount(); j++) {
                Cell c = gameManager.getCells()[i][j];
                onTextChange(i, j, c.getText());
                onStateChange(i, j, c.getState());
            }
        }
        // son satır doldurulur
        for (int j = 0; j < gameManager.getColumnCount(); j++) {
            Cell c = gameManager.getCells()[gameManager.getActiveRow()][j];
            onTextChange(gameManager.getActiveRow(), j, c.getText());
        }
        // varsa seçili hücre tekrar seçilir
        if (gameManager.getSelectedColumn() >= 0) {
            onCellSelectionChange(gameManager.getActiveRow(), gameManager.getSelectedColumn(), true);
        }
    }

    /**
     * Oyun kapatıldığında çalışır.
     * */
    private void onClose() {
        // oyun devam etmiyorsa, kullanıcı ya oyunu kazanmıştır ya da kaybetmiştir
        // bu durumlarda oyun yarıda bırakılmış kabul edilmemektedir
        if (!gameManager.isInGame()) {
            return;
        }
        try {
            // yarıda bırakılan oyun sayısına ekleme yapılır
            storageManager.getStats().insertInterruptedGame();
            storageManager.saveStats();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onClearButtonClick() {
        gameManager.clearSelectedText();
    }

    private void onGuessButtonClick() {
        gameManager.guess();
    }

    private void onDigitOrOperationClick(char c) {
        gameManager.setSelectedText(c);
    }

    private void onCellClick(int row, int column) {
        gameManager.selectCell(row, column);
    }

    private void onFinishLaterButtonClick() {
        try {
            // rastgele üretilmiş denklemi,
            // şimdiye kadar onaylanarak girilmiş satırları,
            // seçili olan hücreyi ve
            // oyunun son süresini
            // içeren nerdle.GameManager objesi dosyaya kaydedilir
            gameManager.stopGame();
            storageManager.saveGame(gameManager);
            dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStateChange(int row, int column, State newState) {
        // hücrenin rengi gerekli şekilde değiştirilir
        // aynı şekilde hücrenin içeriğiyle aynı içeriğe sahip butonun
        // renkleri de gerekiyorsa değiştirilir
        Color color = COLOR_INITIAL;
        char c = gameManager.charAt(row, column);
        JButton btn = digitAndOperationButtonTable.get(c);
        Color btnBg = btn.getBackground();
        boolean changeButtonBg = true;
        switch (newState) {
            case CORRECT:
                color = COLOR_CORRECT;
                break;
            case PARTIALLY_CORRECT:
                color = COLOR_PARTIALLY_CORRECT;
                changeButtonBg = btnBg != COLOR_CORRECT;
                break;
            case INCORRECT:
                color = COLOR_INCORRECT;
                changeButtonBg = btnBg != COLOR_CORRECT && btnBg != COLOR_PARTIALLY_CORRECT;
                break;
        }
        labels[row][column].setBackground(color);
        if (changeButtonBg) {
            btn.setBackground(color);
            btn.setForeground(Color.WHITE);
        }
    }

    @Override
    public void onCellSelectionChange(int row, int column, boolean isSelected) {
        // seçili ya da seçili olmayan hücrenin arkaplanı değiştirilir
        Border border = isSelected ? new LineBorder(Color.BLACK, 2) : null;
        labels[row][column].setBorder(border);
    }

    @Override
    public void onTextChange(int row, int column, char newText) {
        // hücrenin içindeki yazı değiştirilir
        labels[row][column].setText(String.valueOf(newText));
    }

    @Override
    public void onTimeChange(int newTimeInSeconds) {
        labelTimer.setText(TimeUtils.format(newTimeInSeconds));
    }

    @Override
    public void onAlert(String message) {
        // gerekli mesajlar ekranda dialog olarak gösterilir
        JOptionPane.showMessageDialog(null, message, "Uyarı", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void onWin(int totalTryCount, int totalElapsedTime) {
        try {
            // kazanılan oyun sayısına ekleme yapılır
            // toplam oyun süresi ve deneme sayısı da ortalamaya katılır
            // ve varsa kayıtlı oyun silinir
            storageManager.getStats().insertWonGame(totalTryCount, totalElapsedTime);
            storageManager.saveStats();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String formattedTime = TimeUtils.format(totalElapsedTime);

        // oyunun sonucu ekranda dialog olarak gösterilir
        Object[] choices = {"Anasayfa"};
        Object defaultChoice = choices[0];
        int selectedOption = JOptionPane.showOptionDialog(null,
                formattedTime + " sürede " + totalTryCount + " tahminde oyunu kazandınız!",
                "Tebrikler",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                defaultChoice);

        if (selectedOption == 0) {
            // anasayfaya gidilir
            new AppScreen().setVisible(true);
        }
        dispose();
    }

    @Override
    public void onLose(String actualEquation) {
        try {
            // kaybedilen oyun sayısına ekleme yapılır
            // ve varsa kayıtlı oyun silinir
            storageManager.getStats().insertLostGame();
            storageManager.saveStats();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // oyunun sonucu ekranda dialog olarak gösterilir
        JOptionPane.showMessageDialog(null, "Maalesef kaybettiniz!\nDenklem şu idi: " + actualEquation, "Oyun bitti", JOptionPane.ERROR_MESSAGE);
        dispose();
    }


}
