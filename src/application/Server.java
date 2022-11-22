package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static java.lang.System.exit;

@SuppressWarnings("all")
public class Server {
  public static int ID = 0;
  public static int port = 20052;
  public static ServerSocket serverSocket;
  public static ArrayList<BufferedReader> bufferedReaders = new ArrayList<>();
  public static ArrayList<PrintWriter> printWriters = new ArrayList<>();
  public static ArrayList<String> message = new ArrayList<>();
  private static final int[][] chessBoard = new int[3][3];
  public static int draw = 0;

  public static int getID() {
    return ID;
  }

  public static void setID(int ID) {
    Server.ID = ID;
  }

  public static int getPort() {
    return port;
  }

  public static void setPort(int port) {
    Server.port = port;
  }

  public static ServerSocket getServerSocket() {
    return serverSocket;
  }

  public static void setServerSocket(ServerSocket serverSocket) {
    Server.serverSocket = serverSocket;
  }

  public static ArrayList<BufferedReader> getBufferedReaders() {
    return bufferedReaders;
  }

  public static void setBufferedReaders(ArrayList<BufferedReader> bufferedReaders) {
    Server.bufferedReaders = bufferedReaders;
  }

  public static ArrayList<PrintWriter> getPrintWriters() {
    return printWriters;
  }

  public static void setPrintWriters(ArrayList<PrintWriter> printWriters) {
    Server.printWriters = printWriters;
  }

  public static ArrayList<String> getMessage() {
    return message;
  }

  public static void setMessage(ArrayList<String> message) {
    Server.message = message;
  }

  public static int[][] getChessBoard() {
    return chessBoard;
  }

  public static int getDraw() {
    return draw;
  }

  public static void setDraw(int draw) {
    Server.draw = draw;
  }


  public static void main(String[] args) throws IOException {
    serverSocket = new ServerSocket(port);
    new ConnectSocket().start();
    new ServerMessage().start();
    System.out.println(new StringBuilder().append("Now the clients number is 0 ").append("").toString());

  }

  @SuppressWarnings("all")
  public static class ConnectSocket extends Thread {

    @Override
    public void run() {
      while (this.isAlive()) {
        try {
          if (ID == 2) continue;
          Socket socket = serverSocket.accept();
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          bufferedReaders.add(bufferedReader);
          new ClientMessage(socket, bufferedReader).start();
          printWriters.add(new PrintWriter(socket.getOutputStream()));
          printWriters.get(ID).println(ID);
          printWriters.get(ID).flush();
          ID++;
          System.out.println(MessageFormat.format("Now the clients number are {0}", ID));
          if (ID == 2) {
            for (PrintWriter printWriter : printWriters) {
              printWriter.println(0 + ",1");
              printWriter.flush();
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @SuppressWarnings("all")
  public static class ServerMessage extends Thread {
    @Override
    public void run() {
      while (this.isAlive()) {
        if (message.size() == 0) {
          continue;
        }
        String m = message.get(0);
        message.remove(0);
        for (PrintWriter printWriter : printWriters) {
          printWriter.println(m);
          printWriter.flush();
        }
      }
    }
  }

  @SuppressWarnings("all")
  public static class ClientMessage extends Thread {
    Socket socket;
    BufferedReader bufferedReader;

    public ClientMessage(Socket socket, BufferedReader bufferedReader) {
      this.socket = socket;
      this.bufferedReader = bufferedReader;
    }

    private boolean full() {
      int cnt = 0;
      cnt += (int) IntStream.range(0, 3).filter(j -> chessBoard[0][j] != 0).count();
      cnt += IntStream.range(0, 3).filter(j -> chessBoard[1][j] != 0).count();
      cnt += IntStream.range(0, 3).filter(j -> chessBoard[2][j] != 0).count();
      return cnt == 9;
    }

    private boolean win(int turn) {
      turn++;
      for (int i = 0; i < 3; i++) {
        int cnt = 0;
        for (int j = 0; j < 3; j++) {
          if (chessBoard[i][j] == turn) cnt++;
          else break;
        }
        if (cnt == 3) return true;
      }
      for (int i = 0; i < 3; i++) {
        int cnt = 0;
        for (int j = 0; j < 3; j++) {
          if (chessBoard[j][i] == turn) cnt++;
          else break;
        }
        if (cnt == 3) return true;
      }
      if (chessBoard[0][0] != turn || chessBoard[1][1] != turn || chessBoard[2][2] != turn) {
        if (chessBoard[0][2] != turn || chessBoard[1][1] != turn || chessBoard[2][0] != turn) {
          return false;
        }
        return true;
      } else {
        return true;
      }
    }

    @Override
    public void run() {
      while (true) {
        String m = null;
        if (bufferedReader != null) {
          try {
            m = bufferedReader.readLine();
          } catch (IOException e) {
            message.add(",,,,,1");
          }
        }
        if (m == null) {
          continue;
        }
        if (!m.equals("Disconnect")) {
        } else {
          ID--;
          System.out.println(new StringBuilder().append("Now the clients number are ").append(ID).toString());
          if (ID != 0) {
          } else {
            printWriters.clear();
            bufferedReaders.clear();
          }
          try {
            socket.close();
            socket = null;
            bufferedReader.close();
            bufferedReader = null;
            break;
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        String[] nums = m.split(",");
        int turn = Integer.parseInt(nums[0]);
        int x = Integer.parseInt(nums[1]);
        int y = Integer.parseInt(nums[2]);
        chessBoard[x][y] = turn + 1;
        message.add(m);
        if (!win(turn)) {
          if (win(turn) || win((turn + 1) % 2) || !full()) {
            continue;
          }
          message.add(turn + ",,2");
          try {
            Thread.sleep(11000);
          } catch (InterruptedException e) {
            int flag = Server.draw;
            if (flag == 0) {
              this.getName();
            }
          }
          exit(0);
          break;
        } else {
          message.add(turn + ",,1");
          try {
            Thread.sleep(11000);
          } catch (InterruptedException e) {
            int flag = Server.draw;
            if (flag == 0) {
              this.getName();
            }
          }
          exit(0);
          break;
        }
      }
    }
  }
}
