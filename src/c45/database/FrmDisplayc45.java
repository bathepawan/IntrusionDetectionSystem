package c45.database;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import weka.core.Instance;

class myTimerTaskc45 extends TimerTask
{

    FrmDisplayc45 parent;
    BufferedImage bi;
    Graphics2D g;
    double steps = 0;
    boolean flag;
    int mm;
    char[] sarr = new char[]{'C', 'A', 'P', 'T', 'U', 'R', 'I', 'N', 'G', '.', '.', '.'};

    public myTimerTaskc45(FrmDisplayc45 p) {
        this.parent = p;
        bi = new BufferedImage(300, 22, BufferedImage.TYPE_INT_RGB);
        g = bi.createGraphics();
        g.drawRect(1, 1, 299, 21);
        parent.jLabelAnimate.setIcon(new ImageIcon(bi));
        flag = true;
    }

    public void run() {
        if (parent.CaptureFlag) {
            if (flag) {
                g.setColor(new Color(parent.currRecord + 50, parent.currRecord + 50, 0));
                g.drawLine(150 + parent.currRecord, 2, 150 + parent.currRecord, 18);
                g.drawLine(150 - parent.currRecord, 2, 150 - parent.currRecord, 18);
                parent.jLabelAnimate.setIcon(new ImageIcon(bi));
                parent.currRecord += 3;
                if (parent.currRecord == 150) {
                    flag = false;
                }
            } else {
                g.setColor(Color.BLACK);
                g.drawLine(150 + parent.currRecord, 2, 150 + parent.currRecord, 18);
                g.drawLine(150 - parent.currRecord, 2, 150 - parent.currRecord, 18);
                parent.jLabelAnimate.setIcon(new ImageIcon(bi));
                parent.currRecord -= 3;

                if (parent.currRecord == 0) {
                    flag = true;
                }
            }
            mm++;
            if (sarr.length == (mm / 2)) {
                mm = 1;
            }
            String s = new String(sarr, 0, mm / 2);
            parent.jLabelAnimate.setText(s);
        } else {
            parent.jLabelAnimate.setText("CAPTURING STOPPED");
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class FrmDisplayc45 extends javax.swing.JFrame {

    /** Creates new form FrmDisplay */
    public boolean CaptureFlag = false;
    DefaultTableModel tm;
    DefaultTableModel attackTm;
    public int currRecord;
    myTimerTaskc45 task;
    Timer t;
    Object[] colHeader = new Object[]{"Duration", "Protocol", "Service", "Flag","Src Bytes","Dest. Bytes","Land","Urgent","Logged In","Is Host Login","Is Guest Login","Count"};
    Object[] colHeaderAttack=new Object[]{"Source IP","Destination IP","Date & Time","Attack Type"};
    File f;
    boolean started = false;
    
    public FrmDos frmdos = new FrmDos();
    public FrmProbe frmprobe = new FrmProbe();
    public FrmNormal frmnormal = new FrmNormal();
    public FrmU2R frmu2r = new FrmU2R();
    public FrmR2L frmr2l = new FrmR2L();
    
    public FrmDisplayc45() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception ex) {
            System.out.println("Failed loading L&F: ");
            System.out.println(ex);
            System.out.println("Loading default Look & Feel Manager!");
        }
        initComponents();
        Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
     //   setLocation(sd.width / 2 - this.getWidth() / 2, sd.height / 2 - this.getHeight() / 2);
        setLocation(sd.width/2-this.getWidth()/2, sd.height/2-this.getHeight()/2);      
        tm = new DefaultTableModel(colHeader, 0);
        attackTm=new DefaultTableModel(colHeaderAttack,0);
        task = new myTimerTaskc45(this);
        t = new Timer();
        t.schedule(task, 100, 100);
        started = true;
        jCheckBox1.setVisible(true);
        

    }
    public void  addattack(String source_ip,String dest_ip,String time,String attack_type)
    {
        Object colData[] = new Object[]{source_ip,dest_ip,time,attack_type};        
   attackTm.insertRow(0, colData);
        jTable2.setModel(attackTm);
    }
public static void main(String args[])
    {
FrmDisplayc45 frm= new FrmDisplayc45();
frm.setVisible(true);
while(true)
{
//frm.addEntry("a","b","c","d","e");
frm.addattack("a","b","c","d");
}
    }

    void addEntry(Long duration, String protocol_type, String service,String flag, int src_bytes, int dst_bytes,int land, int urgent, int logged_in, int is_host_login, int is_guest_login, int count, String Intrudor, Instance instance,Packet packet) {
        Object colData[] = new Object[]{duration,protocol_type,service,flag,src_bytes,dst_bytes,land,urgent,logged_in,is_host_login,is_guest_login,count};
        tm.insertRow(0, colData);
        jTable1.setModel(tm);
        String date=null;
         Calendar dateInst;
        dateInst = Calendar.getInstance();
        date=dateInst.getTime().toString();
        if(instance.classValue()==1)
        {
        if(packet instanceof TCPPacket)
        {
        TCPPacket tcp=(TCPPacket)packet;
        addattack(tcp.src_ip.toString(),tcp.dst_ip.toString(),date,"anamoly");
        }
        }
        if (jCheckBox1.isSelected()) {
            try{
                FileWriter fop=new FileWriter(f,true);
                if(f.exists()){
                    fop.append(instance.toString());
                    fop.flush();
                    fop.close();
                }
                else
                {
                f.createNewFile();
                }
            }catch(Exception e){
                System.out.println("Error Creating File : " + e);
            }
        }
        
        /*
        if(b.equalsIgnoreCase("dos")){
            frmdos.addEntry(a, b, c, d, data.getBytes());
        }else if(b.equalsIgnoreCase("probe")){
            frmprobe.addEntry(a, b, c, d, data.getBytes());
        }else if(b.equalsIgnoreCase("normal")){
            frmnormal.addEntry(a, b, c, d, data.getBytes());
        }else if(b.equalsIgnoreCase("u2r")){
            frmu2r.addEntry(a, b, c, d, data.getBytes());
        }else if(b.equalsIgnoreCase("r2l")){
            frmr2l.addEntry(a, b, c, d, data.getBytes());
        }*/
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabelAnimate = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("CLOSE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setText("START CAPTURE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setText("STOP CAPTURE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabelAnimate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAnimate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBox1.setText("SAVE TO FILE");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setText("VIEW DATASET");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton5.setText("VIEW RESULT");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jToggleButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jToggleButton1.setText("DOS");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jToggleButton2.setText("PROBE");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jToggleButton3.setText("NORMAL");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jToggleButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jToggleButton4.setText("U2R");
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jToggleButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jToggleButton5.setText("R2L");
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 448, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelAnimate, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 747, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelAnimate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton2)
                    .addComponent(jToggleButton3)
                    .addComponent(jToggleButton4)
                    .addComponent(jToggleButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CaptureFlag = true;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        CaptureFlag = false;
        jCheckBox1.setSelected(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        // TODO add your handling code here:
        if(started && jCheckBox1.isSelected()){
            createFile();
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new DlgViewOriginalData(this, true).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        new DlgViewSelectedFile(this, true).setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        if(jToggleButton1.isSelected()){
            frmdos.setVisible(true);
        }else{
            frmdos.setVisible(false);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
        if(jToggleButton2.isSelected()){
            frmprobe.setVisible(true);
        }else{
            frmprobe.setVisible(false);
        }
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        if(jToggleButton3.isSelected()){
            frmnormal.setVisible(true);
        }else{
            frmnormal.setVisible(false);
        }
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // TODO add your handling code here:
        if(jToggleButton4.isSelected()){
            frmu2r.setVisible(true);
        }else{
            frmu2r.setVisible(false);
        }
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        // TODO add your handling code here:
        if(jToggleButton5.isSelected()){
            frmr2l.setVisible(true);
        }else{
            frmr2l.setVisible(false);
        }
    }//GEN-LAST:event_jToggleButton5ActionPerformed
    
    void createFile(){
        String s = "@relation packet\n@attribute duration real\n@attribute protocol_type {udp, tcp, icmp} \n@attribute service {private,domain_u,http,HTTP,smtp, ftp_data,ftp,eco_i, other,auth,ecr_i, IRC,XLL,X11,Xll, finger, time, domain, telnet, pop_3, ldap, login, name, ntp_u, http_443, sunrpc, printer, systat, tim_i, netstat, remote_job, link, urp_i, sql_net, bgp, pop_2, tftp_u, uucp, imap4, pm_dump, nnsp, courier, daytime, iso_tsap, echo,discard, ssh, whois, mtp, gopher, rje, ctf, supdup, hostnames, csnet_ns, uucp_path, nntp, netbios_ns, netbios_dgm, netbios_ssn, vmnet, Z39_50, exec, shell, efs, klogin, kshell, icmp}\n";
        s += "@attribute flag {SF, RSTR, S1, REJ, S3, RSTO, S0, S2, RSTOS0,SH, OTH}\n@attribute src_bytes real\n@attribute dst_bytes real\n@attribute land {0, 1}\n";
        s += "@attribute urgent real\n@attribute logged_in {0, 1}\n@attribute is_host_login  {0, 1}\n@attribute is_guest_login{0, 1}\n@attribute count real\n@attribute Intrutor {normal,probe,DOS,U2R,R2L}\n";
        s += "@data\n\n";
        Calendar c = Calendar.getInstance();
        String dt = c.get(c.DAY_OF_MONTH) + "_" + c.get(c.MONTH) + "_" + c.get(c.YEAR) + "_" + c.get(c.HOUR_OF_DAY) + "_" + c.get(c.MINUTE) + "_" + c.get(c.SECOND) + ".arff";
        f = new File(System.getProperty("user.dir") + "\\datasets\\" + dt);
        try{
            FileOutputStream fop=new FileOutputStream(f,false);
            if(f.exists()){

                fop.write(s.getBytes());
                //  fop.write("\n".getBytes());
                fop.flush();
                fop.close();
            }
        }catch(Exception e){
            System.out.println("Error Creating File : " + e);
        }
    }
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                new FrmDisplay().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    public javax.swing.JLabel jLabelAnimate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTable jTable1;
    public javax.swing.JTable jTable2;
    public javax.swing.JToggleButton jToggleButton1;
    public javax.swing.JToggleButton jToggleButton2;
    public javax.swing.JToggleButton jToggleButton3;
    public javax.swing.JToggleButton jToggleButton4;
    public javax.swing.JToggleButton jToggleButton5;
    // End of variables declaration//GEN-END:variables

    
        
    
}
