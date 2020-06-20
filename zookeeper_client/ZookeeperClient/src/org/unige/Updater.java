package org.unige;

import org.apache.curator.framework.CuratorFramework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Updater implements Runnable {
    String connection = null;
    String path = null;
    CuratorFramework client = null;

    public Updater(String connection, String path, CuratorFramework client){
        this.connection = connection;
        this.path = path;
        this.client = client;
    }

    @Override
    public void run() {
        byte[] text = null;
        try {
            text = client.getData().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File("conf.txt");

        if(!file.exists()) {
            try {
                if (file.createNewFile())
                {
                    System.out.println("File is created!");
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            assert text != null;
            Files.write(Paths.get("conf.txt"), text);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
