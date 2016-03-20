package com.websever;

import org.json.JSONArray;
import org.json.JSONObject;

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

  private String processData(String requestBody) {
    JSONObject obj = new JSONObject(requestBody);
    JSONArray arr = obj.getJSONArray("expressions");

    JSONObject response = new JSONObject();
    JSONArray responseArray = new JSONArray();

    for (int i = 0; i < arr.length(); i++) {
      String expression = arr.getJSONObject(i).getString("expression");

      float result = new Expression(expression).eval().floatValue();
      JSONObject exprResponse =  new JSONObject();
      exprResponse.put("expression", expression);
      exprResponse.put("result", result);
      responseArray.put(exprResponse);
    }

    response.put("expressions", responseArray);
    return response.toString();
  }
}

