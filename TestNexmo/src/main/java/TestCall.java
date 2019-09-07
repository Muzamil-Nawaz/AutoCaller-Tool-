

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
import com.nexmo.client.voice.Call;
import com.nexmo.client.voice.VoiceClient;
import com.nexmo.client.voice.ncco.Ncco;
import com.nexmo.client.voice.ncco.TalkAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

//import  com.nexmo.quickstart.Util.configureLogging;
//import  com.nexmo.quickstart.Util.envVar;

public class TestCall extends JFrame{
    JTextArea msg;
    TestCall(){
        setSize(500,500);
        setTitle("AutoCaller Tool");
        JLabel label = new JLabel("Type message here:");
        label.setBounds(0, 0, 250, 30);
        add(label);
        
        msg = new JTextArea();
        msg.setBounds(0, 40, 450, 250);
        add(msg);
        
        
        JButton selectCsv = new JButton("Select CSV target file from here");
        selectCsv.setBounds(100,300,250,30);
        add(selectCsv);
        selectCsv.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                try{
                    BufferedReader br = null;
                    JFileChooser fc = new JFileChooser();
                    int result = fc.showOpenDialog(null);
                    String f = fc.getSelectedFile().getPath();
                    if(result==JFileChooser.APPROVE_OPTION){
                        Object o =JOptionPane.showInputDialog("Please input Contact Column No:");
                        ArrayList list = new ArrayList<>();
                        br = new BufferedReader(new FileReader(f));
                        String s = br.readLine();
                        while(s!=null){
                            String[] ss =s.split(",");
                            makeCall(ss[Integer.parseInt(o.toString())-1]);
                            s=br.readLine();
                        }
                        JOptionPane.showMessageDialog(null,"Congratulations sir, Voice call has been forwarded to all your targets");
                    
                    }
                
                    
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Some problem occured ,please try again.");
                    ex.printStackTrace();
                    //Logger.getLogger(TestCall.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        });
        
        JButton selectExcel = new JButton("Selecr Excel target file from here");
        selectExcel.setBounds(100, 340, 250, 30);
        add(selectExcel);
        selectExcel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                try{
                    BufferedReader br = null;
                    JFileChooser fc = new JFileChooser();
                    int result = fc.showOpenDialog(null);
                    String f = fc.getSelectedFile().getPath();
                    if(result==JFileChooser.APPROVE_OPTION){
                        Object o =JOptionPane.showInputDialog("Please input Contact Column No:");
                        Workbook wb=Workbook.getWorkbook(new File(f));
                        Sheet s=wb.getSheet(0);
                        int row=s.getRows();
                        int col=s.getColumns();
                        for (int i = 0; i < row; i++) {
                                 Cell c=s.getCell(Integer.parseInt(o.toString())-1, i);
                                 makeCall(c.getContents());
                        }
                        JOptionPane.showMessageDialog(null,"Congratulations sir, Voice call has been forwarded to all your targets");
                    
                    }
                     }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Some problem occured with file data or something,please try again.");
                    ex.printStackTrace();
                    //Logger.getLogger(TestCall.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
            }
        });
        
        JButton exit = new JButton("Exit");
        exit.setBounds(100, 380, 250, 30);
        add(exit);
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                try {
                    makeCall("s");
                    JOptionPane.showMessageDialog(null, "Thank you for using our services,have a good day :).");
                } catch (IOException ex) {
                    Logger.getLogger(TestCall.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NexmoClientException ex) {
                    Logger.getLogger(TestCall.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) throws Exception {
        //configureLogging();
        new TestCall();
        //voiceClient.createCall(call);
        //voiceClient.createCall(call);
        // message code***************************************************************************************************
        
     


        //client.getVoiceClient().createCall(new Call(TO_NUMBER, NEXMO_NUMBER, ANSWER_URL));
    }
    public void makeCall(String num) throws IOException, NexmoClientException{
         final String NEXMO_APPLICATION_ID = "876056bd-3c79-4b5e-b204-53568f9ca720";
        final String NEXMO_PRIVATE_KEY_PATH = "G:\\Netbeans Project\\TestNexmo\\private (1).key";

        NexmoClient client = NexmoClient.builder()
                .applicationId(NEXMO_APPLICATION_ID)
                .privateKeyPath(NEXMO_PRIVATE_KEY_PATH)
                .build();
        VoiceClient voiceClient = client.getVoiceClient();
        
        TalkAction intro = TalkAction.builder(msg.getText()).build();
        Ncco ncco = new Ncco(intro);
        final String NEXMO_NUMBER = "+923222592161";
        final String TO_NUMBER = "+923053110041";
        final String ANSWER_URL = "https://nexmo-community.github.io/ncco-examples/first_call_talk.json";
        
        System.out.println(num+"num");
        Call call = new Call(TO_NUMBER, NEXMO_NUMBER, ncco);
        voiceClient.createCall(call);
        JOptionPane.showMessageDialog(null, "Call has been broadcasted.");
        File output = new File("Output.txt");
        FileWriter fw = new FileWriter(output,true);
        DateFormat d = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        fw.write("Called :"+TO_NUMBER+" on "+d.format(date)+"\n");
        fw.write("\n");
        fw.close();
        
        
    }
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
