NETPULSE: TCP CONGESTION CONTROL SANDBOX
Netpulse is a custom-built network simulator. It demonstrates how multiple independent hosts share a single router's bandwidth using TCP Congestion Control algorithms (AIMD and Slow Start) without a central coordinator.
It features a Java Event Loop Engine designed by yours truly. It also features manual memory management via Object Pooling, and a decoupled JavaScript dashboard for live telementry visualization.

HOW TO RUN THE SIMULATION
You only need to run the backend engine, and it will automatically serve the frontend web dashboard.

PREREQUISITES
-you need Java 17 or higher
-you need to have maven as well
-if have the above:
    -open the project in your IDE(VS code or IntelliJ IDEA)
    -Allow Maven to download the dependencies (Javalin and Jackson).
    -Navigate to src/main/simulator/api/Main.java
    -click Run on the main method
    -open your web browser and navigate to: http://localhost:8080

THE SANDBOX
-Click Spawn New Host to add computers to the network
-Watch the live graph as they enter TCP Slow Start (exponential growth)
-Once the Router queue hits 100% packets will drop (Tail Drop)
-Watch the graph as hosts instantly cut their speeds and enter the classic TCP "Sawtooth" pattern (Addiive increase, Multiplicative decrease)
-Click the trash icon on any host to instantly terminate its connection and watch the remaining hosts rapidly concume the newly freed bandwidth