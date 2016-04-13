/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idsc45;
import c45.database.*;


/**
 *
 * @author COMPSAKING
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Connection_Record con=new Connection_Record();
        con.capturePackets();
    }

}
