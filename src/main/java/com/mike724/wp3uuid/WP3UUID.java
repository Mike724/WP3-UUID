package com.mike724.wp3uuid;

import com.evilmidget38.UUIDFetcher;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class WP3UUID {

    public static void main(String[] args) {
        WP3UUID app = new WP3UUID();
        app.doWork();
    }

    public WP3UUID() {

    }

    public void doWork() {
        this.printLine("Welcome to the WorldPos 3.x => 3.7 converter (UUID)!");
        this.printLine("Written by Mike724, with thanks to evilmidget38 for his UUIDFetcher!");

        this.printLine("");

        //Get working directory and print
        File workingDir = new File("");
        this.printLine("Working directory: " + workingDir.getAbsolutePath());
        this.printLine("");


        //Ensure players folder exists in working directory
        File playersFolder = new File(workingDir.getAbsolutePath(), "players");

        if(!playersFolder.exists()) {
            this.printLine("Could not find \"players\" folder in working directory.");
            this.printLine("Please go back and read the instructions on the project page.");
            this.exit();
        } else {
            this.printLine("Found players data folder at: "+playersFolder.getAbsolutePath());
        }

        this.printLine("");

        this.printLine("Reading all player names into memory...");

        //Start reading in file (player) names, save into array list.
        ArrayList<String> names = new ArrayList<String>();
        File[] files = playersFolder.listFiles();
        for(File file : files) {
            names.add(file.getName().replaceAll(".txt", ""));
        }

        this.printLine("Converting all names to their respective UUID...");

        //GET ALL THE UUIDs!
        UUIDFetcher fetcher = new UUIDFetcher(names);
        Map<String, UUID> response = null;
        try {
            response = fetcher.call();
        } catch (Exception e) {
            this.printLine("");
            this.printLine("Failed to fetch UUIDs from Mojang's servers.");
            this.printLine("Are you sure you are connected to the internet?");
            this.exit();
        }

        this.printLine(response.toString());

        //Create uuid folder
        File uuidFolder = new File(playersFolder, "uuid");
        uuidFolder.mkdir();

        //Create old folder
        File oldFolder = new File(playersFolder, "old");
        oldFolder.mkdir();

        this.printLine("");
        this.printLine("Creating new player data files...");

        //Loop through all names and move their player file and rename
        for(String name : response.keySet()) {
            try {
                String nameTxt = name + ".txt";
                String uuidTxt = response.get(name) + ".txt";
                File pdata = new File(playersFolder, nameTxt);
                Files.copy(pdata, new File(uuidFolder, uuidTxt));
                Files.move(pdata, new File(oldFolder, nameTxt));
            } catch (IOException e) {
                e.printStackTrace();
                this.printLine("Error (IOException) while trying to manipulate files...");
                this.exit();
            }
        }

        this.printLine("Done! Enjoy :)");
        this.exit();

    }

    private void printLine(String line) {
        System.out.println(line);
    }

    private void exit() {
        this.printLine("");
        this.printLine("Please press the ENTER key to exit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

}