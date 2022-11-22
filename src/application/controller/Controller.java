package application.controller;

import application.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.sql.Connection;
import java.util.ResourceBundle;

import static java.lang.Integer.*;
import static java.lang.System.*;
import static javafx.application.Platform.*;

@SuppressWarnings("all")
public class Controller implements Initializable {
  private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
  private static final int EMPTY = 0;
  private static final int BOUND = 90;
    private static final int OFFSET = 15;
    public static int ID = 0;

  public static int getPlay1() {
    return PLAY_1;
  }

  public static int getPlay2() {
    return PLAY_2;
  }

  public static int getEMPTY() {
    return EMPTY;
  }

  public static int getBOUND() {
    return BOUND;
  }

  public static int getOFFSET() {
    return OFFSET;
  }

  public static int getID() {
    return ID;
  }

  public static void setID(int ID) {
    Controller.ID = ID;
  }

  public static int getCurrentTurn() {
    return CurrentTurn;
  }

  public static void setCurrentTurn(int currentTurn) {
    CurrentTurn = currentTurn;
  }

  public static int getDOWin() {
    return DOWin;
  }

  public static void setDOWin(int DOWin) {
    Controller.DOWin = DOWin;
  }

  public static Socket getSocket() {
    return socket;
  }

  public static void setSocket(Socket socket) {
    Controller.socket = socket;
  }

  public static PrintWriter getPrintWriter() {
    return printWriter;
  }

  public static void setPrintWriter(PrintWriter printWriter) {
    Controller.printWriter = printWriter;
  }

  public static BufferedReader getBufferedReader() {
    return bufferedReader;
  }

  public static void setBufferedReader(BufferedReader bufferedReader) {
    Controller.bufferedReader = bufferedReader;
  }

  public Connection getCon() {
    return con;
  }

  public String getHost() {
    return host;
  }

  public String getDbname() {
    return dbname;
  }

  public String getUser() {
    return user;
  }

  public String getPwd() {
    return pwd;
  }

  public String getPort() {
    return port;
  }

  public Pane getBase_square() {
    return base_square;
  }

  public void setBase_square(Pane base_square) {
    this.base_square = base_square;
  }

  public Button getButton() {
    return button;
  }

  public void setButton(Button button) {
    this.button = button;
  }

  public TextArea getTextArea() {
    return textArea;
  }

  public void setTextArea(TextArea textArea) {
    this.textArea = textArea;
  }

  public Rectangle getGame_panel() {
    return game_panel;
  }

  public void setGame_panel(Rectangle game_panel) {
    this.game_panel = game_panel;
  }

  public static boolean isTURN() {
    return TURN;
  }

  public static int[][] getChessBoard() {
    return chessBoard;
  }

  public static boolean[][] getFlag() {
    return flag;
  }

  public static int CurrentTurn = -1;
  public static int DOWin = -1;
  public static Socket socket;
  public static PrintWriter printWriter;
  public static BufferedReader bufferedReader;
  private final Connection con = null;
  private final String host = "localhost";
  private final String dbname = "postgres";
  private final String user = "checker";
  private final String pwd = "123456";
  private final String port = "5432";
  @FXML
  private Pane base_square;
  @FXML
  private Button button;
  @FXML
  private TextArea textArea;
  @FXML
  private Rectangle game_panel;

  private static final boolean TURN = false;

