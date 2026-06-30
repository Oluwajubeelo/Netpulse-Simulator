NETPULSE: TCP CONGESTSION CONTROL SANDBOX
NetPulse is a custom-built discrete-time network simulator and visualization dashboard. It demonstrates how multiple independent hosts share a single router's bandwidth using TCP Congestion Control algorithms (AIMD & Slow Start) without a central coordinator.

This project features a custom Java Event Loop engine, manual memory management via Object Pooling, a 2D HTML5 Canvas physics engine, and a decoupled JavaScript dashboard for live telemetry visualization.

FEATURES
-Custom Event Loop Engine: A deterministic, single-threaded Java backend running a discrete "Master Clock" to prevent multi-threading race conditions.
-Zero-Allocation Memory: Uses an Object Pool of 10,000 pre-allocated packets to simulate traffic without trigerring Java Garbage Collection pauses.
-Live Topology Physics: An HTML5 <canvas> animation that visualizes actual packet density, travel times, and collision physics when packets are dropped at the router wall.
-Real-time Telemetry: A decoupled Javascript frontend that polls the Java engine and plots live transmission rates(cwnd) on a dynamic Chart.js graph
-Interactive Sandbox Controls: *Spawn and kill hosts dynamically.
    -Toggle Slow Start on/off to see the mathematical difference between exponential and additive growth
    -Time Controls: Pause and resume the simulation at any millisecond to analyze packet drops and TCP states.
-Cloud Ready: Fully containerized with a Dockerfile and dynamic port mapping for easy deployment on platforms like Render(which is what I'm using to host)

LIVE DEMO
https://netpulse-simulator.onrender.com

LOCAL SETUP INSTRUCTIONS
This project is bundled as a single full-stack application. You only need to run the backend engine, and it willl automatically serve the frontend web dashboard.
-Prerequisites
Java 17 or higher
Maven

Running via IDE(IntelliJ/VS Code)
1. Open the project in your IDE
2. Allow Maven to sync/download the dependencies (Javalin and Jackson)
3. Navigate to src/main/java/simulator/api/Main.java
4. Click Run on the main method
5. Open your web browser and navigate to: http://localhost:8080

Running via Terminal
1. Clone the repository and navigate to the root directory
2. Build the project: mvn clean install
3. Run the engine mvn exec: java
4. Open your web browser and navigate to http://localhost:8080

INTERACTING WITH THE SANDBOX
1. The TCP Sawtooth: Click Spawn New Host a few times to add computers to the network. Once the Router queue hits 100% packets will drop (Tail Drop). Observe the graph as hosts instantly cut their speeds and enter the classic TCP "sawtooth" pattern.
2. Observe Slow Start: Turn the Slow Start toggle off, then spawn a new host. Watch how slowly it claims available bandwidth compared to a host using the exponential growth of Slow Start.
3. The Topology View: Watch the animated cables. Slower hosts will have sparsely placed packets, while high-bandwidth hosts will create dense "packet trains". When the router is full, watch the packets turn red and bounce off
4. Time Freezing: Click Pause Simulation to freeze the physics engine and inspect the current state of the network.