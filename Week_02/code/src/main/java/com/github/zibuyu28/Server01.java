package com.github.zibuyu28;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server01 {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8899);

        while(true) {
            Socket accept = serverSocket.accept();
            Server01.service(accept);
            accept.close();
        }
    }

    private static void service(Socket socket) {
        try (OutputStream outputStream = socket.getOutputStream()) {
            Thread.sleep(20);
            String words = "hello\n";
            outputStream.write("HTTP/1.1 200 OK\n".getBytes());
            outputStream.write("Content-Type:text/html;charset=utf-8\n".getBytes());
            outputStream.write(String.format("Content-Length:%d\n",words.length()).getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(words.getBytes());
            outputStream.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