  private static final int[][] chessBoard = new int[3][3];
  private static final boolean[][] flag = new boolean[3][3];

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    game_panel.setOnMouseClicked(event -> {
      if (ID != CurrentTurn) return;
      int x = (int) (event.getX() / BOUND);
      int y = (int) (event.getY() / BOUND);
      if (chessBoard[x][y] != 0) return;
      printWriter.println(String.format("%d,%d,%d,1", ID, x, y));
      printWriter.flush();
    });
  }

  private void refreshBoard(int turn, int x, int y) {
    chessBoard[x][y] = turn + 1;
    if (turn == 0) {
      drawCircle(x, y);
    } else {
      drawLine(x, y);
    }
  }

  void drawChess() {
    for (int i = 0; i < chessBoard.length; i++) {
      for (int j = 0; j < chessBoard[0].length; j++) {
        if (flag[i][j]) {
          // This square has been drawing, ignore.
          continue;
        }
        switch (chessBoard[i][j]) {
          case PLAY_1:
            drawCircle(i, j);
            break;
          case PLAY_2:
            drawLine(i, j);
            break;
          case EMPTY:
            // do nothing
            break;
          default:
            err.println("Invalid value!");
        }
      }
    }
  }

  private void drawCircle(int i, int j) {
    var circle = new Circle();
    runLater(() -> base_square.getChildren().add(circle));
    circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
    circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
    circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
    circle.setStroke(Color.RED);
    circle.setFill(Color.TRANSPARENT);
    flag[i][j] = true;
  }

  private void drawLine(int i, int j) {
    var line_a = new Line();
    var line_b = new Line();
    runLater(() -> {
      base_square.getChildren().add(line_a);
      base_square.getChildren().add(line_b);
    });
    line_a.setStartX(i * BOUND + OFFSET * 1.5);
    line_a.setStartY(j * BOUND + OFFSET * 1.5);
    line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
    line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
    line_a.setStroke(Color.BLUE);

    line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
    line_b.setStartY(j * BOUND + OFFSET * 1.5);
    line_b.setEndX(i * BOUND + OFFSET * 1.5);
    line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
    line_b.setStroke(Color.BLUE);
    flag[i][j] = true;
  }

  public void ConnectServer(ActionEvent actionEvent) throws IOException {
    socket = new Socket(InetAddress.getByName(null), Server.port);
    printWriter = new PrintWriter(socket.getOutputStream());
    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out.println("##########################################################################");
    out.println(new StringBuilder().append("Successfully connected to the Server ").append(InetAddress.getLocalHost()).toString());
    textArea.clear();
    textArea.appendText("Successfully connected to the Server " + InetAddress.getLocalHost());
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      out.println("No time");
    }
    button.setDisable(true);
    new GetServerMessage().start();
  }

  @SuppressWarnings("all")
  public class GetServerMessage extends Thread {

    public GetServerMessage() {
    }

    public GetServerMessage(Runnable target) {
      super(target);
    }

    public GetServerMessage(ThreadGroup group, Runnable target) {
      super(group, target);
    }

    public GetServerMessage(String name) {
      super(name);
    }

    public GetServerMessage(ThreadGroup group, String name) {
      super(group, name);
    }

    public GetServerMessage(Runnable target, String name) {
      super(target, name);
    }

    public GetServerMessage(ThreadGroup group, Runnable target, String name) {
      super(group, target, name);
    }

    public GetServerMessage(ThreadGroup group, Runnable target, String name, long stackSize) {
      super(group, target, name, stackSize);
    }

    public GetServerMessage(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
      super(group, target, name, stackSize, inheritThreadLocals);
    }

    @Override
    public void run() {
      while (this.isAlive()) {
        try {
          String s = bufferedReader.readLine();
          String[] nums = s.split(",");
          if (nums.length == 1) {
            ID = parseInt(nums[0]);
            out.println(new StringBuilder().append("You has ID: ").append(ID).toString());
          } else if (nums.length == 2) {
            CurrentTurn = parseInt(nums[0]);
            out.println("##########################################################################");
            out.println("Please start the game");

          } else if (nums.length == 3) {
            DOWin = parseInt(nums[0]);
            if (parseInt(nums[2]) != 2) {
              if (DOWin != ID) {
                if ((DOWin + 1) % 2 != ID) {
                } else {
                  out.println("##########################################################################");
                  out.println("You lose");
                  textArea.clear();
                  textArea.appendText("You lose");
                  CurrentTurn = -1;
                  try {
                    Thread.sleep(5000);
                  } catch (InterruptedException e) {
                    int flag = Server.draw;
                    if (flag == 0) {
                      this.getName();
                    }
                  }
                  exit(0);
                  break;
                }
              } else {
                out.println("##########################################################################");
                out.println("You win");
                textArea.clear();
                textArea.appendText("You win");
                CurrentTurn = -1;
                try {
                  Thread.sleep(5000);
                } catch (InterruptedException e) {
                  int flag = Server.draw;
                  if (flag == 0) {
                    this.getName();
                  }
                }
                exit(0);
                break;
              }
            } else {
              out.println("Game draw");
              textArea.clear();
              textArea.appendText("Game draw");
              try {
                Thread.sleep(5000);
              } catch (InterruptedException e) {
                int flag = Server.draw;
                if (flag == 0) {
                  this.getName();
                }
              }
              exit(0);
            }

          } else if (nums.length != 6) {
            int last = parseInt(nums[0]);
            refreshBoard(last, parseInt(nums[1]), parseInt(nums[2]));
            CurrentTurn = (last + 1) % 2;
          } else {
            out.println("##########################################################################");
            out.println("Opponent escapes.");
            textArea.clear();
            textArea.appendText("Opponent escapes.");
            break;
          }
          if (CurrentTurn != ID) {
            out.println("##########################################################################");
            out.println("Please Wait");
            textArea.clear();
            textArea.appendText("Please Wait");
          } else {
            out.println("##########################################################################");
            out.println("Your turn");
            textArea.clear();
            textArea.appendText("Your turn");
          }
        } catch (IOException e) {
          out.println("##########################################################################");
          out.println("Server disconnect");
          break;
        }

      }
    }
  }

}
