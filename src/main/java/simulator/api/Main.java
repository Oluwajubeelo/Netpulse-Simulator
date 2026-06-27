package simulator.api;

import io.javalin.Javalin;
import simulator.core.EventLoop;
import simulator.core.Host;
import simulator.core.Router;
import simulator.core.packetPool;
public class Main {
    private static int nextHostId = 1;

    public static void main(String[] args){
        packetPool pool = new packetPool(10000);
        Router router = new Router(200, 50);
        EventLoop loop = new EventLoop(router, pool);

        loop.addHost(new Host(nextHostId++));

        Thread engineThread = new Thread(loop);
        engineThread.start();

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        }).start(8080);

        System.out.println("\n--- Congestion Sandbox Backend is LIVE ---");
        System.out.println("View live data at: http://localhost:8080/stats\n");

        app.get("/stats", ctx ->{
            ctx.json(loop.getSystemSnapshot());
        });

        app.post("/addHost" , ctx ->{
            Host newHost = new Host(nextHostId++);
            loop.addHost(newHost);
            ctx.result("Added Host " + newHost.hostId);
            System.out.println("Spawning new host: " + newHost.hostId);
        });

        app.post("/removeHost", ctx -> {
            String idParam = ctx.queryParam("id");
            if(idParam != null){
                loop.removeHost(Integer.parseInt(idParam));
                ctx.result("Removed Host " + idParam);
                System.out.println("Killing host: " + idParam);
            }
            else{
                ctx.status(400).result("Missing host ID");
            }
        });
    }
}
