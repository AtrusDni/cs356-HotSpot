import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.text.html.HTMLDocument;

// DUMP FILE MUST BE DUMP.TXT
// DUMP.TXT MUST BE NEXT TO SRC FILE (NETBEANS)

public class DD1 implements ActionListener {
    
    // global buttons used for action listeners, event actions, other
    // methods, etc
    File f;
    JLabel status;
    JLabel dlStatus;
    JLabel sleeper;
    JFrame frame;
    JButton deadlock, loadFile, close, exit;
    String path, fName, fDir, s, line;
    Path currentRelativePath;
    Timer t;
    Process p;
    JPanel popup, popupHighlight;
    JPanel panel1, panel2, panel3, panel4;
    
    JEditorPane ep;
    String linkToSource = "";
    
    // runs gui
    DD1() {
        
	currentRelativePath = Paths.get("");
	s = currentRelativePath.toAbsolutePath().toString();
		
        // main frame
        frame = new JFrame("Java HotSpot GUI - Deadlock Detection");
        frame.setSize(600, 300);
        frame.setContentPane(new JLabel(new ImageIcon
                (s+"/blue_background.jpg")));
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        // BUTTON FOR LOADING FILE
        loadFile = new JButton("Load a file...");
        loadFile.setPreferredSize(new Dimension(200, 50));
        loadFile.setFont(loadFile.getFont().deriveFont(15.0f));
        loadFile.setMnemonic('l');
        
        // BUTTON TO START DEADLOCK
        deadlock = new JButton("DeadLock Detection");
        deadlock.setPreferredSize(new Dimension(200, 50));
        deadlock.setFont(loadFile.getFont().deriveFont(15.0f));
        deadlock.setToolTipText("Load file first!");
        deadlock.setMnemonic('d');
        deadlock.setEnabled(false);
        
        // JLabel -- status of file loading
        status = new JLabel("<html><font color=white><U>Java File Status</U>: No File Loaded</font></html>");
        status.setToolTipText("Status of the Java file to be loaded.");
        
        // JLabel -- deadlock status
        dlStatus = new JLabel("<html><font color=white><U>DeadLock Detection</U>: No File Loaded</font></html>");
        dlStatus.setToolTipText("Status of DeadLock Detection function.");
        
        // JLabel -- sleeper -- timer
        sleeper = new JLabel("");
        
        // button action listener
        loadFile.addActionListener(this);
        deadlock.addActionListener(this);
        
        // JPanel for greeting
        JPanel greeting = new JPanel();
        greeting.setPreferredSize(new Dimension(500, 50));
        greeting.setOpaque(false);
        greeting.setLayout(new GridBagLayout());
        
        // JLabel for welcome greeting
        JLabel welcome = new JLabel("Java HotSpot "
                                  + "-- DeadLock Detection");
        welcome.setFont(welcome.getFont().deriveFont(20.2f));
        welcome.setForeground(Color.WHITE);
        greeting.add(welcome);
        
        // JPanel 1 (WEST <<)
        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(250, 250));
        panel1.setOpaque(false);
        panel1.setBorder(BorderFactory.createEmptyBorder());
        
