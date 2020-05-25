package com.sample.framework.uiautomation.Utility;

import com.sample.framework.beans.Configuration;

import java.io.*;

public class FileUtilsSerialized {

    public static Configuration readFromFile(String fileName) {

        Configuration config = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            while (true) {
                Object obj = in.readObject();
                config = (Configuration) obj;
                break;
            }
        } catch (FileNotFoundException e) {
            System.out.println(" File not found " + fileName);
        } catch (EOFException e) {
            e.printStackTrace();
            System.out.println(" EOF Exception encountered : Message : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(" ClassNotFoundException Exception encountered : Message : " + e.getMessage());
        } catch (IOException e) {
            System.out.println(" IOException encountered : Message : " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Exception encountered : Message : " + e.getMessage());
        } finally {
            return config;
        }
    }

    public static void writeToFile(Configuration config, String fileName) {

        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(config);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}