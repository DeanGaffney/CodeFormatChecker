package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

/**
 * Created by dean on 27/08/16.
 */
public class CodeChecker {

    private String filePathName;
    private String fileContent;
    private Stack<Character> stack;
    private boolean isCodeCorrect;

    public CodeChecker(String filePathName){
        this.filePathName = filePathName;
        fileContent = null;
        stack = new Stack<Character>();
        isCodeCorrect = false;
    }

    public boolean checkFile()throws IOException{
        //method reads the file content into a string using java 8 libraries.
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePathName)));
            //TODO delete comments from original file so we dont have to deal with them.
            isCodeCorrect = isFormattedCorrectly();
        }catch(IOException e){
            e.printStackTrace();
        }

        return isCodeCorrect;
    }

    /*
        This method iterates through the file character by character
        and tests each character against regular expressions to see if the 
        character is to be pushed to the stack or popped from the stack.
     */
    private boolean isFormattedCorrectly() throws IOException {
        //go through file line by line
        List<String> lines = Files.readAllLines(Paths.get(filePathName));

        //used to check if a line is a comment
        boolean isComment = false;

        //check if comments are well formatted,if not then return false immediately.
        if(!areCommentsCorrectlyFormatted())return false;

        //go through each character on each line
        for(String line : lines) {
            for (Character character : line.toCharArray()) {

                //check for single and multi line comments,if its a comment just go break and go to next line.
                isComment = isComment(line);
                //if(isComment)break;
                //regex tests for opening brackets
                if (character.toString().matches("([\\[({])") && !isComment) {
                    System.out.println(character);
                    stack.push(character);
                }

                //regex tests for closing brackets.
                else if (character.toString().matches("([\\])}])") && !isComment) {
                    if (!stack.isEmpty() && isMatchingBracket(character, stack.peek())) {
                        stack.pop();
                    } else return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /*
        returns true if a line is considered a comment,false otherwise.
     */
    private boolean isComment(String line){
        //matches a single line comment eg. // hello world
        if(line.matches(".*?\\/\\/.*?"))return true;

        //matches multi line comments including new line
        else if(line.matches("/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*/"))return true;
        // matches whitespace followed by asterisks for multi line comments on a single line.
        else if(line.matches("\\s*?\\*.*?"))return true;
        //matches an ending of a multi line comment on a single line.
        else if(line.matches("\\s*?\\*\\/\\s*?"))return true;
        return false;
    }

    /*
     checks to see if the comments are correctly formatted.
     */
    private boolean areCommentsCorrectlyFormatted() throws IOException {
        try {
            //go through file line by line
            List<String> lines = Files.readAllLines(Paths.get(filePathName));

            for(String line : lines){
                if(line.matches(".*?\\/\\/.*?"))return true;
                else if(line.matches("\\s*?\\*.*?"))return true;//matches an ending of a multi line comment on a single line.
                else if(line.matches("\\s*?\\*\\/\\s*?"))return true;
                else if(line.matches("\\s*?/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*/\\s*?"))return true;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    /*
        Checks to make sure that the brackets are matching brackets.
     */
    private boolean isMatchingBracket(char currentChar,char testingChar){
        if(currentChar == '}' && testingChar == '{')return true;
        if(currentChar == ')' && testingChar == '(')return true;
        if(currentChar == ']' && testingChar == '[')return true;
        else return false;
    }

    public boolean isCodeCorrect() {
        return isCodeCorrect;
    }
}
