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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class RandomizerUI {

	private JFrame frmRedblueRandomizer;
	
	private JCheckBox titleScreenCheckBox;	
	private JCheckBox wildCheckBox;	
	private JCheckBox trainerCheckBox;
	private JButton randomizeButton;
	private JRadioButton totallyRandom;
	private JRadioButton oneToOneReplacement;
	private JCheckBox noLegendariesCheckBox;
	
	
	private RedBlueRandomizer randomizer;
	private String inputFilePath;
	private String inputFileDirectory;
	private final ButtonGroup buttonGroup = new ButtonGroup();	
	private JCheckBox starterCheckBox;
	
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
		frmRedblueRandomizer.setResizable(false);
		frmRedblueRandomizer.setTitle("Red/Blue Randomizer");
		frmRedblueRandomizer.getContentPane().setEnabled(false);
		frmRedblueRandomizer.setBounds(100, 100, 362, 484);
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
		
		JPanel panel = new JPanel();
		panel.setToolTipText("T\n");
		panel.setBorder(new TitledBorder(null, "Randomize", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		titleScreenCheckBox = new JCheckBox("Title Screen Pokemon");
		panel.add(titleScreenCheckBox);
		
		wildCheckBox = new JCheckBox("Wild Pokemon");
		panel.add(wildCheckBox);
		
		trainerCheckBox = new JCheckBox("Trainer Pokemon");
		panel.add(trainerCheckBox);
		
		starterCheckBox = new JCheckBox("Starter Pokemon");
		panel.add(starterCheckBox);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Randomization Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		totallyRandom = new JRadioButton("Totally Random");
		totallyRandom.setSelected(true);
		panel_1.add(totallyRandom);
		buttonGroup.add(totallyRandom);
		
		noLegendariesCheckBox = new JCheckBox("No Legendaries");
		panel_1.add(noLegendariesCheckBox);
		
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
						randomizer.setTitleScreenToggle(titleScreenCheckBox.isSelected());
						randomizer.setPlayerStartersToggle(starterCheckBox.isSelected());
						randomizer.setwildAreasToggle(wildCheckBox.isSelected());
						randomizer.setTrainersToggle(trainerCheckBox.isSelected());
						randomizer.setOneToOneToggle(oneToOneReplacement.isSelected());
						randomizer.setNoLegendariesToggle(noLegendariesCheckBox.isSelected());
						
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
					e.printStackTrace();
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(frmRedblueRandomizer.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(randomizeButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(623, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(randomizeButton, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		frmRedblueRandomizer.getContentPane().setLayout(groupLayout);
	}
}
