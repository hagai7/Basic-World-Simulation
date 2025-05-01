# **World Simulation**
## **Description:**
A Java-based simulation of a two-dimensional virtual world with dynamic environmental features. The simulation includes a day-night cycle, animated trees with seasonal behavior (wind movement, leaf fall in autumn, regrowth), and a controllable avatar that can walk, run, jump, and fly.

## **Prerequisites:**
- Java (JDK 8 or higher)
- A text editor or IDE (e.g. IntelliJ, VSCode)

## **Setup Instructions:**

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/basic-world-simulation.git
    cd basic-world-simulation
    ```

2. **Compile the Code:**
    ```bash
    javac main/Game.java
    ```

3. **Run the Simulation:**
    ```bash
    java main.Game
    ```

## **Project Structure:**
The project is organized into several Java packages:
- `main` – Manages the game logic and simulation flow
- `util` – Utility classes for terrain layout and color management
- `worlds` – Implements world features including avatar behavior, trees, sky, lighting, and environment dynamics
- `assets` – Contains images representing different avatar poses: resting, walking, running, jumping, flying
- `uml` – Includes a UML diagram showing the structure and design of the program

## **Screenshot:**

![image](https://user-images.githubusercontent.com/87193121/230771574-9ca3c15e-afd1-415e-95a2-0070045b314d.png)

---

Feel free to extend the simulation with new terrain types, weather effects, or enhanced avatar interactions.
