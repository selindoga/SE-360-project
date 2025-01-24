package main;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class Controller {
    private Connection conn;
    private int sessionId;
    int edgeAmount;
    int edge_square_amount;
    

    ArrayList<Line> horizontalLines;
    ArrayList<Line> verticalLines;
    ArrayList<Square> squares;
    ArrayList<JPanel> panels;
    GameplayGui gui;
    Player[] players;
    Player currentPlayer;
    int activatedSquares;

    public Controller(Player player1, Player player2) {
        initializeDatabase();
        sessionId = getNextSessionId();
        this.edgeAmount = 5;
        this.horizontalLines = new ArrayList<>();
        this.verticalLines = new ArrayList<>();
        this.squares = new ArrayList<>();
        this.panels = new ArrayList<>();
        gui = new GameplayGui(this);
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        currentPlayer = players[0];
        activatedSquares = 0;
        edge_square_amount = ((int) Math.pow(edgeAmount, 2));


    }

    public Controller(Player player1, Player player2, int edgeAmount) {
        initializeDatabase();
        sessionId = getNextSessionId();

        this.edgeAmount = edgeAmount;
        this.horizontalLines = new ArrayList<>();
        this.verticalLines = new ArrayList<>();
        this.squares = new ArrayList<>();
        this.panels = new ArrayList<>();
        gui = new GameplayGui(this);
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        currentPlayer = players[0];
        activatedSquares = 0;
        edge_square_amount = ((int) Math.pow(edgeAmount, 2));



    }

    private void initializeDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:gameplay.db");
            Statement con_stm = conn.createStatement();

            con_stm.execute("CREATE TABLE IF NOT EXISTS TableGameplay (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "player_name TEXT, line_number INTEGER, square_activated BOOLEAN, square_index INTEGER, "
                    + "score1 INTEGER, score2 INTEGER, current_turn TEXT, sessionId INTEGER);");

            con_stm.execute("CREATE TABLE IF NOT EXISTS TableWinner (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "winner TEXT, score1 INTEGER, score2 INTEGER, sessionId INTEGER);");
            con_stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNextSessionId() {
        int nextSessionId = 1;
        try {
            String sql = "SELECT MAX(sessionId) FROM TableGameplay;";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                nextSessionId = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextSessionId;
    }

    private void logMove(int lineNumber, boolean squareActivated, int squareIndex) {
        try {
            String sql = "INSERT INTO TableGameplay (player_name, line_number, square_activated, square_index, "
                    + "score1, score2, current_turn, sessionId) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, currentPlayer.getName());
            stm.setInt(2, lineNumber);
            stm.setBoolean(3, squareActivated);
            stm.setInt(4, squareIndex);
            stm.setInt(5, players[0].getScore());
            stm.setInt(6, players[1].getScore());
            stm.setString(7, currentPlayer.getName());
            stm.setInt(8, sessionId);
            stm.executeUpdate();

            // hareketleri panelde göstermek istedim
            String moveLog = "Player: " + currentPlayer.getName() +
                             ", Line: " + lineNumber +
                             ", Activated Square: " + (squareActivated ? "Yes" : "No") +
                             (squareActivated ? ", Square Index: " + squareIndex : "");
            gui.addMoveToHistory(moveLog);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == players[0]) ? players[1] : players[0];
        gui.setLabel(currentPlayer.getName() + "'s Turn, Your Score: " + currentPlayer.getScore());
    }

    public void addLine(Line param_line, String orientation) {
        if (orientation.equals("horizontal")) {
            this.horizontalLines.add(param_line);
        } else if (orientation.equals("vertical")) {
            this.verticalLines.add(param_line);
        }
    }

    public void activateLine(Line line) {
        line.activate(currentPlayer.getColor());
        gui.setButton(line);
    }

    public void addPanel(JPanel param_panel) {
        this.panels.add(param_panel);
    }

    public void createSquare(int number, JPanel param_panel) {
        int vert_index = number + (number / edgeAmount);
        Square newSquare = new Square(horizontalLines.get(number),
                horizontalLines.get(number + edgeAmount),
                verticalLines.get(vert_index),
                verticalLines.get(vert_index + 1),
                param_panel);
        this.squares.add(newSquare);
    }

    public void createSquares() {
        for (int i = 0; i < edge_square_amount; i++) {
            createSquare(i, panels.get(i));
        }
    }

    public void activateSquare(Square square) {
        square.activate(currentPlayer.getColor());
        if (square.panel != null) {
            square.panel.repaint();
        }
    }

    public void checkSquares() {
        boolean anySquareActivated = false;
        for (Square square : squares) {
            if (square.isAllLinesAreActive() && !square.is_active) {
                anySquareActivated = true;
                activateSquare(square);
                gui.setPanel(square);
                currentPlayer.increaseScore();
                activatedSquares++;
                gui.setLabel(currentPlayer.getName() + "'s Turn, Your Score: " + currentPlayer.getScore());
            }
        }
        if (!anySquareActivated) {
            switchPlayer();
        }
        if (activatedSquares == edge_square_amount) {
            gameEnd();
        }
    }

    public void afterClick(Line line) {
        activateLine(line);

        boolean squareActivated = false;
        int activatedSquareIndex = -1;

        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            if (square.isAllLinesAreActive() && !square.is_active) {
                activateSquare(square);
                gui.setPanel(square);
                currentPlayer.increaseScore();
                activatedSquares++;
                squareActivated = true;
                activatedSquareIndex = i;
                gui.setLabel(currentPlayer.getName() + "'s Turn, Your Score: " + currentPlayer.getScore());
            }
        }

        logMove(line.lineNumber, squareActivated, activatedSquareIndex);

        if (!squareActivated) {
            switchPlayer();
        }
        if (activatedSquares == edge_square_amount) {
            gameEnd();
        }
    }

    public void run() {
        gui.run_gui();
    }

    public void gameEnd() {
        try {
            String winner = players[0].getScore() > players[1].getScore() ? players[0].getName() : players[1].getName();
            String sql = "INSERT INTO TableWinner (winner, score1, score2, sessionId) VALUES (?, ?, ?, ?);";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, winner);
            stm.setInt(2, players[0].getScore());
            stm.setInt(3, players[1].getScore());
            stm.setInt(4, sessionId);
            stm.executeUpdate();

            gui.setLabel(winner + " Wins!");
            gui.gameEndpopup(winner + " wins", winner + " wins");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
