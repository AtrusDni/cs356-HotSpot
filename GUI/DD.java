import javax.swing.*;
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

// DUMP FILE MUST BE DUMP.TXT
// DUMP.TXT MUST BE NEXT TO SRC FILE (NETBEANS)

public class DD implements ActionListener {
    
    // global buttons used for action listeners, event actions, other
    // methods, etc
    File f;
    JLabel status;
    JLabel dlStatus;
    JLabel sleeper;
    JFrame frame;
    JButton deadlock;
    JButton loadFile;
	String path, fName, fDir, s;
    Path currentRelativePath;
    Timer t;
	Process p;
    
    // runs gui
    DD() {
        
		currentRelativePath = Paths.get("");
		s = currentRelativePath.toAbsolutePath().toString();
		path = "/home/nicolas/cs356-HotSpot/gui/";
		
        // main frame
        frame = new JFrame("Java HotSpot GUI - Deadlock Detection");
        frame.setSize(550, 275);
        frame.setContentPane(new JLabel(new ImageIcon
                (path+"/blue_background.jpg")));
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
        
        // SCRAPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPED
        // JTEXTAREA
        /*JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setToolTipText("Input your own code!");
        
        // SCROLL PANE FOR TEXT AREA
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(240, 420)); */
        // SCRAPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPED
        
        // button action listener
        loadFile.addActionListener(this);
        deadlock.addActionListener(this);
        
        // JPanel for greeting
        JPanel greeting = new JPanel();
        greeting.setPreferredSize(new Dimension(500, 50));
        greeting.setOpaque(false);
        greeting.setLayout(new GridBagLayout());
        
        // JLabel for welcome greeting
        JLabel welcome = new JLabel("Welcome to Java HotSpot "
                                  + "-- DeadLock Detection");
        welcome.setFont(welcome.getFont().deriveFont(20.2f));
        welcome.setForeground(Color.WHITE);
        greeting.add(welcome);
        
        // JPanel 1 (WEST <<)
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(250, 250));
        panel1.setOpaque(false);
        panel1.setBorder(BorderFactory.createEmptyBorder());
        
        // JPanel 3 (combined with panel 1 to separate buttons)
        JPanel panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(250, 250));
        panel3.setOpaque(false);
        panel3.setBorder(BorderFactory.createEmptyBorder());
        
        // JPanel 2 (EAST >>)
        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(250, 250));
        panel2.setOpaque(false);
        panel2.setBorder(BorderFactory.createEmptyBorder());
        panel2.setLayout(new FlowLayout());
        
        // add things to JPanel 1
        //panel1.add(scroll); SCRAPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPED
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
				fName = file.getSelectedFile().getName();
				fName = fName.replace(".java","");
				System.out.println(fName);
				fDir = file.getCurrentDirectory().toString();
                if (f.getName().length() > 5) {
                    status.setText("<HTML><font color=white><U>Java File Status</U>: <br>" 
                               + f.getName() + " has been loaded</font></HTML>");
                    dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: </font>"
                            + "<font color=#8CC739>Ready!</font></html>");
                    deadlock.setToolTipText("File loaded! Go ahead and use!");
                    deadlock.setEnabled(true);
                }
                else {
                    status.setText("<html><font color=white><U>Java File Status</U>: " 
                               + f.getName() + " has been loaded</font></html>");
                    dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: </font>"
                            + "<font color=#8CC739>Ready!</font></html>");
                    deadlock.setToolTipText("File loaded! Go ahead and use!");
                    deadlock.setEnabled(true);
                }
            }
            else {
                status.setText("<html><font color=white><U>Java File Status</U>: Cancelled</font></html>");
                dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: "
                            + "File Cancelled</font></html>");
                deadlock.setToolTipText("Load file first!");
                deadlock.setEnabled(false);
            }
        }
        else if (ae.getActionCommand().equals("DeadLock Detection")) {
            
            dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>:</font> "
                            + "<font color=#F26C4F>In Progress...</font></html>");
            //starts script
			try {
                //starts script
				p = Runtime.getRuntime().exec(s + "/run.sh " + "compile " + fDir + " " + fName + " " + s);
				System.out.println("Waiting...");
				p.waitFor();
				System.out.println("DONE!");
				popup();
			} catch (IOException ex) {
                Logger.getLogger(DD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DD.class.getName()).log(Level.SEVERE, null, ex);
            }
			
            // set text to wait 10 seconds
            sleeper.setText("<html><font color=#CE0000>Please wait a few seconds...</font></html>");
            sleeper.setFont(sleeper.getFont().deriveFont(16.0f));
            
            // sleep timer
            //t = new Timer();
			
            //t.schedule(new SleepForTask(), 10000); 
            // end sleep timer
            
        }
    }
    
    // for the pop up
    // will read dump file and output to
    // an uneditible textarea that is also scrollable
    // adjust size by setting the dimensions of the scroll pane
    public void popup() throws IOException {
        
        // JTEXTAREA
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        //area.setToolTipText("This is the dump file!");
        area.setEditable(false);

        // to read dump file
        // output to textarea
        File dumpFile = new File("dump2.txt");
        Scanner readDump = new Scanner(dumpFile);
        while(readDump.hasNext()) {
            String next = readDump.nextLine();
            area.append(next + "\n");
        }
        
        // SCROLL PANE FOR TEXT AREA
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 500));

        JPanel popup = new JPanel();
        popup.add(scroll);
        
        int result = JOptionPane.showConfirmDialog(null, popup,
            "DeadLock Detection Results", 
            JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            sleeper.setText("");
            dlStatus.setText("<html><font color=white><U>DeadLock Detection</U>: </font>"
                            + "<font color=#8CC739>Ready!</font></html>");
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
                new DD();
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
