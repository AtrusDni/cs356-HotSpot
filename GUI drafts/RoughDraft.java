package roughdraft;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import java.io.*;

public class RoughDraft implements ActionListener {
    
    // global buttons used for action listeners, event actions, other
    // methods, etc
    File f;
    JLabel status;
    JLabel dlStatus;
    JFrame frame;
    JButton deadlock;
    JButton loadFile;
    
    // runs gui
    RoughDraft() {
        
        // main frame
        frame = new JFrame("Java HotSpot GUI (ROUGH DRAFT)");
        frame.setSize(500, 375);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        // BUTTON FOR LOADING FILE
        loadFile = new JButton("Load a file...");
        loadFile.setPreferredSize(new Dimension(225, 100));
        loadFile.setFont(loadFile.getFont().deriveFont(20.0f));
        loadFile.setMnemonic('l');
        
        // BUTTON TO START DEADLOCK
        deadlock = new JButton("DeadLock Detection");
        deadlock.setPreferredSize(new Dimension(225, 100));
        deadlock.setFont(loadFile.getFont().deriveFont(20.0f));
        deadlock.setToolTipText("Load file first!");
        deadlock.setMnemonic('d');
        deadlock.setEnabled(false);
        
        // JLabel -- status of file loading
        status = new JLabel("<html><U>Java File Status</U>: No File Loaded</html>");
        status.setToolTipText("Status of the Java file to be loaded.");
        
        // JLabel -- deadlock status
        dlStatus = new JLabel("<html><U>DeadLock Detection</U>: No File Loaded</html>");
        dlStatus.setToolTipText("Status of DeadLock Detection function.");
        
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
        
        // JPanel for greeting
        JPanel greeting = new JPanel();
        greeting.setPreferredSize(new Dimension(500, 75));
        greeting.setOpaque(true);
        greeting.setLayout(new GridBagLayout());
        
        // JLabel for welcome greeting
        JLabel welcome = new JLabel("Welcome to Java HotSpot "
                                  + "-- DeadLock Detection");
        welcome.setFont(welcome.getFont().deriveFont(20.2f));
        welcome.setForeground(Color.DARK_GRAY);
        greeting.add(welcome);
        
        // JPanel 1 (WEST <<)
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(250, 250));
        panel1.setOpaque(true);
        panel1.setBorder(BorderFactory.createEmptyBorder());
        
        // JPanel 3 (combined with panel 1 to separate buttons)
        JPanel panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(250, 250));
        panel3.setOpaque(true);
        panel3.setBorder(BorderFactory.createEmptyBorder());
        
        // JPanel 2 (EAST >>)
        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(250, 250));
        panel2.setOpaque(true);
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
    public void actionPerformed(ActionEvent ae) {
        // if the button is load a file...
        if (ae.getActionCommand().equals("Load a file...")) {
            // creates file chooser that has a filter (only allows .java files)
            JFileChooser file = new JFileChooser();
            file.setFileFilter(new JavaFileFilter());
            int returnVal = file.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                f = file.getSelectedFile();
                if (f.getName().length() > 5) {
                    status.setText("<HTML><U>Java File Status</U>: <br>" 
                               + f.getName() + " has been loaded</HTML>");
                    dlStatus.setText("<html><U>DeadLock Detection:</U> "
                            + "<font color=green>Ready!</font></html>");
                    deadlock.setToolTipText("File loaded! Go ahead and use!");
                    deadlock.setEnabled(true);
                }
                else {
                    status.setText("<html><U>Java File Status</U>: " 
                               + f.getName() + " has been loaded</html>");
                    dlStatus.setText("<html><U>DeadLock Detection</U>: "
                            + "<font color=green>Ready!</font></html>");
                    deadlock.setToolTipText("File loaded! Go ahead and use!");
                    deadlock.setEnabled(true);
                }
            }
            else {
                status.setText("<html><U>Java File Status</U>: Cancelled</html>");
                dlStatus.setText("<html><U>DeadLock Detection</U>: "
                            + "File Cancelled</html>");
                deadlock.setToolTipText("Load file first!");
                deadlock.setEnabled(false);
            }
        }
    }
    
    // prepares to run the main program (gui)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RoughDraft();
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
