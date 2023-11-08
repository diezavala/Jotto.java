import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


//thumbtack is final
//diamond is static
/**
 * Name: Diego Zavala
 * Date: 2/12/2023
 * Description: This is a word guessing game called Jotto
 */
public class Jotto { // in these next few lines I initialize all the fields required for the program and methods
    private static final boolean DEBUG = true;
    private static final int WORD_SIZE = 5;
    private final ArrayList<String> wordList = new ArrayList<>();
    private final ArrayList<String> playerGuesses = new ArrayList<>();
    private final ArrayList<String> playedWords = new ArrayList<>();
    private String currentWord = "";
    private String filename = "";
    private int score;

    public Jotto(String filename) { //this constructor sets the instance of the filename to the one passed in then calls the file reader
        this.filename = filename;
        readWords();
    }

    private void readWords(){
        //this method creates two objects to open and read a file into a list. These will be the words used in the game
        try{
            File file= new File(filename);
            Scanner scan = new Scanner(file);

            while(scan.hasNextLine()){ // while the file is not at the end, it adds the line to the word list
                String word = scan.nextLine();
                if(!wordList.contains(word)){
                    System.out.println("Word: "+word);
                    wordList.add(word);
                }
            }
            scan.close(); //closes the scanner object
        }catch(FileNotFoundException e){ //catches if the file could not be opened
            System.out.println("Couldn't open "+filename);
        }
    }

    public void play(){ // this method is the main game that asks the user for the different options to play
        Scanner scan = new Scanner(System.in);
        String option;
        do{ //do while loop that will keep asking the user to play until they put zz then quits the game
            System.out.println("=-=-=-=-=-=-=-=-=-=-=\n" +
                    "Choose one of the following:\n" +
                    "1:\t Start the game\n" +
                    "2:\t See the word list\n" +
                    "3:\t See the chosen words\n" +
                    "4:\t Show Player guesses\n" +
                    "zz to exit\n" +
                    "=-=-=-=-=-=-=-=-=-=-=\n" +
                    "What is your choice: \n");
            option = scan.nextLine(); //these next if statements are the conditions for the different options
            if(option.equals("Zz".toLowerCase().trim())){
                break;
            }
            if(option.equals("1") || option.toLowerCase().equals("one")){
                if(pickWord()){
                    guess();
                }else{
                    pickWord();
                }
            }else if(option.equals("2") || option.toLowerCase().equals("two")){
                showWordList();
            }else if(option.equals("3") || option.toLowerCase().equals("three")){
                showPlayedWords();
            } else if(option.equals("4") || option.toLowerCase().equals("tour")) {
                showPlayerGuesses();
            }
            else if(option.equals("ZZ".toLowerCase().trim())){
                System.out.println("Final score: "+score);
                System.out.println("Thank you for playing :)");
            } else{
                System.out.println("I don't know what " + option + " is.");
            }
            System.out.println("\nPress enter to continue\n");
            option = scan.nextLine();
        }while(!option.equals("zz".toLowerCase().trim()));
        System.out.println("Final score: "+score); //this prints at the end of the game, total score
        System.out.println("Thank you for playing :)");
    }

    private void showPlayedWords(){
        //shows the words that have been played by the user, if its the user's first game
        //then the list should be empty
        if(playedWords.isEmpty()){
            System.out.println("No words have been played ");
        } else{
            System.out.println("Current list of played words: ");
            for(int i = 0; i < playedWords.size();i++){
                System.out.println(playedWords.get(i));
            }
        }
    }

    private void showWordList(){
        //this prints the current list of words that were read from the txt file
        System.out.printf("Current word list: ");
        for(int i = 0; i < wordList.size(); i++){
            System.out.println(wordList.get(i));
        }
    }

    private void showPlayerGuesses(){
        //shows the words that the player has guessed if any
        //after it asks if you want to add the new guesses to the words being used in the game
        Scanner scan = new Scanner(System.in);
        if(playerGuesses.isEmpty()){
            System.out.println("No guesses yet. ");
        } else{
            System.out.println("Current player guesses: ");
            //loops through the guesses and prints each word
            for(int i = 0; i < playerGuesses.size();i++){
                System.out.println(playerGuesses.get(i));
            }
        }
        System.out.println("Would you like to add the words to the word list? (y/n) ");
        String answer = scan.nextLine();
        if(answer.equals("y")) {
            updateWordList(); //adds the words the player guessed to the word list
            showWordList(); //shows the list
        }
    }

