import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FifteenGame extends JPanel {

    private int size;
    private int dimension;
    private int blankTilePosition;
    //lista för tile numrena
    private int[] tiles;
    private final Random rand = new Random();
    private boolean gameOver;

    //lista med sjävlaste tile knapparna
    private java.util.List<JButton> tileButtons = new ArrayList<>();

    private JButton shuffleButton;
    private JButton cheatButton;

    public FifteenGame(int size, int dimension) {
        this.size = size;
        this.dimension = dimension;

        //panelens utseende
        setPreferredSize(new Dimension(dimension, dimension));
        setLayout(new BorderLayout());

        //skapar Två Jpanels, en för sjävlaste pusselbrädan och en för knapparna
        JPanel puzzleBoard = new JPanel(new GridLayout(size, size, 2, 2));
        JPanel buttonPanel = new JPanel();

        //för varje siffra i tiles så skapas en button som läggs till i puzzleBoard panelen
        tiles = new int[size * size];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
            JButton b = new JButton();
            b.setFont(new Font("Arial", Font.BOLD, 40));
            b.setFocusPainted(false);
            tileButtons.add(b);
            puzzleBoard.add(b);
        }

        //lägger till självaste puzzleBoard panelen
        add(puzzleBoard, BorderLayout.CENTER);

        //Skapar en shuffle knapp
        shuffleButton = new JButton("Shuffle");
        shuffleButton.setFocusPainted(false);
        shuffleButton.addActionListener(e -> {
            shuffle();
            updateBoard();
        });

        //skapar en cheat knapp
        cheatButton = new JButton("Cheat");
        cheatButton.setFocusPainted(false);
        cheatButton.addActionListener(e -> {
            // Sätter alla tiles till den lösta ordningen
            for (int i = 0; i < tiles.length; i++) {
                tiles[i] = (i + 1) % tiles.length;
            }
            blankTilePosition = tiles.length - 1;

            updateBoard();
        });

        //lägger till en panel för cheat och shuffle knapparna
        buttonPanel.add(shuffleButton);
        buttonPanel.add(cheatButton);
        add(buttonPanel, BorderLayout.SOUTH);

        //kopplar en listener till varje ruta
        for (int i = 0; i < tileButtons.size(); i++) {
            int index = i;
            tileButtons.get(i).addActionListener(e -> tileClicked(index));
        }
        shuffle();
    }

    private void tileClicked(int pos) {
        //metoden körs varenda gång en tile är klickad
        //gör inget om spelet är över
        if (gameOver){
            return;
        }

        // ger kolumnen av tilen som klickas
        int col = pos % size;
        // ger raden av tilen som klickas
        int row = pos / size;
        int blankCol = blankTilePosition % size;
        int blankRow = blankTilePosition / size;

        // kollar om tilen som klickades är bredvid den tomma rutan
        //horisontellt först och sen vertikalt
        if ((row == blankRow && Math.abs(col - blankCol) == 1) ||
                (col == blankCol && Math.abs(row - blankRow) == 1)) {
            //flyttar den tilen du klickade på till den tomma rutan
            tiles[blankTilePosition] = tiles[pos];
            //gör tilen som du klickade på tom
            tiles[pos] = 0;
            //updaterar den tomma rutans position i indexet
            blankTilePosition = pos;

            //går igenom alla knappar i buttons arrayen och sätter tilen som är 0 till den tomma
            updateBoard();

            //om isSolved = true så slutar spelet och visar ett popup att du vann!
            if (isSolved()) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "Congratulations, you won!");
            }
        }
    }

    private void shuffle() {
        gameOver = false;
        //skapar en ny temporär lista och addar alla element från den nuvarande tiles listan
        java.util.List<Integer> tempList = new ArrayList<>();
        for (int tile : tiles) {
            tempList.add(tile);
        }

        //använder collections för att shuffla elementen i den nya listan
        Collections.shuffle(tempList, rand);

        //kopierar tillbaks dom shufflade tilesen till den riktiga listan
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = tempList.get(i);
        }

        //går igenom tiles listan och hittar var den tomma rutans position är
        int zeroIndex = 0;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == 0) {
                zeroIndex = i;
                break;
            }
        }

        //om den tomma rutan inte är på den sista positionen så flyttar vi den dit
        if (zeroIndex != tiles.length - 1) {
            //tar värdet på den tilen som är på den sista platsen så vi kan flytta det
            int temp = tiles[tiles.length - 1];
            tiles[tiles.length - 1] = 0;
            tiles[zeroIndex] = temp;
        }
        blankTilePosition = tiles.length - 1;
        updateBoard();
    }

    private void updateBoard() {
        //loopar igenom alla knapp positioner och sätter så att visuellt blir tom eller ifylld.
        for (int i = 0; i < tiles.length; i++) {
            JButton b = tileButtons.get(i);
            if (tiles[i] == 0) {
                b.setText("");
                b.setBackground(Color.WHITE);
                //sätter så att man inte kan klicka på den tomma knappen
                b.setEnabled(false);
            } else {
                b.setText(String.valueOf(tiles[i]));
                b.setBackground(new Color(0, 255, 0));
                b.setEnabled(true);
            }
        }
    }

    private boolean isSolved() {
        //kollar först så att den sista rutan är tom, annars är spelet inte löst
        if (tiles[tiles.length - 1] != 0){
            return false;
        }
        //loopar igenom och kollar så att ordningen är 1,2,3,4... osv. Om någon ruta har fel värde så är spelet inte löst
        for (int i = 0; i < (tiles.length-1); i++) {
            if (tiles[i] != i + 1){
                return false;
            }
        }
        return true;
    }
}