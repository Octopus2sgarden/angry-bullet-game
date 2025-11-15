import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// NOTE: Ensure your separate Bullet, Shape, Target, and Obstacle classes are imported/accessible.

public class Main {

    // 1. Fields (instance variables) are correctly defined at the top of the class.
    final private List<Shape> obstacles = new ArrayList<>();
    final private List<Shape> targets = new ArrayList<>();

    /**
     * The main entry point of the program.
     * Handles the static context and initializes the game instance.
     * Declares 'throws FileNotFoundException' to bypass try-catch (as per your requirement).
     */
    public static void main(String[] args) throws java.io.FileNotFoundException {
        // Create an instance of the Main class to access instance methods and fields.
        Main game = new Main();

        // Call the instance method through the 'game' object.
        game.loadGameObjects("config.txt");

        // TODO: The StdDraw configuration and game loop starts here.
        // game.startSimulationLoop();
    }

    /**
     * Reads the config file, parses the data using the split(",") format,
     * and creates Target/Obstacle objects, storing them in the respective lists.
     */
    public void loadGameObjects(String filename) throws java.io.FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // Skip comment lines or empty lines
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }

            // Split the line by comma (Format: OBS,0,0,50,50)
            String[] parts = line.split(",");

            // Convert String data to double (assuming minimal validation)
            String type = parts[0].trim();
            double x = Double.parseDouble(parts[1].trim());
            double y = Double.parseDouble(parts[2].trim());
            double w = Double.parseDouble(parts[3].trim());
            double h = Double.parseDouble(parts[4].trim());

            // Create the object based on type and add it to the list
            if (type.equals("OBS")) {
                obstacles.add(new Obstacle(x, y, w, h));
            } else if (type.equals("TGT")) {
                targets.add(new Target(x, y, w, h));
            }
        }
        scanner.close();

        // Optional: Print a confirmation message to console
        System.out.println("Game objects loaded. Obstacles: " + obstacles.size() + ", Targets: " + targets.size());
    }
}