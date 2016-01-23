/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package DirectoryBackup;
/**
 *
 * @author raj
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ReadWriteText implements Runnable {

    FileChannel sourceChannel = null;
    FileChannel destChannel = null;
    File sourceFile, destFile;
    Thread t;

    ReadWriteText(String source, String dest) {
        this.sourceFile = new File(source);
        this.destFile = new File(dest);
        t = new Thread(this);
        t.start();

    }

    @Override
    public void run() {
        this.copyFile(sourceFile, destFile);

    }

    public void copyFile(File source, File dest) {
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "No such directory exists\n First you should  create bakup directory ", null, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
