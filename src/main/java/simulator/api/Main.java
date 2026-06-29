package simulator.api;

import io.javalin.Javalin;
import simulator.core.EventLoop;
import simulator.core.Host;
import simulator.core.Router;
import simulator.core.packetPool;

public class Main {
    private static int nextHostId = 1;
    private static EventLoop currentLoop;
    private static Thread engineThread;

    public static void resetEngine(){
        if (currentLoop!=null){
            currentLoop.isRunning=false;
        }

        Host.useSlowStart = true;

        packetPool pool = new packetPool(10000);
        Router router = new Router(200, 50);
        currentLoop = new EventLoop(router, pool); 

        nextHostId = 1;
        currentLoop.addHost(new Host(nextHostId++));

        engineThread = new Thread(currentLoop);
        engineThread.start();
    }

    public static void main(String[] args){
        resetEngine();

        Javalin app = Javalin.create(config ->{
            config.bundledPlugins.enableCors( cors ->{
                cors.addRule(it -> it.anyHost());
            });
            config.staticFiles.add("frontend", io.javalin.http.staticfiles.Location.EXTERNAL);
        }).start(8080);

        System.out.println("\n--- Congestion Sandbox Backend is LIVE ---");
        System.out.println("View live data at: http://localhost:8080/\n");

        app.get("/stats", ctx ->{
            ctx.json(currentLoop.getSystemSnapshot());
        });

        app.post("/addHost" , ctx->{
            Host newHost = new Host(nextHostId++);
            currentLoop.addHost(newHost);
            ctx.result("Added Host " + newHost.hostId);
            System.out.println("Spawning new host: " + newHost.hostId);
        });

        app.post("/removeHost", ctx ->{
            String idParam = ctx.queryParam("id");
            if(idParam != null){
                currentLoop.removeHost(Integer.parseInt(idParam));
                ctx.result("Removed Host " + idParam);
                System.out.println("Killing host: " + idParam);
            }
            else{
                ctx.status(400).result("Missing host ID");
            }
        });

        app.post("/togglePause", ctx ->{
            if(currentLoop != null){
                currentLoop.isPaused = !currentLoop.isPaused;
                ctx.result("Paused: " + currentLoop.isPaused);
                System.out.println("Simulation Paused: " + currentLoop.isPaused);
            }
        });

        app.post("/reset", ctx ->{
            resetEngine();
            ctx.result("Engine Reset");
            System.out.println("--- The engine has been reset via frontend reload ---");
        });

        app.post("/toggleSlowStart", ctx -> {
            Host.useSlowStart = !Host.useSlowStart;
            ctx.result("Toggled");
            System.out.println("Slow Start is now: " + (Host.useSlowStart ? "ON":"OFF"));
        });
    }
}
