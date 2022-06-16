package com.tahakorkem.nerdle.manager;

import com.tahakorkem.nerdle.ui.GameListener;
import com.tahakorkem.nerdle.data.Cell;
import com.tahakorkem.nerdle.util.EquationUtils;
import com.tahakorkem.nerdle.data.State;

import java.io.Serializable;
import java.util.Map;

public class GameManager implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FILE_NAME = "game";
    /**
     * Oyun alanının toplam satır sayısı
     */
    public static final int ROW_COUNT = 6;
    /**
     * Oyunda seçili olan, işlem yapılabilen satır
     */
    private int activeRow = 0;
    /**
     * Aktif olan satırda seçili olan sütun,
     * -1 değerine sahipse herhangi bir hücre seçili değil
     */
    private int selectedColumn = -1;
    /**
     * Oyun boyunca kullanılan hücrelerin tutulduğu matris
     * */
    private final Cell[][] cells;
    /**
     * Oyun alanının toplam sütun sayısı,
     * bu değer denklem uzunluğuna göre belirlenir
     */
    private final int columnCount;
    /**
     * Hücrelerde yapılan değişikliklerin ve kullanıcıya bildirilecek olayları bildirmek için kullanılan
     * bir callback interface'i
     */
    transient private GameListener listener;
    /**
     * Oyun başladığından beri geçen saniye bazında süre
     */
    private int timeInSeconds = 0;
    /**
     * Oyunun devam ediyor olup olmadığı bilgisi
     */
    private boolean isInGame = true;
    /**
     * Oyunun başında üretilen, kullanıcının tahmin etmeye çalışacağı asıl denklem
     */
    private final String actualEquation;

    public GameManager() {
        // oyunun başında rastgele bir denklem oluşturulur
        actualEquation = EquationUtils.generateEquation();
        // bu denklemin uzunuğu sütun sayısı olarak kullanılacak
        this.columnCount = actualEquation.length();
        // oyunun içinde tutulacak hücreler en başta boş olacak şekilde oluşturulur
        cells = new Cell[ROW_COUNT][columnCount];
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < columnCount; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    /**
     * Belirli bir hücreye tıklandığında çalıştırılır.
     * Seçilmek istenen hücre aktif olan satırda olmalıdır.
     */
    public void selectCell(int row, int column) {
        if (activeRow != row) {
            return;
        }
        setSelectedColumn(column);
    }

    /**
     * Kullanıcı bir tahminde bulunduğunda çalıştırılır.
     */
    public void guess() {
        // oyun bitmişse çalıştırılmaz
        if (!isInGame) {
            return;
        }
        // aktif satırın tüm hücreleri dolu değilse çalıştırılmaz
        if (!checkIfAllFilled()) {
            listener.onAlert("Tüm hücreler doldurulmalıdır!");
            return;
        }
        // girilen denklem geçerli değilse çalıştırılmaz
        if (!checkIfEquationValid()) {
            listener.onAlert("Yazılan denklem geçerli değil!");
            return;
        }

        // seçili olan hücreyi temizle
        clearSelectedColumn();

        // kullanıcının tahminde bulunduğu hücrelerin doğru yerlerinde olup olmadığını kontrol eder
        boolean isAllCorrect = checkAllCells();

        // tüm hücreler doğru yerinde ise oyunu kazanır
        if (isAllCorrect) {
            win();
            return;
        }

        // son satırda denklem bilinmediyse oyunu kaybeder
        if (++activeRow == ROW_COUNT) {
            lose();
            return;
        }

        // yeni geçilen satırın ilk hücresini seçer
        setSelectedColumn(0);
    }

    /**
     * Kullanıcı oyunu kazandığında çalıştırılır.
     */
    private void win() {
        stopGame();
        listener.onWin(activeRow + 1, timeInSeconds);
    }

    /**
     * Tüm hücrelerin doğru olup olmadığını kontrol eder.
     * İlk olarak doğru olan hücrelere bakılır.
     * Eğer tümü doğru yerindeyse sonuç true döner.
     * Bir tane bile doğru yerde değilse false döndürmek üzere ikinci aşamaya geçilir.
     * İkinci aşamada sadece doğru olmayan hücrelere bakılır.
     * Yeri yanlış ama denklemde bulunan hücreler için kısmen doğru,
     * deklemde daha hiç geçmeyen hücreler için ise yanlış etiketi verilir.
     *
     * @return tüm hücrelerin doğru yerlerinde olup olmadığı
     */
    private boolean checkAllCells() {
        // asıl denklemde hangi karakterin ne sıklıkta yer aldığı tablosu oluşturulur
        Map<Character, Integer> frequencyTable = EquationUtils.generateFrequencyTable(actualEquation);

        boolean isAllCorrect = true;

        // doğru olan hücreleri kontrol eder
        for (int i = 0; i < columnCount; i++) {
            char c = cells[activeRow][i].getText();
            if (c == getEquationCharAt(i)) {
                // doğru olan hücrelerin metin değerlerinin
                // sıklık tablosundan sıklığı azaltılır
                frequencyTable.put(c, frequencyTable.get(c) - 1);
                // doğru olduğu tespit edilen hücrenin doğru olduğu bildirilir
                listener.onStateChange(activeRow, i, State.CORRECT);
                cells[activeRow][i].setState(State.CORRECT);
            } else {
                // doğru olmayan bir hücreye denk gelindiğinde isAllCorrect false yapılır
                isAllCorrect = false;
            }
        }
        // tüm hücreler doğru yerinde ise true döner
        if (isAllCorrect) {
            return true;
        }
        // tüm hücreler doğru yerinde değil ise
        // doğru olmayan hücreleri kontrol eder
        for (int i = 0; i < columnCount; i++) {
            char c = cells[activeRow][i].getText();
            if (c != getEquationCharAt(i)) {
                // doğru olmayan hücrelerin sıklık tablosunda yer alıp almadıklarını kontrol eder
                int frequency = frequencyTable.getOrDefault(c, 0);
                State state;
                // sıklık tablosunda daha geçmeyen hücreler için yanlış etiketi verilir
                // geçenler için ise kısmen doğru etiketi verilir ve sıklık tablosundaki sıklığı azaltılır
                if (frequency == 0) {
                    state = State.INCORRECT;
                } else {
                    state = State.PARTIALLY_CORRECT;
                    frequencyTable.put(c, frequency - 1);
                }
                // hücrenin etiket bilgisi bildirilir
                listener.onStateChange(activeRow, i, state);
                cells[activeRow][i].setState(state);
            }
        }

        // tüm hücreler doğru olmadığı için false döner
        return false;
    }

    /**
     * Asıl denklemin belirli bir karakterini döndürür.
     */
    private char getEquationCharAt(int i) {
        return actualEquation.charAt(i);
    }

    /**
     * Aktif olan satırdaki tüm hücrelerin doldurulup doldurulmadığını kontrol eder.
     */
    private boolean checkIfAllFilled() {
        for (int i = 0; i < columnCount; i++) {
            if (cells[activeRow][i].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Girilen denklem geçerli mi değil mi kontrol eder.
     */
    private boolean checkIfEquationValid() {
        // aktif olan satırdaki tüm hücrelerin değerleri stringe çevrilir
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnCount; i++) {
            sb.append(cells[activeRow][i].getText());
        }
        String equation = sb.toString(); // yazılan denklemin string hali
        return EquationUtils.validateEquation(equation);
    }

    /**
     * Kullanıcı oyunu kaybettiğinde çalıştırılır.
     */
    private void lose() {
        stopGame();
        listener.onLose(actualEquation);
    }

    /**
     * Varsa seçili olan hücrenin içindeki metni temizler.
     * Ve uygun durumda bir önceki hücreyi seçili hale getirir.
     */
    public void clearSelectedText() {
        // oyun bitmişse işlem yapma
        if (!isInGame) {
            return;
        }
        // seçili bir hücre olmadığında işlem yapma
        if (selectedColumn == -1) {
            return;
        }
        // seçili olan hücre ilk hücre değilse ve hücrenin içi boşsa
        // bir önceki hücreyi seç
        if (selectedColumn != 0 && cells[activeRow][selectedColumn].isEmpty()) {
            setSelectedColumn(selectedColumn - 1);
        }
        // hücrenin içinin boşaltıldığını bildir
        listener.onTextChange(activeRow, selectedColumn, ' ');
        cells[activeRow][selectedColumn].clear();
    }

    /**
     * Verilen metni seçilen hücreye yerleştirir.
     * Eğer halihazırda bir hücre seçilmemişse uygun bulunan ilk hücreyi seçer
     * ve o hücreye yerleştirir.
     */
    public void setSelectedText(char text) {
        // oyun bitmişse işlem yapma
        if (!isInGame) {
            return;
        }
        // seçili bir hücre olmadığında seçilebilecek ilk hücreyi seç
        if (selectedColumn == -1) {
            // ilk içi boş olan hücreyi bul
            int i = 0;
            while (i < columnCount - 1 && !cells[activeRow][i].isEmpty()) {
                i++;
            }
            // bulunan hücreyi seç
            setSelectedColumn(i);
        }
        // hücrenin değiştiğini bildir
        listener.onTextChange(activeRow, selectedColumn, text);
        cells[activeRow][selectedColumn].setText(text);
        // seçili olan hücre son hücre değilse bir sonraki hücreyi seç
        if (selectedColumn + 1 < columnCount) {
            setSelectedColumn(selectedColumn + 1);
        }
    }

    /**
     * Varsa seçili olan hücrenin seçimini kaldırır.
     */
    public void clearSelectedColumn() {
        // seçili bir hücre olmadığında işlem yapma
        if (selectedColumn == -1) {
            return;
        }
        // hücrenin içinin boşaltıldığını bildir
        listener.onCellSelectionChange(activeRow, selectedColumn, false);
        selectedColumn = -1;
    }

    /**
     * Tıklanan hücrenin seçimi yapılır.
     * Zaten seçili olan bir hücreye tıklandığında seçimi kaldırılır.
     * Öncesinde seçilmiş hücrenin seçimi kaldırılır.
     *
     * @param selectedColumn kaçıncı sütunun seçilmek istendiği
     *                       bu değer -1 ise hücrenin seçimi kaldırılır.
     */
    public void setSelectedColumn(int selectedColumn) {
        // seçili bir hücre varsa onun seçiminin kaldırıldığını bildir
        if (this.selectedColumn != -1) {
            listener.onCellSelectionChange(activeRow, this.selectedColumn, false);
        }
        // zaten seçili olan bir hücreye tıklandığında seçimi kaldır
        // aksi halde yeni seçilen hücreyi bildir
        if (this.selectedColumn == selectedColumn) {
            selectedColumn = -1;
        } else {
            listener.onCellSelectionChange(activeRow, selectedColumn, true);
        }
        this.selectedColumn = selectedColumn;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        listener.onTimeChange(timeInSeconds);
    }

    public boolean isInGame() {
        return isInGame;
    }

    /**
     * Belirtilen satır ve sütundaki hücrenin metnini döndürür.
     *
     * @param row    kaçıncı satır
     * @param column kaçıncı sütun
     */
    public char charAt(int row, int column) {
        return cells[row][column].getText();
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public int getActiveRow() {
        return activeRow;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    /**
     * Oyunun başlaması için gerekli olan metod.
     */
    public void startGame() {
        isInGame = true;
    }

    /**
     * Oyunun bitmesi için gerekli olan metod.
     */
    public void stopGame() {
        isInGame = false;
    }
}
