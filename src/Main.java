/**
 * FILE: Main.java
 * AUTHOR: SEDA GUNES
 * STUDENT ID: 2025719036
 * DATE: 19.10.2025
 * AI ASSISTANTS: OpenAI's ChatGPT (GPT-5) , Google Gemini , Claude
 * DESCRIPTION:
 * The entry point for the Angry Bullet game application.
 * This class is solely responsible for instantiating the GamePlay
 * class and initiating the main game loop.
 */
public class Main {
    /**
     * The main method, which starts the application.
     * @param args Command line arguments (not used).
     * @throws java.io.FileNotFoundException If the config.txt file is not found by GamePlay.
     */
    public static void main(String[] args) throws java.io.FileNotFoundException {

        // Create an instance of the GamePlay class
        GamePlay game = new GamePlay();

        // Start the game application.
        game.startGame();
    }
}