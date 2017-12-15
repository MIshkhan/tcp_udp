/**
 *  Datagram
 *  | Msg | length | Host | serverPort |
 */

import java.net.*;
import java.io.*;

public class UDPClient {

  public static void main(String args[]) {    
    try (DatagramSocket aSocket = new DatagramSocket()) {
      // important
      aSocket.setSoTimeout(10000);   
      
      byte [] m = new String("Give me date").getBytes();
      int serverPort = 8000;
      InetAddress aHost = InetAddress.getByName("localhost");

      DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

      // If it’s too small, it will be silently truncated
      byte[] buffer = new byte[1000]; 
      DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

      aSocket.send(request);
      aSocket.receive(reply);
      
      System.out.println("Reply: " + new String(reply.getData()));
    }
    catch (SocketException e) {
      System.out.println("Socket: " + e);
    }
    catch (IOException e) {
      System.out.println("IO: " + e);
    }    
  }

}
