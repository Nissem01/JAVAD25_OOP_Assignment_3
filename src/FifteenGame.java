import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FifteenGame extends JPanel {

    private int size;
    private int dimension;
    private int numberOfTiles;
    private int blankTilePosition;
    //lista för tile numrena
    private int[] tiles;
    private final Random rand = new Random();
    private JButton shuffleButton;
    //lista med sjävlaste tile knapparna
    private java.util.List<JButton> tileButtons = new ArrayList<>();
    private boolean gameOver;

    public FifteenGame(int size, int dimension) {
        this.size = size;
        this.dimension = dimension;
        this.numberOfTiles = size * size - 1;

        //panelens utseende
        setPreferredSize(new Dimension(dimension, dimension));
        setLayout(new BorderLayout());

        //skapandet av självaste pusselbrädan
        JPanel puzzleBoard = new JPanel(new GridLayout(size, size, 2, 2));
        puzzleBoard.setBackground(Color.WHITE);

        //för varje siffra i tiles så skapas en button och läggs till i pusslet.
        tiles = new int[size * size];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
            JButton b = new JButton();
            b.setFont(new Font("Arial", Font.BOLD, 60));
            b.setFocusPainted(false);
            tileButtons.add(b);
            puzzleBoard.add(b);
        }

        //lägger till självaste brädan PÅ panelen
        add(puzzleBoard, BorderLayout.CENTER);

        //lägger till en shuffle button längst ner
        shuffleButton = new JButton("Shuffle");
        shuffleButton.setFocusPainted(false);
        shuffleButton.addActionListener(e -> {
            shuffle();
            updateBoard();
        });

        add(shuffleButton, BorderLayout.SOUTH);

        //kopplar en listener till varje ruta
        for (int i = 0; i < tileButtons.size(); i++) {
            final int index = i;
            tileButtons.get(i).addActionListener(e -> tileClicked(index));
        }
        newGame();
    }

    private void tileClicked(int pos) {
        //metoden körs varenda gång en tile är klickad
        //gör inget om spelet är över
        if (gameOver) return;


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
            //updaterar den tomma rutans position
            blankTilePosition = pos;

            //går igenom alla knappar i buttons arrayen och sätter tilen som är 0 till den tomma
            updateBoard();

            //om isSolved = true så slutar spelet och visar ett popup att du vann!
            if (isSolved()) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "Grattis, du vann!");
            }
        }
    }

    private void newGame() {
            // Återställer tilesen så att dom hamnar i normal ordning
            reset();
            // Blandar tilesen
            shuffle();
            //updaterar så att användaren ser dom nya tilesen
            updateBoard();
    }

    private void reset() {
        //loopar igenom all tilesen och lägger dom i normal ordning
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }
        //sätter den tommas position till den sista
        blankTilePosition = tiles.length - 1;
    }

    private void shuffle() {
        //skapar en ny temporär lista och addar alla element från den nuvarande tiles listan
        java.util.List<Integer> list = new ArrayList<>();
        for (int tile : tiles) {
            list.add(tile);
        }

        //använder collections för att shuffla elementen i den nya listan
        Collections.shuffle(list, rand);

        //kopierar tillbaks dom shufflade tilesen till den riktiga listan
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = list.get(i);
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
            tiles[tiles.length - 1] = 0;
            tiles[zeroIndex] = tiles[tiles.length - 1];
        }

        blankTilePosition = tiles.length - 1;
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
        for (int i = 0; i < numberOfTiles; i++) {
            if (tiles[i] != i + 1){
                return false;
            }
        }
        return true;
    }
}