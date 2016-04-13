/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package c45.database;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author SAGAR
 */
public class InstanceCreator {
    Attribute aDuration;
    Attribute aProtocol_type;
    Attribute aService;
    Attribute aFlag;
    Attribute aSrc_Bytes;
    Attribute aDst_Bytes;
    Attribute aLand;
    Attribute aUrgent;
    Attribute aLogged_in;
    Attribute ais_host_login;
    Attribute ais_guest_login;
    Attribute aCount;
    Attribute aIntrutor;
    FastVector features;
    Instances instances;

    public InstanceCreator() 
    {
        aDuration=new Attribute("duration");
        FastVector protocol_values=new FastVector();
        protocol_values.addElement("udp");
        protocol_values.addElement("tcp");
        protocol_values.addElement("icmp");
        aProtocol_type=new Attribute("protocol_type",protocol_values);
        FastVector service_values=new FastVector();
        service_values.addElement("private");
        service_values.addElement("domain_u");
        service_values.addElement("http");
        service_values.addElement("HTTP");
        service_values.addElement("smtp");
        service_values.addElement("ftp_data");
        service_values.addElement("ftp");
        service_values.addElement("eco_i");
        service_values.addElement("other");
        service_values.addElement("auth");
        service_values.addElement("ecr_i");
        service_values.addElement("IRC");
        service_values.addElement("XLL");
        service_values.addElement("X11");
        service_values.addElement("Xll");
        service_values.addElement("finger");
        service_values.addElement("time");
        service_values.addElement("domain");
        service_values.addElement("telnet");
        service_values.addElement("pop_3");
        service_values.addElement("ldap");
        service_values.addElement("login");
        service_values.addElement("name");
        service_values.addElement("ntp_u");
        service_values.addElement("http_443");
        service_values.addElement("sunrpc");
        service_values.addElement("printer");
        service_values.addElement("systat");
        service_values.addElement("tim_i");
        service_values.addElement("netstat");
        service_values.addElement("remote_job");
        service_values.addElement("link");
        service_values.addElement("urp_i");
        service_values.addElement("sql_net");
        service_values.addElement("bgp");
        service_values.addElement("pop_2");
        service_values.addElement("tftp_u");
        service_values.addElement("uucp");
        service_values.addElement("impa4");
        service_values.addElement("pm_dump");
        service_values.addElement("nnsp");
        service_values.addElement("courier");
        service_values.addElement("daytime");
        service_values.addElement("iso_tsap");
        service_values.addElement("echo");
        service_values.addElement("discard");
        service_values.addElement("ssh");
        service_values.addElement("whois");
        service_values.addElement("mtp");
        service_values.addElement("gopher");
        service_values.addElement("rje");
        service_values.addElement("ctf");
        service_values.addElement("supdup");
        service_values.addElement("hostnames");
        service_values.addElement("csnet_ns");
        service_values.addElement("uucp_path");
        service_values.addElement("nntp");
        service_values.addElement("netbios_ns");
        service_values.addElement("netbios_dgm");
        service_values.addElement("netbios_ssn");
        service_values.addElement("vmnet");
        service_values.addElement("Z39_50");
        service_values.addElement("exec");
        service_values.addElement("shell");
        service_values.addElement("efs");
        service_values.addElement("klogin");
        service_values.addElement("kshell");
        service_values.addElement("icmp");
        aService=new Attribute("service",service_values);
        FastVector flag_values=new FastVector();
        flag_values.addElement("SF");
        flag_values.addElement("RSTR");
        flag_values.addElement("S1");
        flag_values.addElement("REJ");
        flag_values.addElement("S3");
        flag_values.addElement("RSTO");
        flag_values.addElement("S0");
        flag_values.addElement("S2");
        flag_values.addElement("RSTOS0");
        flag_values.addElement("SH");
        flag_values.addElement("OTH");
        aFlag=new Attribute("flag",flag_values);
        aSrc_Bytes=new Attribute("src_bytes");
        aDst_Bytes=new Attribute("dst_bytes");
        FastVector land_values=new FastVector();
        land_values.addElement("0");
        land_values.addElement("1");
        aLand=new Attribute("land",land_values);
        aUrgent=new Attribute("urgent");
        FastVector logged_in_values=new FastVector();
        logged_in_values.addElement("0");
        logged_in_values.addElement("1");
        aLogged_in=new Attribute("logged_in",logged_in_values);
        FastVector is_host_login_values=new FastVector();
        is_host_login_values.addElement("0");
        is_host_login_values.addElement("1");
        ais_host_login=new Attribute("is_host_login",is_host_login_values);
        FastVector is_guest_login_values=new FastVector();
        is_guest_login_values.addElement("0");
        is_guest_login_values.addElement("1");
        ais_guest_login=new Attribute("is_guest_login",is_guest_login_values);
        aCount =new Attribute("count");
        FastVector intrutor_values=new FastVector();
        intrutor_values.addElement("normal");
        /*intrutor_values.addElement("probe");
        intrutor_values.addElement("DOS");
        intrutor_values.addElement("U2R");
        intrutor_values.addElement("R2L");*/
        intrutor_values.addElement("anomaly");
        aIntrutor=new Attribute("intrutor",intrutor_values);
        features=new FastVector(13);
        features.addElement(aDuration);
        features.addElement(aProtocol_type);
        features.addElement(aService);
        features.addElement(aFlag);
        features.addElement(aSrc_Bytes);
        features.addElement(aDst_Bytes);
        features.addElement(aLand);
        features.addElement(aUrgent);
        features.addElement(aLogged_in);
        features.addElement(ais_host_login);
        features.addElement(ais_guest_login);
        features.addElement(aCount);
        features.addElement(aIntrutor);
        instances=new Instances("Rel",features,10);
        instances.setClassIndex(12);
  
    }
    
    public Instance createInstance(Long duration, String protocol_type, String service,String flag, int src_bytes, int dst_bytes,int land, int urgent, int logged_in, int is_host_login, int is_guest_login, int count, String Intrudor) 
    {
         Instance inst=new Instance(13);
       /* try {
           
            instances=new Instances(new FileReader(System.getProperty("user.dir")+"\\dataset1.arff"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InstanceCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
        Logger.getLogger(InstanceCreator.class.getName()).log(Level.SEVERE, null, ex);
        }*/
         
         inst.setDataset(instances);
         inst.setValue(aDuration,duration); 
         inst.setValue(aProtocol_type,protocol_type);
         inst.setValue(aService,service);
         inst.setValue(aFlag,flag);
         inst.setValue(aSrc_Bytes,src_bytes);
         inst.setValue(aDst_Bytes,dst_bytes);
         inst.setValue(aLand,land);
         inst.setValue(aUrgent,urgent);
         inst.setValue(aLogged_in,logged_in);
         inst.setValue(ais_host_login,is_host_login);
         inst.setValue(ais_guest_login,is_guest_login);
         inst.setValue(aCount,count);
         inst.setValue(aIntrutor,Intrudor);
         
         return inst;
    }
       
    public static void main(String args[])
    {
    InstanceCreator instance=new InstanceCreator();
    Instance inst=instance.createInstance(0L,"tcp","http","SF",45,45,0,1,0,1,0,2,"normal");
    System.out.println(inst.toString());
    }
}
