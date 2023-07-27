import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Smidle {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private static final int MAX_GUESSES = 5;
    private static final String[] NUMBERED_GUESS = { ANSI_RED + "Final guess: ", ANSI_YELLOW + "Fourth guess: ", ANSI_YELLOW + "Third guess: ", ANSI_GREEN + "Second guess: ", ANSI_GREEN + "First guess: "};
    private static God[] godList;

    private static void godListInstantiation() {
        try {
            File file = new File("SmiteGodList.csv");
            Scanner scan = new Scanner(file);

            int godArraySize = Integer.parseInt(scan.nextLine().trim());
            godList = new God[godArraySize];
            scan.useDelimiter(",\\s*|\n");

            for(int godPosition = 0; godPosition < godArraySize; godPosition++) {
                godList[godPosition] = new God(Integer.parseInt(scan.next().trim()), scan.next(), scan.next(), Class.valueOf(scan.next()), Pantheon.valueOf(scan.next()), Type.valueOf(scan.next()), BasicAttack.valueOf(scan.next()), new SimpleDateFormat(scan.next().trim()));
            }

            scan.close();

        } catch (IOException ioError) {
            System.out.println("There was an error when reading in the gods file, please ensure it's within this package!");
        } catch (Exception programFailure) {
            System.out.print("The programs CSV reader (what reads the Gods CSV file) is not formatted correctly. A proper CSV must be provided!");
            System.out.print(programFailure.getMessage());
        }
    }

    public static void playSmidle() {

        godListInstantiation(); //Loads in the god list from the CSV file stored in the package.

        //Explains the game
        System.out.println(ANSI_GREEN + "\n\nWelcome to Smidle, a Wordle-styled game where you must guess who the chosen god is from the MOBA game Smite!\n" + ANSI_RESET);
        //States the number of available game modes
        System.out.println("There are 3 different game modes for you to choose from. Below, the 3 modes are explained in detail.");
        //Notifies user how to learn more about playing the game
        System.out.println("If you are new or need a refresher on how to play, simple type \"help\" to learn the rules and how to play!");
        //Explains how to choose a game mode
        System.out.println("To choose a game mode, simply type the number that represents the game mode and hit \"Enter\" to play it.\n");

        //Daily-Smidle
        System.out.println("1\t-\t" + ANSI_GREEN + "Today's Smidle" + ANSI_RESET + " | Everyday a new God is chosen for you to guess.");

        //Archived Daily-Smidle's
        System.out.println("2\t-\t" + ANSI_BLUE + "Past Daily Smidle" + ANSI_RESET + " | Choose from past Smidle's by inputting in an older date." + ANSI_RED + "\tCURRENTLY UNAVAILABLE" + ANSI_RESET);

        //Play with a random god
        System.out.println("3\t-\t" + ANSI_PURPLE + "Randomly Chosen God" + ANSI_RESET + " | Have a random god chosen for you to guess.\n");

        Scanner input;
        String choice;

        do {
            System.out.print("Game mode: ");
            input = new Scanner(System.in);
            choice = input.nextLine();
            choice = choice.trim();
            if(choice.equalsIgnoreCase("help")) {
                System.out.println("\n--------------------" + ANSI_GREEN + " HOW TO PLAY " + ANSI_RESET + "--------------------\nThe goal of the game is to guess the correct god that the game has chosen for you to guess.");
                System.out.println("The game allows you " + ANSI_BLUE + "5" + ANSI_RESET + " attempts to guess the correct god.");
                System.out.println("If you guess the correct god, you " + ANSI_GREEN + "WIN" + ANSI_RESET + "!");
                System.out.println("If you make a guess that is wrong, information about the god you guessed is shown to you to help guide your remaining guesses.");
                System.out.println("The information that is shown is of the following format:\n");
                System.out.println("God Name | Pantheon | Position | Type | Basic Attack | Release Order & Date");
                System.out.println("\nThe god you guessed will have this information coloured to help you. The following colours for each information criteria means the following:\n");
                System.out.println("\"Pantheon:" + ANSI_GREEN + " Greek" + ANSI_RESET + "\" means the Pantheon you guessed was correct, with \"Pantheon:" + ANSI_RED + " Roman" + ANSI_RESET + "\" meaning it was wrong.");
                System.out.println("\"Position:" + ANSI_GREEN + " Warrior" + ANSI_RESET + "\" means the Position you guessed was correct, with \"Position:" + ANSI_RED + " Hunter" + ANSI_RESET + "\" meaning it was wrong.");
                System.out.println("These same rules apply to Type and Basic Attack as well.");
                System.out.println("\"Release Order:" + ANSI_YELLOW + " 54 " + ANSI_RESET + "↓ (Lower)\" means the Release Order you guessed was within 10 releases of the correct god and tells you the release number is a little lower (older release).");
                System.out.println("\"Release Order:" + ANSI_RED + " 4 " + ANSI_RESET + " ↑ (Higher)\" means the Release Order you guessed was NOT within 10 releases of the correct god and tells you the release number is much higher (newer release).");
                System.out.println("The date simply helps guide you with a timeframe of when it may have been released in case the release number does not help you. It operates the same as Position and Pantheon for it's colour.");
                System.out.println("\nAnd ultimately, if you run out of guesses, you " + ANSI_RED + "LOSE" + ANSI_RESET + ".");
                System.out.println("Good luck and come back to this if you ever feel you need a refresher on how to play!\n-----------------------------------------------------\n");
            }
            else if(!choice.equals("1") && !choice.equals("2") && !choice.equals("3"))
                System.out.println("\n\nYour choice is not one of the available game modes, please provide the number of a valid game mode that is listed above.\n");
        } while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3"));

        switch (choice) {
            case "1" -> {
                LocalDate currentDate = LocalDate.now();
                int hash = godHash(currentDate.getDayOfMonth(), currentDate.getMonthValue(), currentDate.getYear());
                Smidle.smidleGame(hash);
            }
            case "2" -> {
                System.out.println("\nPlease provide the date for what past Daily Smidle you want to play in the format MM/DD/YYYY (e.g. 02/19/2017).\n");
                int hash = -1;
                Scanner date;

                do {

                    System.out.print("Smidle Date (MM/DD/YYYY): ");
                    date = new Scanner(System.in);
                    String dateString = date.nextLine();
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                    try {

                        LocalDate dateParse = LocalDate.parse(dateString, dateFormat);
                        int year = dateParse.getYear();
                        int month = dateParse.getMonthValue();
                        int day = dateParse.getDayOfMonth();

                        hash = godHash(day,month,year);

                    } catch(DateTimeParseException improperInput) {
                        System.out.println("\nThe input you provided was invalid, please ensure your date is in the format of MM/DD/YYYY.\n");
                    } catch(Exception crash) {
                        System.out.println("\nSomething went wrong, please try again and ensure your input is valid.\n");
                    }

                } while(hash == -1);

                Smidle.smidleGame(hash);
            }
            case "3" -> {
                int chosenGod = (int) (Math.random() * godList.length);
                Smidle.smidleGame(chosenGod);
            }
        }

        System.out.print("\nWould you like to play again? Press \"y\" and enter to play again, or enter any other key to quit the game.\n\n-> ");
        Scanner response = new Scanner(System.in);
        String responseString = response.nextLine().trim();
        if(responseString.equalsIgnoreCase("y"))
            playSmidle();

        response.close();

    }

    private static int godHash(int day, int month, int year) {

        return (((int) Math.pow(day, 2) * 13) + (month * (13)) + (year)) % (godList.length - 1);

    }

    private static void smidleGame(int godChoice) {

        God chosenGod = godList[godChoice];
        int remainingGuesses = MAX_GUESSES;
        boolean correctGuess = false;
        Scanner guessInput;
        String guess;

        System.out.println("You have " + ANSI_GREEN + "5" + ANSI_RESET + " guesses to find out who the mystery god is. Good luck!\n");

        while(!correctGuess && remainingGuesses > 0) {
            //Ask for a guess
            System.out.print(NUMBERED_GUESS[remainingGuesses-1] + ANSI_RESET);
            guessInput = new Scanner(System.in);
            guess = guessInput.nextLine().trim();
            boolean guessedGodFound = false;
            int godPosition;

            for(godPosition = 0; godPosition < godList.length && !guessedGodFound; godPosition++) {
                //If guess is valid...
                if(godList[godPosition].getName().equalsIgnoreCase(guess)) {
                    guessedGodFound = true;
                    //And is the correct guess...
                    if(godList[godPosition].getName().equalsIgnoreCase(chosenGod.getName()))
                        correctGuess = true;
                    //And is not the correct god...
                    else
                        remainingGuesses--;
                }

            }

            if(guessedGodFound && correctGuess) {
                System.out.println("\nYou guessed correctly! The mystery god was " + chosenGod.getName() + ", " + chosenGod.getTitle() + "!\n");
            }
            else if(guessedGodFound) {
                God guessedGod = godList[godPosition-1];
                System.out.print("\n" + guessedGod.getName() + " | " );

                //Show if correct pantheon
                System.out.print("Pantheon: ");
                if(guessedGod.getPantheon() == chosenGod.getPantheon())
                    System.out.print(ANSI_GREEN + guessedGod.getPantheon() + ANSI_RESET + " | ");
                else
                    System.out.print(ANSI_RED + guessedGod.getPantheon() + ANSI_RESET + " | ");

                //Show if correct class/position
                System.out.print("Position: ");
                if(guessedGod.getPosition() == chosenGod.getPosition())
                    System.out.print(ANSI_GREEN + guessedGod.getPosition() + ANSI_RESET + " | ");
                else
                    System.out.print(ANSI_RED + guessedGod.getPosition() + ANSI_RESET + " | ");

                //Show if correct type
                System.out.print("Type: ");
                if(guessedGod.getType() == chosenGod.getType())
                    System.out.print(ANSI_GREEN + guessedGod.getType() + ANSI_RESET + " | ");
                else
                    System.out.print(ANSI_RED + guessedGod.getType() + ANSI_RESET + " | ");

                //Show if correct basic attack
                System.out.print("Basic Attack: ");
                if(guessedGod.getAttackType() == chosenGod.getAttackType())
                    System.out.print(ANSI_GREEN + guessedGod.getAttackType() + ANSI_RESET + " | ");
                else
                    System.out.print(ANSI_RED + guessedGod.getAttackType() + ANSI_RESET + " | ");

                //Show if correct or near the release order and date
                System.out.print("Release Order & Date: ");
                if(guessedGod.getReleaseOrder() == chosenGod.getReleaseOrder())
                    System.out.print(ANSI_GREEN + guessedGod.getReleaseOrder());
                else if(guessedGod.getReleaseOrder() > (chosenGod.getReleaseOrder() - 10) && guessedGod.getReleaseOrder() < (chosenGod.getReleaseOrder() + 10) && guessedGod.getReleaseOrder() < chosenGod.getReleaseOrder())
                    System.out.print(ANSI_YELLOW + guessedGod.getReleaseOrder() + ANSI_RESET + " ↑ (Higher)");
                else if(guessedGod.getReleaseOrder() > (chosenGod.getReleaseOrder() - 10) && guessedGod.getReleaseOrder() < (chosenGod.getReleaseOrder() + 10) && guessedGod.getReleaseOrder() > chosenGod.getReleaseOrder())
                    System.out.print(ANSI_YELLOW + guessedGod.getReleaseOrder() + ANSI_RESET + " ↓ (Lower)");
                else if(guessedGod.getReleaseOrder() > chosenGod.getReleaseOrder())
                    System.out.print(ANSI_RED + guessedGod.getReleaseOrder() + ANSI_RESET + " ↓ (Lower)");
                else
                    System.out.print(ANSI_RED + guessedGod.getReleaseOrder() + ANSI_RESET + " ↑ (Higher)");
                //Now the date
                if(guessedGod.getReleaseDate().equals(chosenGod.getReleaseDate()))
                    System.out.print(ANSI_RESET + " & " + ANSI_GREEN + guessedGod.getReleaseDate().toPattern() + "\n\n");
                else
                    System.out.print(ANSI_RESET + " & " + ANSI_RED + guessedGod.getReleaseDate().toPattern() + "\n\n");

            }
            else {
                System.out.println("\nThe god you guessed (\"" + guess + "\") is not a god in the game. Please ensure spelling is correct as the name must be correct!\n\n");
            }

        }

        //Let user know the game is over and who the correct god was
        if(!correctGuess)
            System.out.println(ANSI_RED + "GAME OVER!" + ANSI_RESET + " You unfortunately have run out of guesses. The correct god was " + chosenGod.getName() + ", " + chosenGod.getTitle() + "!\n");

    }

}
