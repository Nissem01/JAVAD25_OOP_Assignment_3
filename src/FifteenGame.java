import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FifteenGame extends JPanel {

    private int size;
    private int dimension;
    private int numberOfTiles;
    private int blankTilePosition;
    private int[] tiles;
    private final Random rand = new Random();

    private JButton shuffleButton;
    private java.util.List<JButton> buttons = new ArrayList<>();
    private boolean gameOver;

    public FifteenGame(int size, int dimension) {
        this.size = size;
        this.dimension = dimension;
        this.numberOfTiles = size * size - 1;

        setPreferredSize(new Dimension(dimension, dimension));
        setLayout(new BorderLayout());

        JPanel puzzleBoard = new JPanel(new GridLayout(size, size, 2, 2));
        puzzleBoard.setBackground(Color.WHITE);

        tiles = new int[size * size];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
            JButton b = new JButton();
            b.setFont(new Font("Arial", Font.BOLD, 20));
            b.setFocusPainted(false);
            buttons.add(b);
            puzzleBoard.add(b);
        }

        shuffleButton = new JButton("Shuffle");
        shuffleButton.setFocusPainted(false);
        shuffleButton.addActionListener(e -> {
            shuffle();
            updateBoard();
        });

        add(puzzleBoard, BorderLayout.CENTER);
        add(shuffleButton, BorderLayout.SOUTH);

        for (int i = 0; i < buttons.size(); i++) {
            final int index = i;
            buttons.get(i).addActionListener(e -> tileClicked(index));
        }
        newGame();
    }

    private void tileClicked(int pos) {
        if (gameOver) return;

        int col = pos % size;
        int row = pos / size;
        int blankCol = blankTilePosition % size;
        int blankRow = blankTilePosition / size;

        if ((row == blankRow && Math.abs(col - blankCol) == 1) ||
                (col == blankCol && Math.abs(row - blankRow) == 1)) {
            tiles[blankTilePosition] = tiles[pos];
            tiles[pos] = 0;
            blankTilePosition = pos;
            updateBoard();

            if (isSolved()) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "Grattis, du vann!");
            }
        }
    }

    private void newGame() {
            reset();
            shuffle();
        updateBoard();
    }

    private void reset() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }
        blankTilePosition = tiles.length - 1;
    }

    private void shuffle() {
        int n = numberOfTiles;
        while (n > 1) {
            int random = rand.nextInt(n--);
            int tmp = tiles[random];
            tiles[random] = tiles[n];
            tiles[n] = tmp;
        }
        if (tiles[tiles.length - 1] != 0) {
            int indexOfZero = -1;
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i] == 0) {
                    indexOfZero = i;
                    break;
                }
            }
            int tmp = tiles[tiles.length - 1];
            tiles[tiles.length - 1] = 0;
            tiles[indexOfZero] = tmp;
        }

        blankTilePosition = tiles.length - 1;
    }

    private int findBlank() {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == 0) return i;
        }
        return tiles.length - 1;
    }

    private void updateBoard() {
        for (int i = 0; i < tiles.length; i++) {
            JButton b = buttons.get(i);
            if (tiles[i] == 0) {
                b.setText("");
                b.setBackground(Color.WHITE);
                b.setEnabled(false);
            } else {
                b.setText(String.valueOf(tiles[i]));
                b.setBackground(new Color(0, 255, 0));
                b.setEnabled(true);
            }
        }
    }

    private boolean isSolved() {
        if (tiles[tiles.length - 1] != 0) return false;
        for (int i = 0; i < numberOfTiles; i++) {
            if (tiles[i] != i + 1) return false;
        }
        return true;
    }
}