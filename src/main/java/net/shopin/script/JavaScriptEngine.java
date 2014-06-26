package net.shopin.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-26
 * Time: 下午6:20
 */
public class JavaScriptEngine {


    public static void main(String[] args) throws IOException {

        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
// create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        while (true) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(4444, 50, InetAddress.getByName("localhost"));
            } catch (IOException e) {
                System.err.println("Could not listen on port: 4444.");
                System.exit(1);
            }

            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                if ("quit".equals(inputLine))
                    break;
                try {
                    // evaluate JavaScript code from String
                    out.println(engine.eval(inputLine));
                } catch (Exception e) {
                    out.println("error:" + e.getMessage());
                }
            }
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        }

    }
}
