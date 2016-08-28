package main;

import utilities.CodeChecker;

import java.io.IOException;

/**
 * Created by dean on 27/08/16.
 */
public class Main {

    public static void main(String[] args) {
        String filePathName = "files/HelloWorld.txt";
        CodeChecker codeChecker = new CodeChecker(filePathName);

        try {
            codeChecker.checkFile();
            boolean isCodeCorrect = codeChecker.isCodeCorrect();
            System.out.println(isCodeCorrect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