    private int guess(){
        //is the main game that calls all the functions
        ArrayList<String> currentGuesses = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int letterCount = 0;
        int score = WORD_SIZE + 1;
        String wordGuess;
        while(score != 0){
            //as long as the score is not 0, the game will keep going
            System.out.println("Current score: "+ score);
            System.out.println("What is your guess (q to quit): ");
            wordGuess = scan.nextLine();
            if(wordGuess.trim().toLowerCase().equals("q")){
                if(score > 0){
                    score = 0;
                    break;
                }
            }else{
                //checks if the word guessed is the right length
                if(wordGuess.length() == WORD_SIZE){
                    //prints out the jotto score of that word
                    System.out.println(wordGuess + " has a score of " + getLetterCount(wordGuess));
                    addPlayerGuess(wordGuess);
                    System.out.println("Current score: " + score);
                    showPlayedWords();
                    //if the word guessed is correct then it congrats you and returns the score
                     if(wordGuess.equals(currentWord)){
                        System.out.println("DINGDINGDING!!! the word was "+ currentWord);
                        currentGuesses.add(wordGuess);
                        playerGuessScores(currentGuesses);
                        System.out.println("Your score "+score);
                        return score;

                    }else if(letterCount == WORD_SIZE){ //checks if the word is an anagram of the answer
                         if(!wordGuess.equals(currentWord)){
                             System.out.println("The word you chose is an anagram. ");
                         }
                     }
                    if(playedWords.contains(wordGuess)){ //checks if the word was already guessed
                        System.out.println("This word was already played.");
                        continue;
                    }
                    currentGuesses.add(wordGuess);
                    letterCount = getLetterCount(wordGuess);

                    score--; //score goes down by one every guess
                    playerGuessScores(currentGuesses);
                }else if(wordGuess.length() != WORD_SIZE){ //makes sure the guess is 5 letters long
                    System.out.println("Word must be 5 characters ("+wordGuess+" is "+wordGuess.length());
                    continue;
                }
            }
        }
        return score;
    }

    public int getLetterCount(String wordGuess){
        //this gets the jotto score of the word for each word guessed
        int count = 0;
        ArrayList<Character> dupes = new ArrayList<>();
        for(int i = 0; i < wordGuess.length();i++){//using a nested for loop to go through every letter of both words and checking them
            for(int j = 0; j <  currentWord.length();j++){
                if(wordGuess.charAt(i) == currentWord.charAt(j)){
                    if(!dupes.contains(currentWord.charAt(i))){
                        dupes.add(wordGuess.charAt(i));//if the letter is new, it adds it to the list
                        count++;
                    }
                }
            }
        }
        return count; //returns the count(jotto score)
    }

    private void updateWordList(){
        try{ //tries to add the words that the player guessed to the word list of words that willl be used in the game
            FileWriter writer = new FileWriter(filename, true);
            for(int i = 0; i < playerGuesses.size()-1; i++){
                if(playerGuesses.contains(i) != wordList.contains(i)){
                    writer.write(playerGuesses.get(i)); //writes to the file

                }
            }
            writer.close(); //closing the writer
        }catch(IOException e){
            System.out.println("Error: IOException "); //catches the exception error
        }
    }

    private boolean pickWord(){ //randomly chooses a word from the list of words and uses it as the word the user needs to guess
        Random random = new Random();
        currentWord = wordList.get(random.nextInt(wordList.size()));
        if(playedWords.contains(currentWord) && (playedWords.size() == wordList.size())){
            System.out.println("You've guessed them all!");
            return false;
        }else if(playedWords.contains(currentWord) && (playedWords.size()) != wordList.size()){
            pickWord();
            playedWords.add(currentWord);
        }
        if(DEBUG == true){
            System.out.println("Current word: "+currentWord);
        }
        return true;
    }

    private int score(){
        return score;//returns the score
    }

    private void addPlayerGuess(String wordGuess){
        //if the word guessed is not already in the list of guesses, adds it
        if(!playerGuesses.contains(wordGuess)){
            playerGuesses.add(wordGuess);
        }
    }

    private void playerGuessScores(ArrayList<String> guesses){
        //prints out all the words the player has guessed and their jotto score
        System.out.println("Guess      Score");
        for(int i = 0; i < guesses.size();i++){
            System.out.println(guesses.get(i)+"      "+ getLetterCount(guesses.get(i)));
        }
    }

}
