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
    private final double MAX_Y_INTERVAL = 400.0; // StdDraw.setYscale(0, 400) ile eşleşmeli
    private final double GROUND_LEVEL = 0.0;
    private final double MAX_V0 = 300.0;

    private double currentV0;
    private double currentTheta;

    // Atış anındaki sabit hız ve açı değerleri (Yörüngeyi sabitlemek için)
    private double shotV0;
    private double shotTheta;

    private Bullet bullet;

    private boolean isBulletMoving = false;
    private boolean isAwaitingRestart = false;
    private String lastOutcomeMessage = "READY: Adjust V0 and Theta with mouse. Click to fire.";

    // Fırlatma noktası (İlk OBS'in sağ üst köşesi varsayılır: 50x50, 0,0'da)
    private final double SHOOTING_X = 50.0;
    private final double SHOOTING_Y = 55.0; // Merminin yarıçapını hesaba katarak 50 + 5 = 55 yapıldı

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

        // Canvas size ve Y scale tutarlı hale getirildi
        StdDraw.setCanvasSize(700, 400);
        StdDraw.setXscale(0, MAX_X_INTERVAL);
        StdDraw.setYscale(0, MAX_Y_INTERVAL); // MAX_Y_INTERVAL 400 olarak kullanıldı

        // 2. Load Game Objects
        loadGameObjects("config.txt");

        // 3. Start control loop
        controlLoop();
    }

    // --- GAME LOGİC METHODS (loadGameObjects ve drawGameSetup değişmedi) ---

    // ... loadGameObjects (değişmedi)

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
                    obsColor = StdDraw.BLACK;
                } else {
                    obsColor = StdDraw.DARK_GRAY;
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

            // 1. Durum Kontrolleri ve Nişan Alma/Ateşleme
            if (!isBulletMoving && !isAwaitingRestart) {
                // Nişan Alma Modu: Kullanıcı girişi işle, nişan çizgisini ve YÖRÜNGE TAHMİNİNİ çiz
                handleUserInput();
                drawShootingLine();
            }

            // 2. Mermi Hareketi ve Yörünge Çizimi (Atış Yapıldıysa)
            if (bullet != null) {
                if (isBulletMoving) {

                    drawTrajectory(shotV0, shotTheta);

                    String outcome = runSimulationStep();

                    if (!outcome.equals("MOVING")) {
                        // Simulation ended (Hit, Ground, Max X)
                        isBulletMoving = false;
                        isAwaitingRestart = true;
                        lastOutcomeMessage = outcome;
                    }
                    bullet.draw(); // Her adımda mermiyi çiz

                } else {
                    // Mermi hareket etmeyi durdurdu (isAwaitingRestart TRUE)
                    drawTrajectory(shotV0, shotTheta); // Sabit yörüngeyi tekrar çiz
                    bullet.draw(); // Son konumunu çiz
                }
            }


            // 3. R TUŞU İLE YENİDEN BAŞLATMA KONTROLÜ
            if (isAwaitingRestart && StdDraw.isKeyPressed('R')) {
                bullet = null;
                isAwaitingRestart = false;
                // Atış değerlerini temizle (Nişan alma moduna dönerken)
                shotV0 = 0.0;
                shotTheta = 0.0;
                lastOutcomeMessage = "READY: Adjust V0 and Theta with mouse. Click to fire.";
            }


            // Display the outcome message at the top left
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
            // ATIS ANINDAKI DEGERLERI KAYDET
            shotV0 = currentV0;
            shotTheta = currentTheta;

            bullet = new Bullet(SHOOTING_X, SHOOTING_Y, shotV0, shotTheta);
            isBulletMoving = true; // Mermiyi hareket ettirmeye başla
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

        // Açı ve Hız gösterimi
        StdDraw.text(textX, textY + 5, String.format("a: %.1f°", currentTheta));
        StdDraw.text(textX, textY - 5, String.format("v: %.1f", currentV0));

        StdDraw.setFont();

        // 2. Draw the AIMING LINE (Nişan alma çizgisi)
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.003);

        double thetaRad = Math.toRadians(currentTheta);
        final double MAX_LINE_LENGTH = 100.0;
        double lineLength = (currentV0 / MAX_V0) * MAX_LINE_LENGTH;
        // Çizginin minimum uzunluğunu sıfır hızda bile görünür olması için ayarla
        lineLength = Math.max(5.0, lineLength);


        double endX = SHOOTING_X + lineLength * Math.cos(thetaRad);
        double endY = SHOOTING_Y + lineLength * Math.sin(thetaRad);

        StdDraw.line(SHOOTING_X, SHOOTING_Y, endX, endY);
        StdDraw.setPenRadius();
    }

    /**
     * Draws the predicted or fixed trajectory of the bullet using a dotted line.
     * Bu metot, v0 ve theta değerlerini parametre olarak alır.
     */
    public void drawTrajectory(double v0, double thetaDegrees) {

        // Define trajectory parameters
        double thetaRad = Math.toRadians(thetaDegrees);
        double vX = v0 * Math.cos(thetaRad);
        double vY0 = v0 * Math.sin(thetaRad);

        double time = 0.0;
        double deltaTime = 0.05;

        StdDraw.setPenColor(StdDraw.BLACK);
        double pointRadius = 1.0;

        // Yörüngeyi ekran sınırını geçene kadar veya 20 saniyeye kadar çiz. (5.0'dan artırıldı)
        while (time < 20.0) {
            time += deltaTime;
            // Parabolik hareket formülleri
            double nextX = SHOOTING_X + vX * time;
            double nextY = SHOOTING_Y + vY0 * time - 0.5 * Bullet.g * time * time;

            // Sınır kontrolü
            if (nextY <= GROUND_LEVEL || nextY >= MAX_Y_INTERVAL || nextX >= MAX_X_INTERVAL) {
                break;
            }

            // Her 3. adımı çizerek kesik kesik bir çizgi efekti oluştur
            if (((int)(time / deltaTime)) % 3 == 0) {
                StdDraw.filledCircle(nextX, nextY, pointRadius);
            }
        }
    }
    /**
     * Executes one step of the projectile motion simulation and checks for collision/termination conditions.
     * Returns a String indicating the game state ("MOVING" or the outcome message).
     */
    public String runSimulationStep() {
        // Bu metot sadece mermi var olduğunda çağrılmalıdır.
        if (bullet == null) {
            return "READY: Adjust V0 and Theta with mouse. Click to fire.";
        }

        // Animasyon hızı için küçük bir zaman adımı (0.05 saniye)
        double deltaTime = 0.08;

        // 1. Merminin konumunu güncelle
        bullet.updatePosition(deltaTime);

        // 2. Çarpışma ve Sınır Kontrolleri

        // A) Hedef Vuruldu mu? (En yüksek öncelik)
        for (Shape target : targets) {
            if (target.isColliding(bullet)) {
                // Mermiyi yok etmiyoruz, sadece hareketini durduruyoruz ve son konumunda kalmasını sağlıyoruz
                return "Congratulations: You hit the target! (SUCCESS)";
            }
        }

        // B) Engele Çarptı mı?
        for (Shape obstacle : obstacles) {
            if (obstacle.isColliding(bullet)) {
                return "Hit an obstacle! Press 'r' to shoot again.(FAILURE)";
            }
        }

        // C) Maksimum X Sınırını Aştı mı?
        if (bullet.getxCur() > MAX_X_INTERVAL) {
            return "Max X interval exceeded! (FAILURE)";
        }

        // E) Yere Düştü mü? (Y koordinatı 0 veya altına indi mi?)
        if (bullet.getyCur() - bullet.getRadius() <= GROUND_LEVEL) {
            return "Hit the ground! Press 'r' to shoot again. (FAILURE)";
        }

        // Hiçbir koşul sağlanmadıysa, mermi hareket etmeye devam ediyor.
        return "MOVING";
    }

    /**
     * Displays the current outcome or status message on the top-left corner of the screen.
     */
    public void displayOutcome(String message) {
        double textX = MAX_X_INTERVAL / 2.0;
        double textY = MAX_Y_INTERVAL - 20;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));

        String displayMessage = message;

        if (message.startsWith("RESULT:")) {
            String outcome = message.replace("RESULT: ", "");
            // Sonuç mesajlarına göre yeniden ateşleme talimatını ekle
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
            // Mermi hareket ederken mesaj gösterme
            displayMessage = "";
        }

        StdDraw.text(textX, textY, displayMessage);
        StdDraw.setFont();
    }
}