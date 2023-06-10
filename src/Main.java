
import Logic.BoardLogic;
import Logic.GameState;
import Logic.PlayerInput;
import Visual.BoardVisual;
import Visual.ScoreboardPanel;

import javax.swing.*;
import java.awt.*;

class Main extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private final BoardLogic boardLogic;
    private String playerName;

    public Main () {
        super("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        boardLogic = new BoardLogic();
        BoardVisual boardVisual = new BoardVisual(boardLogic);
        PlayerInput playerInput = new PlayerInput(boardLogic);
        ScoreboardPanel scoreboardPanel = new ScoreboardPanel(boardLogic);

        playerName = showPlayerNameDialog();
        if (playerName == null) {
            // Gracz wybrał opcję wyjścia
            System.exit(0);
        }
        boardLogic.setPlayerName(playerName);


        boardLogic.addGameStateListner(event -> {
            if (
                    event.getGameState() == GameState.UNPAUSED
                    || event.getGameState() == GameState.NEWGAME
            ) {
                Main.this.requestFocusInWindow();
            }
        });
        addKeyListener(playerInput);
        boardLogic.addRefreshListner(boardVisual);
        boardLogic.addRefreshListner(scoreboardPanel);
        boardLogic.addGameStateListner(playerInput);
        boardLogic.addGameStateListner(boardVisual);
        playerInput.addChangeDirectionListner(boardLogic);
        getContentPane().add(boardVisual,BorderLayout.CENTER);
        getContentPane().add(scoreboardPanel,BorderLayout.EAST);
    }

    private String showPlayerNameDialog() {
        JTextField playerNameField = new JTextField();
        Object[] message = {"Podaj nazwę gracza:", playerNameField};
        int option = JOptionPane.showOptionDialog(this, message, "Wprowadź nazwę",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            playerName = playerNameField.getText().trim();

            if (!playerName.isEmpty()) {
                boardLogic.setPlayerName(playerName);
                return playerName;
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Nazwa gracza nie może być pusta.",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
                return showPlayerNameDialog();
            }
        } else {
            return null;
        }
    }
}
