/**
 *  DatagramPacket
 *  DatagramSocket
 */

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.logging.*;

public class DaytimeUDPServer {
  
  private final static int PORT = 8000;
  private final static Logger audit = Logger.getLogger("requests");
  private final static Logger errors = Logger.getLogger("errors");
  
  public static void main(String[] args) {
    try (DatagramSocket socket = new DatagramSocket(PORT)) { // 0 -> any free port
      while (true) {
        try {
          DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
          socket.receive(request);
          
          String daytime = new Date().toString();
          byte[] data = daytime.getBytes("US-ASCII");
          
          DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
          socket.send(response);
          
          audit.info(daytime + " " + request.getAddress());
        } catch (IOException | RuntimeException ex) {
          errors.log(Level.SEVERE, ex.getMessage(), ex);
        }
      }
    } catch (IOException ex) {
      errors.log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

}

/*
  As you can see in this example, UDP servers tend not to be as multithreaded as TCP
  servers. They usually don’t do a lot of work for any one client, and they can’t get blocked
  waiting for the other end to respond because UDP never reports errors. Unless a lot of
  time-consuming work is required to prepare the response, an iterative approach works
  just fine for UDP servers.
*/

/*
  Because port numbers are given as two-byte unsigned integers(2^16), 65,536 different possible
  UDP ports are available per host. These are distinct from the 65,536 different TCP ports
  per host. Because the length is also a two-byte unsigned integer, the number of bytes in
  a datagram is limited to 65,536 minus the eight bytes for the header.
  However, this is redundant with the datagram length field of the IP header, which limits 
  datagrams to between 65,467 and 65,507 bytes. (The exact number depends on the size of the IP header.)
  If the checksum for the data fails, the native network software silently discards the datagram; 
  neither the sender nor the receiver is notified. UDP is an unreliable protocol, after all.
*/

/*
  Although the theoretical maximum amount of data in a UDP datagram is 65,507 bytes,
  in practice there is almost always much less. On many platforms, the actual limit is more
  likely to be 8,192 bytes (8K). And implementations are not required to accept datagrams
  with more than 576 total bytes, including data and headers. Consequently, you should
  be extremely wary of any program that depends on sending or receiving UDP packets
  with more than 8K of data. Most of the time, larger packets are simply truncated to 8K
  of data. For maximum safety, the data portion of a UDP packet should be kept to 512
  bytes or less, although this limit can negatively affect performance compared to larger
  packet sizes
*/






/*
static String d = "As you can see in this example, UDP servers tend not to be as multithreaded as TCP  servers. They usually don’t do a lot of work for any one client, and they can’t get blocked  waiting for the other end to respond because UDP never reports errors. Unless a lot of  time-consuming work is required to prepare the response, an iterative approach works  just fine for UDP servers.  Because port numbers are given as two-byte unsigned integers(2^16), 65,536 different possible  UDP ports are available per host. These are distinct from the 65,536 different TCP ports  per host. Because the length is also a two-byte unsigned integer, the number of bytes in  a datagram is limited to 65,536 minus the eight bytes for the header.  However, this is redundant with the datagram length field of the IP header, which limits   datagrams to between 65,467 and 65,507 bytes. (The exact number depends on the size of the IP header.)  If the checksum for the data fails, the native network software silently discards the datagram;   neither the sender nor the receiver is notified. UDP is an unreliable protocol, after all.  Although the theoretical maximum amount of data in a UDP datagram is 65,507 bytes,  in practice there is almost always much less. On many platforms, the actual limit is more  likely to be 8,192 bytes (8K). And implementations are not required to accept datagrams  with more than 576 total bytes, including data and headers. Consequently, you should  be extremely wary of any program that depends on sending or receiving UDP packets  with more than 8K of data. Most of the time, larger packets are simply truncated to 8K  of data. For maximum safety, the data portion of a UDP packet should be kept to 512  bytes or less, although this limit can negatively affect performance compared to larger  packet sizes";
*/



Managing Connections
  
  Unlike TCP sockets, datagram sockets aren’t very picky about whom they’ll talk to. In
  fact, by default they’ll talk to anyone; but this is often not what you want.

    public void connect(InetAddress host, int port)

    Specifies that the DatagramSocket will only send packets to and receive packets
    from the specified remote host on the specified remote port. Attempts to send packets
    to a different host or port will throw an IllegalArgumentException . Packets received
    from a different host or a different port will be discarded without an exception or other
    notification

    public void disconnect()
    The disconnect() method breaks the “connection” of a connected DatagramSocket so
    that it can once again send packets to and receive packets from any host and port.

    
    public int getPort()                 returns port of host whom socket is connected -1 otherwise 
    public InetAddress getInetAddress()  returns port of InetAddress whom socket is connected null otherwise

    // SO_TIMEOUT is the amount of time, in milliseconds, that receive() waits for
    // an incoming datagram before throwing an InterruptedIOException , which is a subclass of IOException . 
    • SO_TIMEOUT  

    // Max buffer size of datagram packets that can be received by the application. Packets that won’t fit in the receive buffer are silently discarded.
    // operating system is free to ignore this suggestion
    • SO_RCVBUF

    // suggests a number of bytes to use for buffering output on this socket
    // when data is larger than the setting of SO_SNDBUF then it is implementation specific if the packet is sent or discarded.
    // operating system is free to ignore this suggestion
    • SO_SNDBUF

    // controls whether multiple datagram sockets can bind to the same port and address at the same time. true/false
    // If multiple sockets are bound to the same port, received packets will be copied to all bound sockets.
    • SO_REUSEADDR*

    // controls whether a socket is allowed to send packets to and receive packets from broadcast addresses such as 192.168.254.255, the local network
    // broadcast address for the network with the local address 192.168.254.*    true/false  default=true
    // On some implementations, sockets bound to a specific address do not receive broadcast packets. In other words, you should use the Data
    // gramPacket(int port) constructor, not the DatagramPacket(InetAddress address, int port) constructor to listen to broadcasts.
    • SO_BROADCAST

    
    • IP_TOS
