package br.edu.ifsuldeminas.sd.sockets.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class UDPClient {

    private static final int TIME_OUT = 5000; 
    private static int SERVER_PORT = 3000;
    private static int BUFFER_SIZE = 200;

    private static String KEY_TO_EXIT = "q";

    public static void main(String[] args) {
        DatagramSocket datagramSocket = null;
        InetAddress serverAddress = null;
        Scanner reader = new Scanner(System.in);
        String stringMessage = "";

        try {
            datagramSocket = new DatagramSocket();
            serverAddress = InetAddress.getLocalHost(); // Altere se o servidor não estiver na mesma máquina

            while (!stringMessage.equals(KEY_TO_EXIT)) {
                System.out.printf("Escreva uma mensagem (%s para sair): ", KEY_TO_EXIT);
                stringMessage = reader.nextLine();

                if (!stringMessage.equals(KEY_TO_EXIT)) {
                    // Envia mensagem ao servidor
                    byte[] message = stringMessage.getBytes();
                    DatagramPacket datagramPacketToSend = new DatagramPacket(message, message.length, serverAddress, SERVER_PORT);
                    datagramSocket.setSoTimeout(TIME_OUT);
                    datagramSocket.send(datagramPacketToSend);

                    // Recebe resposta do servidor
                    try {
                        byte[] responseBuffer = new byte[BUFFER_SIZE];
                        DatagramPacket datagramPacketForResponse = new DatagramPacket(responseBuffer, responseBuffer.length);
                        datagramSocket.receive(datagramPacketForResponse);
                        String receivedMessage = new String(datagramPacketForResponse.getData(), 0, datagramPacketForResponse.getLength());
                        System.out.printf("Resposta do servidor: %s\n", receivedMessage);
                    } catch (SocketTimeoutException e) {
                        System.out.printf("Sem resposta do servidor de eco UDP.\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOpenedResources(datagramSocket, reader);
            System.out.printf("Cliente saindo com %s ...\n", KEY_TO_EXIT);
        }
    }

    private static void closeOpenedResources(DatagramSocket datagramSocket, Scanner reader) {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
        if (reader != null) {
            reader.close();
        }
    }
}
