/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package DirectoryBackup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author amanraj
 */
public class MainBackground {

	// This is background job.
    
    public static void writeBackupDirectory(String absPath) {
        try {
	    File file = new File(System.getProperty("user.home")+File.separator+"DirectoryBackup.data");
	    FileWriter fWriter;
            if (!file.exists()) {
                file.createNewFile();
            }
            if (!fileNameExist(absPath)) {
                  fWriter = new FileWriter(file, true);
                  fWriter.write(absPath + "\n");
                  DirctoryBackup.jTextArea2.append("Your backup repository "+absPath+" is active \n Your all backup data will be stored here !");
                  fWriter.close();
            }
        } catch (IOException ex) {
        }

    }

    public static boolean fileNameExist(String filePath) {        // check if file name already exist in main.txt file
        boolean found = false;
        try {
		File file = new File(System.getProperty("user.home")+File.separator+"DirectoryBackup.data");
            	BufferedReader bReader = new BufferedReader(new FileReader(file));
            	String line = bReader.readLine();
            	while (!found && line != null) {
                	if ((new File(line).getName()).equals(new File(filePath).getName())) {
                    		found = true;
                	}
                	line = bReader.readLine();
            	}

        } catch (IOException ex) {
        }
        return found;
    }

    public static void getSourceFile(String path) {

        File sourceFile = new File(path);
	ReadWriteText wrtxt=null;
        try {
	    File file = new File(System.getProperty("user.home")+File.separator+"DirectoryBackup.data");
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            String mainDirPath = bReader.readLine();     // path of directory where backup is being kept
            FileWriter fWriter = new FileWriter(file, true);

            if (!fileNameExist("f-" + path)) {
                fWriter.write("f-" + path + "\n");
                fWriter.close();
            }
            if (!new File(mainDirPath + File.separator + sourceFile.getName()).exists()) {
                wrtxt = new ReadWriteText(path, mainDirPath + File.separator + sourceFile.getName());
                DirctoryBackup.jTextArea2.append("Backup for " + sourceFile + " file has been created in " + mainDirPath);
            } else {
                JOptionPane.showMessageDialog(null, "Sorry ! A file with this name already exists in " + mainDirPath + ".\n Please change the name of the file and then proceed.", null, JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
        }
    }

    public static void getSourceFolder(String sourceDirPath) {
        File sourceDir = new File(sourceDirPath);
        try {
	    File file = new File(System.getProperty("user.home")+File.separator+"DirectoryBackup.data");
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            String mainDirPath = bReader.readLine();     // path of directory where backup is being kept
            FileWriter fWriter = new FileWriter(file, true);
            if (!fileNameExist("d-" + sourceDirPath)) // to write directory path in main.txt if not present
            {
                fWriter.write("d-" + sourceDirPath + "\n");
                fWriter.close();
            }

            File folder = new File(mainDirPath + File.separator + sourceDir.getName());    // backup dir + this directory
            if (!folder.exists()) {
                folder.mkdir();
                writeEachDir(sourceDir, folder);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Sorry ! A Directory with this name already exists in " + mainDirPath + ".\n Please change the name of the Directory and then proceed.", null, JOptionPane.ERROR_MESSAGE);
            }
            

        } catch (IOException ex) {}

    }

    private static void writeEachDir(File file, File dest) {
	
	ReadWriteText wrtxt=null;
        String flist[] = file.list();
        int size = flist.length;
        File tempSourceFile, tempDestFile;
        for (int i = 0; i < size; i++) {
            tempSourceFile = new File(file + File.separator + flist[i]);
            tempDestFile = new File(dest + File.separator + flist[i]);
            if (tempSourceFile.isDirectory() && !tempDestFile.exists()) {
                tempDestFile.mkdir();
                writeEachDir(tempSourceFile, tempDestFile);

            } else {
                wrtxt = new ReadWriteText(tempSourceFile.getAbsolutePath(), dest.getAbsolutePath() + File.separator + flist[i]);
                DirctoryBackup.jTextArea2.append("Backup for " + tempSourceFile + " file has been created in backup directory\n");
            }
        }

    }

    public static void update() {
        try {
	    File file = new File(System.getProperty("user.home")+File.separator+"DirectoryBackup.data");
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            String backupDir = bReader.readLine();
            File storeHouseDir = new File(backupDir);      // "storeHouseDir" really?? well, it's 3 in the morning and i coudn't come up with any other name
            String backupFiles[] = storeHouseDir.list();
	    ReadWriteText wrtxt=null;
            int nOfFiles = backupFiles.length;                    // number of files in backup directory
            boolean found;
            File someFile;                  // I m out of names now
            String nextLine = bReader.readLine();
            int i;
            while (nextLine != null) {
                String content[] = nextLine.split("-");
                File contentFile = new File(content[1]);
                i = 0;
                found = false;
                switch (content[0]) {
                    case "f":
                        while (found == false && i < nOfFiles) {                                // 
                            someFile = new File(backupDir + File.separator + backupFiles[i]);
                            if (contentFile.getName().equals(backupFiles[i])) {
                                long sourceModificationDate = contentFile.lastModified();
                                long destModificationDate = someFile.lastModified();
                                if (sourceModificationDate > destModificationDate) // update when modification date of source file is greater than modification date of destination file
                                {
                                    wrtxt = new ReadWriteText(content[1], backupDir + File.separator + backupFiles[i]);
                                    DirctoryBackup.jTextArea2.append("File " + content[1] + " has been successfully updated\n");
                                }
                                found = true;
                            }
                            i++;
                        }
                        break;

                    case "d":

                        while (found == false && i < nOfFiles) {                                // 
                            someFile = new File(backupDir + File.separator + backupFiles[i]);
                            if (contentFile.getName().equals(backupFiles[i])) {
                                updateDirectory(contentFile, someFile);
                                found = true;
                            }
                            i++;
                        }
                        break;

                }
                nextLine = bReader.readLine();
            }
        } catch (Exception ex) {
        }
    }

    private static void updateDirectory(File source, File dest) {
	ReadWriteText wrtxt=null;
        String flist[] = source.list();
        int size = flist.length;
        File tempSourceFile, tempDestFile;
        for (int i = 0; i < size; i++) {
            tempSourceFile = new File(source + File.separator + flist[i]);
            tempDestFile = new File(dest + File.separator + flist[i]);
            long sourcemodifiedDate = tempSourceFile.lastModified();
            long destmodifiedDate = tempDestFile.lastModified();
            if (tempSourceFile.isDirectory() && tempDestFile.exists()) {
                updateDirectory(tempSourceFile, tempDestFile);
            }
            else if(tempSourceFile.isDirectory() && !tempDestFile.exists())               // if new directory has been created in source directory and is not present in destination
            {                                                                             // in that case create that directory
                tempDestFile.mkdir();
                writeEachDir(tempSourceFile,tempDestFile);
            }
            else if (sourcemodifiedDate > destmodifiedDate) {
                wrtxt = new ReadWriteText(tempSourceFile.getAbsolutePath(), dest.getAbsolutePath() + File.separator + flist[i]);
                DirctoryBackup.jTextArea2.append("File " + tempSourceFile + " has been successfully updated\n");

            }
        }

    }
}

//    boolean needUpdation(String sourceFile)
//    {
//        boolean flag=false;
//        
//        return flag;
//    }
//                Runtime rt = Runtime.getRuntime();
//                //rt.exec(new String[]{"cmd.exe","/c","start"});
//                rt.exec("cmd.exe /c start copy e:\\test\\BubbleSort.java e:\\");

