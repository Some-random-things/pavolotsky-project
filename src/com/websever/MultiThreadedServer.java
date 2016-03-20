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
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      // read request
      String line;
      line = in.readLine();
      StringBuilder raw = new StringBuilder();
      raw.append("" + line);
      boolean isPost = line.startsWith("POST");
      int contentLength = 0;
      while (!(line = in.readLine()).equals("")) {
        raw.append('\n' + line);
        if (isPost) {
          final String contentHeader = "Content-Length: ";
          if (line.startsWith(contentHeader)) {
            contentLength = Integer.parseInt(line.substring(contentHeader.length()));
          }
        }
      }
      StringBuilder body = new StringBuilder();
      if (isPost) {
        for (int i = 0; i < contentLength; i++) {
          int c = in.read();
          body.append((char) c);
        }
      }

      String response = processData(body.toString());
      Thread.sleep(30000);

      // response
      out.write("HTTP/1.1 200 OK\r\n");
      out.write("Content-Type: text/html\r\n");
      out.write("\r\n");
      out.write(response);
      out.flush();
      out.close();
      socket.close();
      //
    } catch (Exception e) {
      e.printStackTrace();
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
    }
  }

  private String processData(String request) {
    return "RESULT";
  }
}

