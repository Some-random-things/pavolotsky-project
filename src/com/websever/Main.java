package com.websever;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocket ssock = new ServerSocket(10000);
        System.out.println("Liste");
        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
            new Thread(new MultiThreadedServer(sock)).start();
        }
    }
}
