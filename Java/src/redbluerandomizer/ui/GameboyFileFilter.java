package redbluerandomizer.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GameboyFileFilter extends FileFilter {

	private String description = ".gb ROM files";
	private String regex = ".*\\.gb$";
	
    public boolean accept(File file) {
      if(file.isDirectory()){
    	  return true;
      }
      
      String fileName = file.getName().toLowerCase();
      if(fileName.matches(regex)){
    	  return true;
      }
      else{
    	  return false;
      }
    }

	@Override
	public String getDescription() {		
		return description;
	}
    
}