        // JPanel 3 (combined with panel 1 to separate buttons)
        panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(250, 250));
        panel3.setOpaque(false);
        panel3.setBorder(BorderFactory.createEmptyBorder());
        
        // JPanel 2 (EAST >>)
        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(300, 250));
        panel2.setOpaque(false);
        panel2.setBorder(BorderFactory.createEmptyBorder());
        panel2.setLayout(new FlowLayout());
        
        // add things to JPanel 1
        panel1.add(loadFile);
        panel3.add(deadlock);
        panel1.add(panel3);
        
        // add things to JPanel 2
        panel2.add(status);
        panel2.add(dlStatus);
        panel2.add(sleeper);
        
        // add things to JFrame
        frame.getContentPane().add(greeting, BorderLayout.NORTH);
        frame.getContentPane().add(panel1, BorderLayout.CENTER);
        //frame.getContentPane().add(panel3, BorderLayout.SOUTH);
        frame.getContentPane().add(panel2, BorderLayout.EAST);
        
	JPanel panel4 = new JPanel();
        panel4.setPreferredSize(new Dimension(150, 150));
        panel4.setOpaque(false);
        panel4.setBorder(BorderFactory.createEmptyBorder());
        panel4.setLayout(new FlowLayout());
	close = new JButton("Close");
	close.addActionListener(this);
	panel4.add(close);
	frame.add(panel4, BorderLayout.SOUTH);
        
        // set frame visibility
        frame.setVisible(true);
    }
    
    // for loading file button
    // CAN be exclusive (attach to action listener declaration)
    public void actionPerformed(ActionEvent ae){
        // if the button is load a file...
        if (ae.getActionCommand().equals("Load a file...")) {
            // creates file chooser that has a filter (only allows .java files)
            JFileChooser file = new JFileChooser();
            file.setFileFilter(new JavaFileFilter());
            int returnVal = file.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                f = file.getSelectedFile();
                linkToSource = file.getSelectedFile().getAbsolutePath();
				fName = file.getSelectedFile().getName();
				fName = fName.replace(".java","");
				System.out.println(fName);
				fDir = file.getCurrentDirectory().toString();
                if (f.getName().length() > 5) {
                    status.setText("<HTML><font color=white><U>Java File Status</U>: <br>" 
                               + f.getName() + " has been loaded</font></HTML>");
                    dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: </font>"
                            + "<font color=#8CC739>Ready!</font></html>");
                    sleeper.setText("<html><font color=#CE0000>Press Detection button and wait...</font></html>");
                    deadlock.setToolTipText("File loaded! Go ahead and use!");
                    deadlock.setEnabled(true);
                }
                else {
                    status.setText("<html><font color=white><U>Java File Status</U>: " 
                               + f.getName() + " has been loaded</font></html>");
                    dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: </font>"
                            + "<font color=#8CC739>Ready!</font></html>");
                    sleeper.setText("<html><font color=#CE0000>Press Detection button and wait...</font></html>");
                    deadlock.setToolTipText("File loaded! Go ahead and use!");
                    deadlock.setEnabled(true);
                }
            }
            else {
                status.setText("<html><font color=white><U>Java File Status</U>: Cancelled</font></html>");
                dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: "
                            + "File Cancelled</font></html>");
                sleeper.setText("");
                deadlock.setToolTipText("Load file first!");
                deadlock.setEnabled(false);
            }
        }
        else if (ae.getActionCommand().equals("DeadLock Detection")) {
            
            dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>:</font> "
                            + "<font color=#F26C4F>In Progress...</font></html>");
            sleeper.setText("<html><font color=#CE0000>Press Detection button and wait...</font></html>");
	    //t.schedule(new SleepForTask(), 5000); 
            //starts script
			try {
                //starts script
				line = s + "/run.sh " + "compile " + fDir + " " + fName + " " + s;
				p = Runtime.getRuntime().exec(line);
				System.out.println("Compiling...");
				p.waitFor();
				System.out.println("Finished compliling!");
			} catch (IOException ex) {
                Logger.getLogger(DD1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DD1.class.getName()).log(Level.SEVERE, null, ex);
            }

		try {
				line = s + "/run.sh " + "runit " + fDir + " " + fName + " " + s;
				p = Runtime.getRuntime().exec(line);
				System.out.println("Checking program for deadlocks...");
				p.waitFor();
				System.out.println("ALL DONE! Displaying results...");
			} catch (IOException ex) {
                Logger.getLogger(DD1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DD1.class.getName()).log(Level.SEVERE, null, ex);
            }
			
            // set text to wait 10 seconds
		t = new Timer();
            t.schedule(new SleepForTask(), 100); 
       	System.out.println("DONE!");
            
            // sleep timer
            
            // end sleep timer
            
        }
	else if (ae.getActionCommand().equals("Close")) {
		System.exit(0);
	}
    }
    
    // for the pop up
    // will read dump file and output to
    // an uneditible textarea that is also scrollable
    // adjust size by setting the dimensions of the scroll pane
    public void popup() throws IOException {

        // JEDITORPANE (HANDLES HTML)
        ep = new JEditorPane();
        ep.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        ep.setEditable(false);
        
        // for Courier font bc of reasons
        Font font = new Font("Courier New", Font.PLAIN, 14);
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize() + "pt; }";
        ((HTMLDocument)ep.getDocument()).getStyleSheet().addRule(bodyRule);
        
        // for hyperlinking
        // replaces (library).java:(number) with the link to source code
        // source code opens on desktop
        ep.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            int checkCount = Integer.parseInt(e.getDescription());
                            JTextArea textArea = new JTextArea();
                            Highlighter hl = textArea.getHighlighter();
                            hl.removeAllHighlights();
                            try {
                                File lineFile = new File(linkToSource);
                                Scanner scan = new Scanner(lineFile);
                                int counter = 1;
                                while (scan.hasNext()) {
                                    String currentLine = scan.nextLine();
                                    textArea.append(currentLine + "\n");
                                    counter++;
                                }
                                textArea.getHighlighter().addHighlight(textArea.getLineStartOffset(checkCount-1),
                                        textArea.getLineEndOffset(checkCount-1), 
                                        DefaultHighlighter.DefaultPainter);
                            }
                            catch (Exception t) {
                                // catch something
                            }

                            JScrollPane lineScroll = new JScrollPane(textArea);
                            lineScroll.setPreferredSize(new Dimension(700, 600));

                            popupHighlight = new JPanel();
                            popupHighlight.add(lineScroll);


                            int result = JOptionPane.showConfirmDialog(null, popupHighlight,
                            f.getName() + " Source Code", 
                            JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (result == JOptionPane.OK_OPTION) {
                                sleeper.setText("");
                                dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: </font>"
                                            + "<font color=#8CC739>Ready!</font></html>");
                                sleeper.setText("<html><font color=#CE0000>Press Detection button and wait...</font></html>");
                                //sleeper.setText("<html><font color=#CE0000>cleaning directory...</font></html>");
                                p = Runtime.getRuntime().exec(s + "/run.sh " + "clean " + s);
                                //sleeper.setText("<html><font color=#CE0000>Ready!</font></html>");
                            }
                        }
                        catch (IOException ex) {
                            // nothing needed here
                        }
                    }
                }
            }
        });
        
        // to read dump file
        // output to textarea
        File dumpFile = new File("dump2.txt");
        Scanner readDump = new Scanner(dumpFile); 
        String appendAllDump = "";
        int line;
        while(readDump.hasNext()) {
            String next = readDump.nextLine();
            String stacking = "";
            int indexStart = next.indexOf(f.getName());
            if (indexStart != -1) {
                
                String fullSub = next.substring(next.lastIndexOf(f.getName()));
                String sub = "";
                int length = f.getName().length() + 1;
                for (int i = 0; i < fullSub.length(); i++) {
                    if (fullSub.charAt(i) == ')') {
                        sub = fullSub.substring(length, i);
                    }
                }
                int lockNum = Integer.parseInt(sub);
                
                /*for (int k = indexStart; k > 0; k--) {
                    if (next.charAt(k) == '(') {
                        indexStart = k + 1;
                    }
                }*/
                for (int i = indexStart; i < next.length() - 1; i++) {
                    stacking = stacking.concat(next.charAt(i) + "");
                }
                fullSub = fullSub.substring(0, fullSub.length() - 1);
                next = next.replaceAll(stacking, "<a href=\"" + lockNum
                        + "\">" + fullSub + "</a>");
            }
            appendAllDump = appendAllDump + next + "<br>";
        }
        ep.setText(appendAllDump);
        
        // SCROLL PANE FOR TEXT AREA
        JScrollPane scroll = new JScrollPane(ep);
        scroll.setPreferredSize(new Dimension(650, 500));


        popup = new JPanel();
        popup.add(scroll);
        
        int result = JOptionPane.showConfirmDialog(null, popup,
            "DeadLock Detection Results", 
            JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            sleeper.setText("");
            dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: </font>"
                            + "<font color=#8CC739>Ready!</font></html>");
            sleeper.setText("<html><font color=#CE0000>Press Detection button and wait...</font></html>");
	    //sleeper.setText("<html><font color=#CE0000>cleaning directory...</font></html>");
	    p = Runtime.getRuntime().exec(s + "/run.sh " + "clean " + s + " " + fName);
	    //sleeper.setText("<html><font color=#CE0000>Ready!</font></html>");
        }
    }
    
    // timer
    class SleepForTask extends TimerTask {
        @Override
        public void run() {
            try {
                popup();
            }
            catch (IOException ioe) {
                // catch
                // need to bypass the stupid throws IOException...
            }
            t.cancel();
        }
    }
    // end timer
    
    // prepares to run the main program (gui)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DD1();
            }
        });
    }
}

// will only allow .java files to be opened/searched/shown
class JavaFileFilter extends FileFilter {
    public boolean accept(File file) {
        if(file.getName().endsWith(".java")) {
            return true;
        }
        if(file.isDirectory()) {
            return true;
        }
        return false;
    }
    
    public String getDescription() {
        return "Java Source Code Files";
    }
}

