/**
 * FILE: GamePlay.java
 * AUTHOR: SEDA GUNES
 * STUDENT ID: 2025719036
 * DATE: 19.10.2025
 * AI ASSISTANTS: OpenAI's ChatGPT (GPT-5) , Google Gemini , Claude
 * DESCRIPTION:
 * This class handles the core game logic, including user input for aiming,
 * simulating the projectile motion (Angry Bullet physics), drawing the game setup (obstacles/targets),
 * managing collisions, and displaying the outcome. It allows the bullet to exit the top screen boundary
 * and tracks its off-screen position.
 */

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GamePlay {

    // --- GAME FIELDS ---
    private List<Shape> obstacles = new ArrayList<>();
    private List<Shape> targets = new ArrayList<>();

    private final double INITIAL_V0 = 180.0;
    private final double INITIAL_THETA = 45.0; // Degrees
    private final double MAX_X_INTERVAL = 700.0;
    private final double MAX_Y_INTERVAL = 400.0; //
    private final double GROUND_LEVEL = 0.0;
    private final double MAX_V0 = 300.0;

    private double currentV0;
    private double currentTheta;

    // Constant speed and angle values at the moment of firing (to fix the trajectory)
    private double shotV0;
    private double shotTheta;

    private Bullet bullet;

    private boolean isBulletMoving = false;
    private boolean isAwaitingRestart = false;
    private String lastOutcomeMessage = "Press Space to fire.";

    // Firing point (assumed to be the top-right corner of the first OBS: 50x50 at 0,0)
    private final double SHOOTING_X = 50.0;
    private final double SHOOTING_Y = 55.0;  // Calculated as 50 (platform height) + 5 (bullet radius)

    // Constructor
    public GamePlay() {
        //  Assign initial values
        this.currentV0 = INITIAL_V0;
        this.currentTheta = INITIAL_THETA;
    }

    /**
     * Sets up StdDraw, loads game objects, and starts the control loop.
     */
    public void startGame() throws java.io.FileNotFoundException {
        // 1. StdDraw Settings
        StdDraw.enableDoubleBuffering();

        /// Canvas size and Y scale adjusted to be consistent
        StdDraw.setCanvasSize(700, 400);
        StdDraw.setXscale(0, MAX_X_INTERVAL);
        StdDraw.setYscale(0, MAX_Y_INTERVAL); // MAX_Y_INTERVAL is used as 400

        // 2. Load Game Objects
        loadGameObjects("config.txt");

        // 3. Start control loop
        controlLoop();
    }

    // --- GAME LOGİC METHODS  ---
    /**
     * Loads obstacles (OBS) and targets (TGT) from the configuration file.
     */
    public void loadGameObjects(String filename) throws java.io.FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int obstacleCount = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // Skip comment lines or empty lines
            if (line.trim().isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split(",");

            String type = parts[0].trim();
            double x = Double.parseDouble(parts[1].trim());
            double y = Double.parseDouble(parts[2].trim());
            double w = Double.parseDouble(parts[3].trim());
            double h = Double.parseDouble(parts[4].trim());

            if (type.equals("OBS")) {
                // Define color shooting platform black obstacles are dark gray
                Color obsColor;
                if (obstacleCount == 0) {
                    obsColor = StdDraw.BLACK; // The initial platform obstacle
                } else {
                    obsColor = StdDraw.DARK_GRAY; // Regular obstacles
                }

                // Create obstacles
                obstacles.add(new Obstacle(x, y, w, h, obsColor));
                obstacleCount++;
            } else if (type.equals("TGT")) {
                targets.add(new Target(x, y, w, h));
            }
        }
        scanner.close();
    }

    /**
     * Clears the screen and draws all static game elements (ground, obstacles, targets).
     */
    public void drawGameSetup() {
        // Clear the screen
        StdDraw.clear(StdDraw.WHITE);

        // Draw the ground line
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.line(0, GROUND_LEVEL, MAX_X_INTERVAL, GROUND_LEVEL);

        // Draw obstacles and targets
        for (Shape obs : obstacles) {
            obs.draw();
        }
        for (Shape tgt : targets) {
            tgt.draw();
        }
    }


    /**
     * The main game loop controlling drawing, user input, and simulation steps.
     */
    public void controlLoop() {
        while (true) {
            drawGameSetup();

            // 1. Status Checks and Aiming/Firing
            if (!isBulletMoving && !isAwaitingRestart) {
                // Aiming Mode: Process user input, draw the aiming line
                handleUserInput();
                drawShootingLine();
            }

            // 2. Bullet Movement and Trajectory Drawing (If fired)
            if (bullet != null) {
                if (isBulletMoving) {

                    // Draw the fixed trajectory path up to the current time elapsed
                    drawTrajectory(shotV0, shotTheta, bullet.getTime());

                    String outcome = runSimulationStep();

                    if (!outcome.equals("MOVING")) {
                        // Simulation ended (Hit, Ground, Max X)
                        isBulletMoving = false;
                        isAwaitingRestart = true;
                        lastOutcomeMessage = outcome;
                    }
                    bullet.draw(); // Draw the bullet at every step

                } else {
                    // Bullet has stopped (isAwaitingRestart is TRUE)
                    // Draw the full trajectory path using the fixed shot values
                    drawTrajectory(shotV0, shotTheta, bullet.getTime());
                    bullet.draw(); // Draw its final position
                }
            }


            // 3. RESTART CONTROL VIA 'R' KEY
            if ( !isBulletMoving && isAwaitingRestart && StdDraw.isKeyPressed('R')) {
                bullet = null;
                isAwaitingRestart = false;
                // Clear the shot values (Return to aiming mode)
                shotV0 = 0.0;
                shotTheta = 0.0;
                lastOutcomeMessage = "Press Space to fire.";
            }
            // Display the outcome message at the top
            displayOutcome(lastOutcomeMessage);

            StdDraw.show();
            StdDraw.pause(20);
        }
    }

    /**
     * Processes user input (mouse position and space bar press) to set aiming parameters
     * and trigger the bullet launch.
     */
    public void handleUserInput() {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();

        // 1. Calculate Velocity (V0) based on mouse distance from the firing point.
        double distance = Math.sqrt(Math.pow(mouseX - SHOOTING_X, 2) + Math.pow(mouseY - SHOOTING_Y, 2));

        // Scale distance to V0 (e.g., max V0 is 300)
        currentV0 = Math.min(distance * 0.8, 300.0);

        // 2. Calculate Angle (Theta) using Math.atan2 (results in radians, convert to degrees)
        currentTheta = Math.toDegrees(Math.atan2(mouseY - SHOOTING_Y, mouseX - SHOOTING_X));

        // 3. Limit the angle to the relevant quadrant (0 to 90 degrees)
        currentTheta = Math.max(0.0, Math.min(90.0, currentTheta));

        // 4. Launch the bullet when the mouse is pressed AND no bullet is currently active.
        if (StdDraw.isKeyPressed(32) && bullet == null) {
            // SAVE THE VALUES AT THE MOMENT OF SHOT
            shotV0 = currentV0;
            shotTheta = currentTheta;

            bullet = new Bullet(SHOOTING_X, SHOOTING_Y, shotV0, shotTheta);
            isBulletMoving = true; // Start the bullet movement
        }
    }

    /**
     * Draws the aiming line (vector) and the UI box based on the current V0 and Theta.
     */
    public void drawShootingLine() {

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));

        double textX = 25;
        double textY = 25;

        // 1. Display Angle (a) and Velocity (v)
        StdDraw.text(textX, textY + 5, String.format("a: %.1f°", currentTheta));
        StdDraw.text(textX, textY - 5, String.format("v: %.1f", currentV0));

        StdDraw.setFont();

        // 2. Draw the AIMING LINE
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.007);

        double thetaRad = Math.toRadians(currentTheta);
        final double MAX_LINE_LENGTH = 100.0;
        // Scale the line length relative to MAX_V0
        double lineLength = (currentV0 / MAX_V0) * MAX_LINE_LENGTH;
        // Set a minimum line length to keep it visible even at low velocity
        lineLength = Math.max(5.0, lineLength);


        double endX = SHOOTING_X + lineLength * Math.cos(thetaRad);
        double endY = SHOOTING_Y + lineLength * Math.sin(thetaRad);

        StdDraw.line(SHOOTING_X, SHOOTING_Y, endX, endY);
        StdDraw.setPenRadius();
    }

    /**
     * Draws the predicted or fixed trajectory of the bullet using dotted circles.
     * This method takes the launch V0 and Theta values as parameters.
     * @param v0 Initial velocity.
     * @param thetaDegrees Launch angle in degrees.
     * @param tLimit The time limit up to which the trajectory should be drawn.
     */
    public void drawTrajectory(double v0, double thetaDegrees, double tLimit) {

        // Define trajectory parameters
        double thetaRad = Math.toRadians(thetaDegrees);
        double vX = v0 * Math.cos(thetaRad);
        double vY0 = v0 * Math.sin(thetaRad);

        double time = 0.0;
        double deltaTime = 0.05;

        StdDraw.setPenColor(StdDraw.BLACK);
        double pointRadius = 3.0;

        // Draw the trajectory until it exceeds the screen boundary or reaches the time limit
        while (time < tLimit) {
            // Advance the time step
            time += deltaTime;

            // If the new calculated time exceeds the drawing limit, limit the time
            if (time > tLimit) {
                time = tLimit; // Limit time to draw the last point
            }

            // Parabolic motion formulas
            double nextX = SHOOTING_X + vX * time;
            double nextY = SHOOTING_Y + vY0 * time - 0.5 * Bullet.g * time * time;

            // Boundary check
            if (nextY <= GROUND_LEVEL || nextX >= MAX_X_INTERVAL) {
                // If the bullet has stopped (tLimit is large, meaning drawing the full path)
                if (tLimit > 10.0) { // If tLimit is 20.0, we are drawing the full path
                    break;// Stop drawing beyond the boundary
                }
            }

            // Draw every 3rd step to create a dashed line effect
            if (((int)(time / deltaTime)) % 3 == 0) {
                StdDraw.filledCircle(nextX, nextY, pointRadius);
            }

            // If the time limit has been reached, break the loop after drawing the last point
            if (time >= tLimit) {
                break;
            }
        }
    }
    /**
     * Executes one step of the projectile motion simulation and checks for collision/termination conditions.
     * Returns a String indicating the game state ("MOVING" or the outcome message).
     */
    public String runSimulationStep() {
        // This method should only be called when a bullet exists.
        if (bullet == null) {
            return "Adjust V0 and Theta with mouse. Click space fire.";
        }

        // Small time step for animation speed (0.08 seconds)
        double deltaTime = 0.08;

        // 1. Update the bullet's position
        bullet.updatePosition(deltaTime);

        // 2. Collision and Boundary Checks

        // A) Was the Target hit? (Highest priority)
        for (Shape target : targets) {
            if (target.isColliding(bullet)) {
                // We don't destroy the bullet, just stop its movement and keep it at the final position
                return "Congratulations: You hit the target! (SUCCESS)";
            }
        }

        // B) Did it hit an Obstacle?
        for (Shape obstacle : obstacles) {
            if (obstacle.isColliding(bullet)) {
                return "Hit an obstacle! Press 'r' to shoot again.(FAILURE)";
            }
        }

        // C) Did it exceed the Maximum X boundary?
        if (bullet.getxCur() > MAX_X_INTERVAL) {
            return "Max X interval exceeded! Press 'r' to shoot again. (FAILURE)";
        }

        // E) Did it hit the Ground? (Y coordinate went to 0 or below, considering the bullet's radius)
        if (bullet.getyCur() - bullet.getRadius() <= GROUND_LEVEL) {
            return "Hit the ground! Press 'r' to shoot again. (FAILURE)";
        }

        // If no condition is met, the bullet continues to move.
        return "MOVING";
    }

    /**
     * Displays the current outcome or status message on the top - center of the screen.
     */
    public void displayOutcome(String message) {
        double textX = MAX_X_INTERVAL / 2.0;
        double textY = MAX_Y_INTERVAL - 20;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));

        String displayMessage = message;

        if (message.startsWith("RESULT:")) {
            String outcome = message.replace("RESULT: ", "");
            // Add the restart instruction based on the outcome message
            if (message.contains("FAILURE")) {
                displayMessage = outcome + " (Press 'R' to shoot again.)";
            } else if (message.contains("SUCCESS")) {
                displayMessage = outcome + " (Press 'R' to restart.)";
            } else {
                displayMessage = outcome;
            }
        } else if (message.contains("READY")) {
            displayMessage = message;
        } else if (message.equals("MOVING")) {
            // Do not display a message while the bullet is moving
            displayMessage = "";
        }

        StdDraw.text(textX, textY, displayMessage);
        StdDraw.setFont();
    }
}