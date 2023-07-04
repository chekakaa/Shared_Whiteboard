package CreateWhiteBoard;

import rmiRemote.IRemoteWhiteBoard;

import java.awt.BasicStroke;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.awt.Stroke;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.imageio.ImageIO;

import java.awt.Color;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JList;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:25
 */

// the function of GUI of the manager
public class managerUI extends JFrame implements ActionListener{

    private JPanel frontPanel;
    private JTextField textField;
    private sketchpad panel;
    private JFileChooser fileChooser;
    private File file;
    private String filePath;
    private JList list;
    private JTextArea textArea;
    private String username;
    private IRemoteWhiteBoard whiteBoard;
    private String suffix = ".png";



    public sketchpad getpanel() {
        return panel;
    }

    public JList getJlist() {
        return list;
    }

    public JTextArea gettextArea() {
        return textArea;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void createWhiteBoard(IRemoteWhiteBoard whiteBoard) {
        this.whiteBoard = whiteBoard;
    }

    public managerUI() {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch(Exception e) {
            System.out.println("Error setting Java LAF: " + e);
        }
        panel = new sketchpad();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int flag = JOptionPane.showConfirmDialog(null,"save or not?","INFO", JOptionPane.YES_NO_OPTION);
                if(flag == 0) {
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.setCurrentDirectory(new File("."));
                        file = fileChooser.getSelectedFile();
                        File fileCreated;
                        try {
                            if (file.getAbsolutePath().toUpperCase().endsWith(suffix.toUpperCase())) {
                                fileCreated = file;
                                filePath = file.getAbsolutePath();
                            } else {
                                fileCreated = new File(file.getAbsolutePath() + suffix);
                                filePath = file.getAbsolutePath() + suffix;
                            }
                            suffix = suffix.substring(1);
                            ImageIO.write(panel.save(), suffix, fileCreated);
                            JOptionPane.showMessageDialog(null, "save success", "Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                try {
                    whiteBoard.end();
                } catch (RemoteException e1) {
                    System.out.println("exit");
                    e1.printStackTrace();
                }
                e.getWindow().dispose();
            }
        });
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(fileChooser.getFileFilter());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(95, 95, 911, 575);

        //File menu of new, open, save, saveas and close
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Arial", Font.PLAIN, 16));
        menuBar.add(fileMenu);

        //new menu
        JMenuItem newMenuBar = new JMenuItem("New");
        fileMenu.add(newMenuBar);
        newMenuBar.setFont(new Font("Arial", Font.PLAIN, 16));
        newMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int flag = JOptionPane.showConfirmDialog(null,"save or not?","INFO", JOptionPane.YES_NO_OPTION);
                if(flag == 0) {
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.setCurrentDirectory(new File("."));
                        file = fileChooser.getSelectedFile();
                        File fileCreated;
                        try {
                            if (file.getAbsolutePath().toUpperCase().endsWith(suffix.toUpperCase())) {
                                fileCreated = file;
                                filePath = file.getAbsolutePath();
                            } else {
                                fileCreated = new File(file.getAbsolutePath() + suffix);
                                filePath = file.getAbsolutePath() + suffix;
                            }
                            suffix = suffix.substring(1);
                            ImageIO.write(panel.save(), suffix, fileCreated);
                            JOptionPane.showMessageDialog(null, "save success", "Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                filePath = null;
                panel.clear();
                panel.repaint();
                panel.synchronize();
            }
        });

        //open menu
        JMenuItem openMenuBar = new JMenuItem("Open");
        fileMenu.add(openMenuBar);
        openMenuBar.setFont(new Font("Arial", Font.PLAIN, 16));
        openMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int judgement = JOptionPane.showConfirmDialog(null,"save or not?","INFO", JOptionPane.YES_NO_OPTION);
                if(judgement == 0) {
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.setCurrentDirectory(new File("."));
                        file = fileChooser.getSelectedFile();
                        File fileCreated;
                        try {
                            if (file.getAbsolutePath().toUpperCase().endsWith(suffix.toUpperCase())) {
                                fileCreated = file;
                                filePath = file.getAbsolutePath();
                            } else {
                                fileCreated = new File(file.getAbsolutePath() + suffix);
                                filePath = file.getAbsolutePath() + suffix;
                            }
                            suffix = suffix.substring(1);
                            ImageIO.write(panel.save(), suffix, fileCreated);
                            JOptionPane.showMessageDialog(null, "save success", "Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileChooser.setCurrentDirectory(new File("."));
                    filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    file=new File(filePath);
                    try {
                        BufferedImage bufferedImage = ImageIO.read(file);
                        panel.load(bufferedImage);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    panel.synchronize();
                }
            }
        });

        //save menu
        JMenuItem saveMenuBar = new JMenuItem("Save");
        fileMenu.add(saveMenuBar);
        saveMenuBar.setFont(new Font("Arial", Font.PLAIN, 16));
        saveMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (filePath == null) {
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.setCurrentDirectory(new File("."));
                        file = fileChooser.getSelectedFile();
                        File fileCreated;
                        try {
                            if (file.getAbsolutePath().toUpperCase().endsWith(suffix.toUpperCase())) {
                                fileCreated = file;
                                filePath = file.getAbsolutePath();
                            } else {
                                fileCreated = new File(file.getAbsolutePath() + suffix);
                                filePath = file.getAbsolutePath() + suffix;
                            }
                            suffix = suffix.substring(1);
                            ImageIO.write(panel.save(), suffix, fileCreated);
                            JOptionPane.showMessageDialog(null, "save success", "Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    return;
                }
                else {
                    file = new File(filePath);
                }
                try {
                    String[] route = filePath.split("\\.");
                    ImageIO.write(panel.save(), route[route.length - 1],file);
                    JOptionPane.showMessageDialog(null, "save success", "Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //save as menu
        JMenuItem saveAsMenuBar = new JMenuItem("Save As");
        fileMenu.add(saveAsMenuBar);
        saveAsMenuBar.setFont(new Font("Arial", Font.PLAIN, 16));
        saveAsMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileChooser.setCurrentDirectory(new File("."));
                    file = fileChooser.getSelectedFile();
                    File fileCreated;
                    try {
                        if (file.getAbsolutePath().toUpperCase().endsWith(suffix.toUpperCase())) {
                            fileCreated = file;
                            filePath = file.getAbsolutePath();
                        } else {
                            fileCreated = new File(file.getAbsolutePath() + suffix);
                            filePath = file.getAbsolutePath() + suffix;
                        }
                        suffix = suffix.substring(1);
                        ImageIO.write(panel.save(), suffix, fileCreated);
                        JOptionPane.showMessageDialog(null, "save success", "Information", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        JSeparator separator = new JSeparator();
        fileMenu.add(separator);

        //close menu
        JMenuItem closeMenuBar = new JMenuItem("Close");
        closeMenuBar.setFont(new Font("Arial", Font.PLAIN, 16));
        fileMenu.add(closeMenuBar);
        closeMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int flag = JOptionPane.showConfirmDialog(null,"save or not?","INFO", JOptionPane.YES_NO_OPTION);
                if(flag == 0) {
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.setCurrentDirectory(new File("."));
                        file = fileChooser.getSelectedFile();
                        File fileCreated;
                        try {
                            if (file.getAbsolutePath().toUpperCase().endsWith(suffix.toUpperCase())) {
                                fileCreated = file;
                                filePath = file.getAbsolutePath();
                            } else {
                                fileCreated = new File(file.getAbsolutePath() + suffix);
                                filePath = file.getAbsolutePath() + suffix;
                            }
                            suffix = suffix.substring(1);
                            ImageIO.write(panel.save(), suffix, fileCreated);
                            JOptionPane.showMessageDialog(null, "save success", "Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                try {
                    whiteBoard.end();
                } catch (RemoteException e1) {
                    System.out.println("exit");
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        frontPanel = new JPanel();
        frontPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(frontPanel);
        frontPanel.setLayout(null);

        JLabel commandShape = new JLabel("Shape:");
        commandShape.setFont(new Font("Arial", Font.BOLD, 16));
        commandShape.setBounds(10, 0, 66, 25);
        frontPanel.add(commandShape);

        JLabel commandTools = new JLabel("Tools:");
        commandTools.setFont(new Font("Arial", Font.BOLD, 16));
        commandTools.setBounds(431, 0, 66, 25);
        frontPanel.add(commandTools);

        JToolBar shapeChooser = new JToolBar();
        shapeChooser.setFloatable(false);
        shapeChooser.setBounds(0, 28, 340, 41);
        frontPanel.add(shapeChooser);

        JToolBar toolsChooser = new JToolBar();
        toolsChooser.setFloatable(false);
        toolsChooser.setBounds(421, 28, 235, 41);
        frontPanel.add(toolsChooser);

        JButton lineButton = new JButton("Line");
        lineButton.setFont(new Font("Arial", Font.PLAIN, 18));
        shapeChooser.add(lineButton);

        JButton circleButton = new JButton("Circle");
        circleButton.setFont(new Font("Arial", Font.PLAIN, 18));
        shapeChooser.add(circleButton);

        JButton ovalButton = new JButton(" Oval ");
        ovalButton.setFont(new Font("Arial", Font.PLAIN, 18));
        shapeChooser.add(ovalButton);

        JButton triangleButton = new JButton("Triangle");
        triangleButton.setFont(new Font("Arial", Font.PLAIN, 18));
        shapeChooser.add(triangleButton);

        JButton rectangleButton = new JButton("Rectangle");
        rectangleButton.setFont(new Font("Arial", Font.PLAIN, 18));
        shapeChooser.add(rectangleButton);


        JButton pencilButton = new JButton(" Pencil ");
        pencilButton.setFont(new Font("Arial", Font.PLAIN, 18));
        toolsChooser.add(pencilButton);

        JButton textButton = new JButton("  Text  ");
        textButton.setFont(new Font("Arial", Font.PLAIN, 18));
        toolsChooser.add(textButton);

        JButton eraserButton = new JButton("  Eraser  ");
        eraserButton.setFont(new Font("Arial", Font.PLAIN, 18));
        toolsChooser.add(eraserButton);

        //button_listener
        lineButton.addActionListener(this);
        circleButton.addActionListener(this);
        ovalButton.addActionListener(this);
        triangleButton.addActionListener(this);
        rectangleButton.addActionListener(this);
        pencilButton.addActionListener(this);
        textButton.addActionListener(this);
        eraserButton.addActionListener(this);


        JButton selectedButton = new JButton("");
        selectedButton.setEnabled(false);
        selectedButton.setBackground(Color.BLACK);
        selectedButton.setBounds(121, 87, 22, 22);
        selectedButton.setOpaque(true);
        selectedButton.setBorderPainted(false);
        frontPanel.add(selectedButton);

        JLabel selectedLabel = new JLabel("Selected Colour:");
        selectedLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        selectedLabel.setBounds(10, 79, 120, 16);
        frontPanel.add(selectedLabel);

        //colour area
        JButton blueButton = new JButton("");
        blueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.BLUE);
                panel.setColor(Color.BLUE);
            }
        });
        blueButton.setBounds(218, 73, 22, 22);
        blueButton.setBackground(Color.BLUE);
        blueButton.setOpaque(true);
        blueButton.setBorderPainted(false);
        frontPanel.add(blueButton);

        JButton greenButton = new JButton("");
        greenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.GREEN);
                panel.setColor(Color.GREEN);
            }
        });
        greenButton.setBounds(330, 73, 22, 22);
        frontPanel.add(greenButton);
        greenButton.setBackground(Color.GREEN);
        greenButton.setOpaque(true);
        greenButton.setBorderPainted(false);

        JButton cyanButton = new JButton("");
        cyanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.CYAN);
                panel.setColor(Color.CYAN);
            }
        });
        cyanButton.setBounds(330, 101, 22, 22);
        frontPanel.add(cyanButton);
        cyanButton.setBackground(Color.CYAN);
        cyanButton.setOpaque(true);
        cyanButton.setBorderPainted(false);

        JButton magentaButton = new JButton("");
        magentaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.MAGENTA);
                panel.setColor(Color.MAGENTA);
            }
        });
        magentaButton.setBounds(274, 73, 22, 22);
        frontPanel.add(magentaButton);
        magentaButton.setBackground(Color.MAGENTA);
        magentaButton.setOpaque(true);
        magentaButton.setBorderPainted(false);

        JButton chooseColorButton = new JButton("Others");
        chooseColorButton.setFont(new Font("Arial", Font.PLAIN, 16));
        chooseColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color initialColor = null;
                Color selectedColor = JColorChooser.showDialog(null, "Choose a color", initialColor);
                System.out.println("");
                selectedButton.setBackground(selectedColor);
                chooseColorButton.setBackground(selectedColor);
                panel.setColor(selectedColor);
            }
        });
        chooseColorButton.setBounds(358, 85, 84, 28);
        frontPanel.add(chooseColorButton);


        JButton yellowButton = new JButton("");
        yellowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.YELLOW);
                panel.setColor(Color.YELLOW);

            }
        });
        yellowButton.setBounds(302, 73, 22, 22);
        yellowButton.setBackground(Color.YELLOW);
        yellowButton.setOpaque(true);
        yellowButton.setBorderPainted(false);
        frontPanel.add(yellowButton);

        JButton orangeButton = new JButton("");
        orangeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.ORANGE);
                panel.setColor(Color.ORANGE);
            }
        });
        orangeButton.setBounds(246, 73, 22, 22);
        frontPanel.add(orangeButton);
        orangeButton.setBackground(Color.ORANGE);
        orangeButton.setOpaque(true);
        orangeButton.setBorderPainted(false);

        JButton redButton = new JButton("");
        redButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.RED);
                panel.setColor(Color.RED);
            }
        });
        redButton.setBounds(246, 101, 22, 22);
        frontPanel.add(redButton);
        redButton.setBackground(Color.RED);
        redButton.setOpaque(true);
        redButton.setBorderPainted(false);

        JButton whiteButton = new JButton("");
        whiteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.WHITE);
                panel.setColor(Color.WHITE);
            }
        });
        whiteButton.setOpaque(true);
        whiteButton.setBorderPainted(false);
        whiteButton.setBackground(Color.WHITE);
        whiteButton.setBounds(274, 101, 22, 22);
        frontPanel.add(whiteButton);

        JButton blackButton = new JButton("");
        blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.BLACK);
                panel.setColor(Color.BLACK);
            }
        });
        blackButton.setBounds(218, 101, 22, 22);
        frontPanel.add(blackButton);
        blackButton.setBackground(Color.BLACK);
        blackButton.setOpaque(true);
        blackButton.setBorderPainted(false);

        JButton grayButton = new JButton("");
        grayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                selectedButton.setBackground(Color.GRAY);
                panel.setColor(Color.GRAY);

            }
        });
        grayButton.setBounds(302, 101, 22, 22);
        grayButton.setBackground(Color.GRAY);
        grayButton.setOpaque(true);
        grayButton.setBorderPainted(false);
        frontPanel.add(grayButton);

        //text help of colour
        JLabel paletteLabel = new JLabel("Palette:");
        paletteLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        paletteLabel.setBounds(151, 79, 73, 16);
        frontPanel.add(paletteLabel);

        //Size option
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sizeLabel.setBounds(480, 79, 42, 16);
        frontPanel.add(sizeLabel);

        JComboBox sizeComboBox = new JComboBox();
        sizeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        sizeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Small", "Medium", "Large"}));
        sizeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String size = (String)sizeComboBox.getSelectedItem();
                Stroke selectStroke = new BasicStroke(1.0f);
                if(size.contentEquals("Small")) {
                    selectStroke = new BasicStroke(1.0f);
                }
                else if(size.contentEquals("Medium")) {
                    selectStroke = new BasicStroke(2.0f);
                }
                else if(size.contentEquals("Large")) {
                    selectStroke = new BasicStroke(3.0f);
                }
                panel.setStroke(selectStroke);
            }
        });
        sizeComboBox.setBounds(532, 85, 108, 31);
        frontPanel.add(sizeComboBox);

        // whiteboard area
        panel.setBounds(10, 129, 646, 394);
        panel.setBackground(Color.white);
        frontPanel.add(panel);
        JPanel panelOutline = new JPanel();
        panelOutline.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelOutline.setBounds(8, 127, 650, 396);
        frontPanel.add(panelOutline);


        eraserButton.addActionListener(this);

        //users, chatbox and kickout area
        JLabel usersLabel = new JLabel("Users:");
        usersLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usersLabel.setBounds(672, 0, 66, 25);
        frontPanel.add(usersLabel);

        JLabel chatLabel = new JLabel("ChatBox:");
        chatLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chatLabel.setBounds(672, 186, 96, 25);
        frontPanel.add(chatLabel);

        JButton KickOut = new JButton("Kick Out");
        KickOut.setFont(new Font("Arial", Font.PLAIN, 16));
        KickOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog( "enter the username" );
                try {
                    whiteBoard.kickOutClient(input);
                } catch (RemoteException e1) {
                    System.out.println("err in GUI");
                    e1.printStackTrace();
                }
            }
        });
        KickOut.setBounds(758, 0, 96, 25);
        frontPanel.add(KickOut);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBounds(642, 212, 183, 237);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(668, 209, 207, 237);
        frontPanel.add(scroll);


        textField = new JTextField();
        textField.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));
        textField.setBounds(668, 458, 130, 26);
        frontPanel.add(textField);
        textField.setColumns(10);

        // to send message to others
        JButton sendButton = new JButton("Send");
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String message = textField.getText();
                try {
                    if(!message.equals("")) {
                        whiteBoard.broadcast("[" + username +"]" + ": " + message);
                    }
                } catch (RemoteException e1) {
                    JOptionPane.showMessageDialog(null, "the manager has left the room", "error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                    System.exit(0);
                }
                textField.setText(null);;
            }
        });
        sendButton.setBounds(805, 458, 80, 29);
        sendButton.setFont(new Font("Arial", Font.PLAIN, 16));
        frontPanel.add(sendButton);

        list = new JList();
        list.setBounds(668, 34, 207, 151);
        list.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));
        frontPanel.add(list);



        this.setTitle("Manager");
        setVisible(true);

    }

    @Override
    // to get the type of different command and shows person who is using the whiteboard
    public void actionPerformed(ActionEvent e) {
        String shapeCommand = e.getActionCommand();
        if(shapeCommand.contentEquals("Line")) {
            panel.setType("line");
        }
        else if(shapeCommand.contentEquals("Rectangle")) {
            panel.setType("rect");
        }
        else if(shapeCommand.contentEquals("Triangle")) {
            panel.setType("tri");
        }
        else if(shapeCommand.contentEquals("Circle")) {
            panel.setType("circle");
        }
        else if(shapeCommand.contentEquals(" Pencil ")) {
            panel.setType("pencil");
        }
        else if(shapeCommand.contentEquals(" Oval ")) {
            panel.setType("oval");
        }
        else if(shapeCommand.contentEquals("  Eraser  ")) {
            panel.setType("erase");
        }
        else if(shapeCommand.contentEquals("  Text  ")) {
            panel.setType("text");
        }
        else if(shapeCommand.contentEquals("Exit")) {
            System.exit(0);

        }
        try {
            whiteBoard.broadcast(username + " is using the whiteboard.");
        }catch (RemoteException e1) {
            JOptionPane.showMessageDialog(null, "the manager has left the room", "error", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
            System.exit(0);
        }

    }
}

