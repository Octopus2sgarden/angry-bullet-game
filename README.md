# Angry Bullet Game 🎯

A physics-based projectile motion game built with Java and StdDraw library.

## About
Players aim and fire a bullet to hit an orange target while avoiding gray 
obstacles. Velocity and angle are controlled by mouse position.

## How to Run
1. Compile: `javac *.java`
2. Run: `java Main`
3. Controls: Mouse to aim, SPACE to fire, R to restart

## Features
- Realistic projectile motion physics (parabolic trajectory)
- Real-time aiming line with angle/velocity display
- Trajectory path visualization
- External level config via `config.txt`

## OOP Concepts
| Concept | Where Used |
|---|---|
| Inheritance | `Obstacle` and `Target` extend `Shape` |
| Polymorphism | `draw()` and `isColliding()` overridden in subclasses |
| Encapsulation | Private fields with public getters |
| Composition | `GamePlay` contains `Bullet` and lists of `Shape` objects |

## Class Structure
- `Main.java` → Entry point
- `GamePlay.java` → Game loop and state management  
- `Bullet.java` → Physics calculations
- `Shape.java` → Abstract base class
- `Obstacle.java` / `Target.java` → Game objects

## 📄 [Full Report](report.pdf)
