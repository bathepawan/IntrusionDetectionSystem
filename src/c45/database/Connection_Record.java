package c45.database;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
import weka.core.Instance;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author COMPSAKING
 */
public class Connection_Record 
{
    static Hashtable connection;
    static Hashtable temp;
    static Hashtable fin_temp;
    static Hashtable map_packets;
    static 
    JpcapCaptor capture;
    static FrmDisplayc45 frmDisplay;
    Classifier classifier;
    InstanceCreator instance;
    static Hashtable flag_analyze;
    static Hashtable udp_map;
    public Connection_Record() {
        connection=new Hashtable(30);
        temp=new Hashtable(5);
        fin_temp=new Hashtable(5);
        map_packets=new Hashtable();
        frmDisplay=new FrmDisplayc45();
        frmDisplay.setVisible(true);
        classifier=new Classifier();
        instance=new InstanceCreator();
        flag_analyze=new Hashtable();
        udp_map=new Hashtable();
    }
   public void capturePackets()
    {
        Packet packet=null;
        //try {
            //openFile(System.getProperty("user.dir")+"\\packetsCaptured\\packets");
            openDevice(0);
            
            while (!Thread.currentThread().isInterrupted()) {
                packet=capture.getPacket();
               
                   
                    if (!frmDisplay.CaptureFlag) 
                    {
                        
                        continue;
        
                    }
                    else
                    {
                        
                        if(packet==null)
                         {
                            /*try {
                                Thread.currentThread().sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Connection_Record.class.getName()).log(Level.SEVERE, null, ex);
                            }*/
                         }
                         else
                         {
                                analyze(packet);
                                
                         }
                             
                
            }
                    
              }
          //  }
        /*catch (IOException ex) {
           Logger.getLogger(Connection_Record.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        System.out.println("Partial connection:"+temp.size()+"\nConnection:"+connection.size());
                System.out.println("Partial Terminated:"+fin_temp.size());
   }
   public String flag_analyzer(TCPPacket src,TCPPacket dest)
   {
       String flag="OTH"; 
     if(Connection_Record.temp.containsKey(dest.dst_ip + ":" + dest.dst_port + "," + dest.src_ip + ":" + dest.src_port))
     {
     if(dest.rst)
     {
     flag="REJ";
     }
     }
     else if(Connection_Record.temp.containsKey(dest.dst_ip + ":" + dest.dst_port + "," + dest.src_ip + ":" + dest.src_port))
        {
            flag="S0";
        }
     else if(Connection_Record.connection.containsKey(dest.dst_ip + ":" + dest.dst_port + "," + dest.src_ip + ":" + dest.src_port))
     {
      if(src.fin && !dest.fin) {
             flag="S2";
         }
      else if(dest.fin && !src.fin) {
             flag="S3";
         }
      else if(src.rst){
             flag="RSTO";
      }
      else if(dest.rst){
          flag="RSTR";
      }
      else {
             flag="SF";
         }
     }
       
   return flag;
   }
   
   public String flag_analyzer(UDPPacket src,UDPPacket dest)
   {
       return "SF";
   }
public static void main(String args[])
    {
Connection_Record con=new Connection_Record();
con.capturePackets();
    }
private void openDevice(int dev_num)
{
        try {
            NetworkInterface devices[]=JpcapCaptor.getDeviceList();
            for(int i=0;i<devices.length;i++)
            {
            System.out.println(devices[i].description);
            }
            capture=JpcapCaptor.openDevice(devices[dev_num],65535, true,25);
            capture.setFilter("tcp or udp", true);
        } catch (IOException ex) {
            Logger.getLogger(Connection_Record.class.getName()).log(Level.SEVERE, null, ex);
        }


}
    private void openFile(String file)
    {
        try {
            capture = JpcapCaptor.openFile(file);
            
        } catch (IOException ex) {
            Logger.getLogger(Connection_Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void analyze(Packet packet)
    {
    TCPPacket tcp;
    UDPPacket udp;
    FileWriter rawData=null;
    String srcBytes=null;
    String destBytes=null;
    String instanceString=null;
    int land=0;
    long duration=0;
    String flag=null;
    Instance inst;
        if(packet instanceof TCPPacket)
        {
    
    TCPPacket srcPacket=null;
    try {
   System.out.println(packet.toString());              
        tcp = (TCPPacket) packet;
        
                if (tcp.syn) 
                {
                    //Connection_Record.temp.containsKey(tcp.src_ip+":"+tcp.src_port+","+tcp.dst_ip+":"+tcp.dst_port) || Connection_Record.temp.containsKey(tcp.dst_ip+":"+tcp.dst_port+","+tcp.src_ip+":"+tcp.src_port)
                    //checking whether connection is partially established if so then make it as full else put in partial connectin table
                    if (Connection_Record.temp.containsKey(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port)) {
                        Connection_Record.connection.put(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port, tcp.sec);
                        Connection_Record.temp.remove(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port);
                        System.out.println("Connection:" + tcp.dst_ip + ":" + tcp.dst_port + "->" + tcp.src_ip + ":" + tcp.src_port);
                    } else {
                        Connection_Record.temp.put(tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port, "P_Con" + Connection_Record.temp.size() + 1);
                        System.out.println("P_CON:" + tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port + "->" + Connection_Record.temp.get(tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port));
                    }
                } 
                rawData = new FileWriter(System.getProperty("user.dir") + "\\packetsCaptured\\rawData.txt", true);
               
    //            data=tcp.syn+","+tcp.fin+","+tcp.ack+","+tcp.ack_num+","+tcp.sequence+","+tcp.len;
/* Mappig of packets from source and destination
 * check whehther connection was there and by checking with dest ip and dest source, if entry exist then
 * we can conclude that packet is from dest to source. So extract source packet logged in hashtable else
 * put the packet in hash table
 */
                if(Connection_Record.connection.containsKey(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port))
                {
                    // data=data+""+tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port+" dest";
                    srcPacket=(TCPPacket)Connection_Record.map_packets.get(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port);
                    Connection_Record.map_packets.remove(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port);
                }
                    else
                {
                  //  if(Connection_Record.map_packets.put(tcp, data))
                    Connection_Record.map_packets.put(tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port,tcp);

                }
                        //if(Connection_Record.connection.containsKey(tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port))
        //        data=data+tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port+"  src";
                //else
                      //  data=data+"  unkonwon";
                
                if(srcPacket==null)
                {
               // frmDisplay.addEntry("m","m","m","m","m");
                //System.out.println("Connection Record not found!!!");
                }
                if(srcPacket!=null)
                {
               long time=Long.parseLong(Connection_Record.connection.get(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port).toString());
               duration=tcp.sec-time;

               if(tcp.src_port==tcp.dst_port) {
                        land=1;
                    }
               else {
                        land=0;
                    }
               //duration,protocol,service,srcBytes,dstBytes,flag,land,urgent,logged_in,host_login,guest_login,count
                  instanceString=duration+",tcp,http,"+srcPacket.length+","+tcp.length+",SF,"+land+",0,0,0,0,0";
                  rawData.write(instanceString+","+"\n");
                  flag=flag_analyzer(srcPacket, tcp);
                  inst=instance.createInstance(duration,"tcp","http",flag,srcPacket.length,tcp.length,land,0,0,0,0,0,"normal");
                  System.out.println(inst);
                  classifier.classify(inst);
                  System.out.println(inst);
                  Connection_Record.frmDisplay.addEntry(duration,"tcp","http",flag,srcPacket.length,tcp.length,land,0,0,0,0,0,"normal",inst,packet);
                }
    //rawData.write(tcp.src_ip+":"+tcp.src_port+"->"+tcp.dst_ip+":"+tcp.dst_port+","+tcp.syn+","+tcp.fin+"="+tcp.usec+"\n");
                //System.out.println(tcp.ack_num+","+tcp.sequence);
                if (tcp.fin) {
                    /*  if(Connection_Record.fin_temp.containsKey(tcp.src_ip+":"+tcp.src_port+","+tcp.dst_ip+":"+tcp.dst_port))
                    {
                    Connection_Record.connection.remove(tcp.src_ip+":"+tcp.src_port+","+tcp.dst_ip+":"+tcp.dst_port);
                    Connection_Record.connection.remove(tcp.dst_ip+":"+tcp.dst_port+","+tcp.src_ip+":"+tcp.src_port);
                    Connection_Record.fin_temp.remove(tcp.src_ip+":"+tcp.src_port+","+tcp.dst_ip+":"+tcp.dst_port);
                    System.out.println("Connection Terminated:"+tcp.src_ip+":"+tcp.src_port+","+tcp.dst_ip+":"+tcp.dst_port);
                    }*/
                    //checks whether connection is partially terminated if so then terminate fully otherwise make it as partial
                    if (Connection_Record.fin_temp.containsKey(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port)) {
                        Connection_Record.connection.remove(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port);
                        Connection_Record.connection.remove(tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port);
                        Connection_Record.fin_temp.remove(tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port);
                        System.out.println("Connection Terminated:" + tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port);
                    } else {
                        Connection_Record.fin_temp.put(tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port, "P_Ter_Con" + Connection_Record.fin_temp.size() + 1);
                        System.out.println("Partial Terminated:" + tcp.src_ip + ":" + tcp.src_port + "," + tcp.dst_ip + ":" + tcp.dst_port);
                    }
                }
    } catch (IOException ex) {
                Logger.getLogger(Connection_Record.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rawData.close();
                } catch (IOException ex) {
                    Logger.getLogger(Connection_Record.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

    }
        else if(packet instanceof UDPPacket)
		{
                UDPPacket srcPacket=null;
                System.out.println(packet.toString());              
                udp = (UDPPacket) packet;
                System.out.println("Hello");
                if(udp_map.containsKey(udp.dst_ip + ":" + udp.dst_port + "," + udp.src_ip + ":" + udp.src_port))
                {
                    // data=data+""+tcp.dst_ip + ":" + tcp.dst_port + "," + tcp.src_ip + ":" + tcp.src_port+" dest";
                    
                    srcPacket=(UDPPacket)udp_map.get(udp.dst_ip + ":" + udp.dst_port + "," + udp.src_ip + ":" + udp.src_port);
                    udp_map.remove(udp.dst_ip + ":" + udp.dst_port + "," + udp.src_ip + ":" + udp.src_port);
                }
                else{
                    udp_map.put(udp.src_ip + ":" + udp.src_port + "," + udp.dst_ip + ":" + udp.dst_port,udp);
                }
                     if(srcPacket==null)
                {
               // frmDisplay.addEntry("m","m","m","m","m");
                //System.out.println("Connection Record not found!!!");
                }
                     else if(srcPacket!=null)
                {
                    
              /**feature building */
               
               if(udp.src_port==udp.dst_port) {
                        land=1;
                    }
               else {
                        land=0;
                    }
                  flag=flag_analyzer(srcPacket,udp);
                  inst=instance.createInstance(duration,"udp","http",flag,srcPacket.length,udp.length,land,0,0,0,0,0,"normal");
                  System.out.println(inst);
                  classifier.classify(inst);
                  System.out.println(inst);
                  Connection_Record.frmDisplay.addEntry(duration,"udp","http",flag,srcPacket.length,udp.length,land,0,0,0,0,0,"normal",inst,packet);
               /** other features remaining:service(mapping),flag,urgent,logged_in,host_login,guest_login,count*/
                }
                //duration,protocol,service,srcBytes,dstBytes,flag,land,urgent,logged_in,host_login,guest_login,count
                
            //frmDisplay.addEntry("a", "null", "null", "null", "null");
        }
   //System.out.println(packet.toString());     
}

    
   
}
