import Events.GameStateEvent;
import InterfaceLink.GameStateListner;
import Logic.BoardLogic;;
import Logic.GameState;
import Logic.PlayerInput;
import Visual.BoardVisual;
import javax.swing.*;

class Main extends JFrame {

    public static void main(String[] args) {;
        SwingUtilities.invokeLater(Main::new);
    }

    public static String playerName;

    public Main () {
        super("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setLocationRelativeTo(null);

        playerName = showPlayerNameDialog();
        if (playerName == null) {
            // Gracz wybrał opcję wyjścia
            System.exit(0);
        }
        BoardLogic boardLogic = new BoardLogic();
        BoardVisual boardVisual = new BoardVisual(boardLogic);
        PlayerInput playerInput = new PlayerInput(boardLogic);

        boardLogic.setPlayerName(playerName);

        boardLogic.addGameStateListner(new GameStateListner() {
            @Override
            public void changeGameState(GameStateEvent event) {
                if (event.getGameState() == GameState.UNPAUSED) {
                    Main.this.requestFocusInWindow();
                }
            }
        });

        addKeyListener(playerInput);
        boardVisual.addGameStateListner(boardLogic);
        boardLogic.addRefreshListner(boardVisual);
        boardLogic.addFoodEventListner(boardLogic);
        boardLogic.addGameStateListner(boardLogic);
        boardLogic.addGameStateListner(playerInput);
        playerInput.addChangeDirectionListner(boardLogic);
        getContentPane().add(boardVisual);
    }

    private String showPlayerNameDialog() {
        JTextField playerNameField = new JTextField();
        Object[] message = {"Podaj nazwę gracza:", playerNameField};
        int option = JOptionPane.showOptionDialog(this, message, "Wprowadź nazwę",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (option == JOptionPane.OK_OPTION) {
            return playerNameField.getText();
        } else {
            return null; // Gracz wybrał opcję wyjścia lub anulował
        }
    }
}
