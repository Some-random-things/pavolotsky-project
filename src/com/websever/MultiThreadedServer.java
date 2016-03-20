package com.websever;

import java.io.*;
import java.net.Socket;

public class MultiThreadedServer implements Runnable {
  Socket socket;
  MultiThreadedServer(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      Thread.sleep(30000);
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

