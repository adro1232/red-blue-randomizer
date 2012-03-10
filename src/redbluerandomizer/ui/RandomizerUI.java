package redbluerandomizer.ui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import redbluerandomizer.RedBlueRandomizer;

public class RandomizerUI {

	private JFrame frmRedblueRandomizer;
	
	private JCheckBox introCheckBox;	
	private JCheckBox rivalStarterCheckBox;	
	private JCheckBox wildCheckBox;	
	private JCheckBox trainerCheckBox;
	private JCheckBox gymLeaderCheckBox;
	private JButton randomizeButton;
	private JRadioButton totallyRandom;
	private JRadioButton oneToOneReplacement;
	
	
	private RedBlueRandomizer randomizer;
	private String inputFilePath;
	private String inputFileDirectory;
	private final ButtonGroup buttonGroup = new ButtonGroup();	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RandomizerUI window = new RandomizerUI();
					window.frmRedblueRandomizer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RandomizerUI() {
		randomizer = new RedBlueRandomizer();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRedblueRandomizer = new JFrame();
		frmRedblueRandomizer.setTitle("Red/Blue Randomizer");
		frmRedblueRandomizer.getContentPane().setEnabled(false);
		frmRedblueRandomizer.setBounds(100, 100, 322, 458);
		frmRedblueRandomizer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmRedblueRandomizer.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem menuItem = new JMenuItem("Open");
		menuItem.addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseReleased(MouseEvent e) {
				JFileChooser choose = new JFileChooser();
				choose.setFileFilter(new GameboyFileFilter());
				if(inputFileDirectory != null){
					choose.setCurrentDirectory(new File(inputFileDirectory));
				}
				choose.showOpenDialog(null);
				if(choose.getSelectedFile() != null){
					try{
						File rom = choose.getSelectedFile();
						inputFilePath = rom.getAbsolutePath();
						inputFileDirectory = rom.getParentFile().getAbsolutePath();
						randomizer.readRom(inputFilePath);
						if(!randomizer.isPokemonRedBlue()){
							JOptionPane.showMessageDialog(null, "Warning: This doesn't look like a Pokemon Red or Blue ROM...");
						}						
						randomizeButton.setEnabled(true);
					}
					catch(Exception ex){
						JOptionPane.showMessageDialog(null, "There was an error opening the file.");
					}
				}							
			}
		});
		mnFile.add(menuItem);
		frmRedblueRandomizer.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setToolTipText("T\n");
		panel.setBorder(new TitledBorder(null, "Randomize", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmRedblueRandomizer.getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		introCheckBox = new JCheckBox("Title Screen Pokemon");
		panel.add(introCheckBox);
		
		rivalStarterCheckBox = new JCheckBox("Rival Starter Pokemon");
		panel.add(rivalStarterCheckBox);
		
		wildCheckBox = new JCheckBox("Wild Pokemon");
		panel.add(wildCheckBox);
		
		trainerCheckBox = new JCheckBox("Trainer Pokemon");
		panel.add(trainerCheckBox);
		
		gymLeaderCheckBox = new JCheckBox("Gym Leader Pokemon");
		panel.add(gymLeaderCheckBox);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Randomization Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmRedblueRandomizer.getContentPane().add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		totallyRandom = new JRadioButton("Totally Random");
		totallyRandom.setSelected(true);
		panel_1.add(totallyRandom);
		buttonGroup.add(totallyRandom);
		
		oneToOneReplacement = new JRadioButton("1-1 Replacement");
		panel_1.add(oneToOneReplacement);
		buttonGroup.add(oneToOneReplacement);
		
		randomizeButton = new JButton("Randomize!");
		randomizeButton.setEnabled(false);
		randomizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File(inputFileDirectory));
					chooser.setFileFilter(new GameboyFileFilter());
					chooser.showSaveDialog(null);				
					File outputFile = chooser.getSelectedFile();
					if(outputFile != null){
						//set options
						randomizer.setIntroToggle(introCheckBox.isSelected());
						randomizer.setStartersToggle(rivalStarterCheckBox.isSelected());
						randomizer.setwildAreasToggle(wildCheckBox.isSelected());
						randomizer.setTrainersToggle(trainerCheckBox.isSelected());
						randomizer.setGymLeadersToggle(gymLeaderCheckBox.isSelected());
						randomizer.setOneToOneToggle(oneToOneReplacement.isSelected());
						
						//randomize and save
						randomizer.randomize();
						String outputFilePath = outputFile.getAbsolutePath();
						if(!outputFilePath.matches(".*\\.gb")){
							outputFilePath += ".gb";
						}
						randomizer.saveRom(outputFilePath);
						JOptionPane.showMessageDialog(null, "ROM has been randomized! :D");
					}
					else{
						return;
					}					
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, "An error occurred during randomization. Please check your rom and try again.");
				}
			}
		});
		frmRedblueRandomizer.getContentPane().add(randomizeButton);		
	}
}
