// package simulator.core;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// public class EventLoop implements Runnable{
//     public long currentTick;
//     private List<Host> activeHosts;
//     public Router router;
//     public packetPool pool;
//     public boolean isRunning;

//     public EventLoop(Router router , packetPool pool){
//         this.currentTick = 0;
//         this.activeHosts = new ArrayList<>();
//         this.router = router;
//         this.pool = pool;
//         this.isRunning = false;
//     }
//     public void addHOst(Host h){
//         this.activeHosts.add(h);
//     }
    
//     public void removeHost(int hostId){
//         this.activeHosts. removeIf(h -> h.hostId == hostId);
//     }
//     @Override
//     public void run(){
//         this.isRunning = true;

//         while(isRunning){
//             for(Host host : activeHosts){
//                 host.tick(currentTick, pool, router);
//             }
//             router.processTick(pool);
//             currentTick+=1;
//             try{
//                 Thread.sleep(50);
//             }
//             catch(InterruptedException e){
//                 System.out.println("Event Loop Interrupted.");
//                 isRunning = false;
//             }
//         }
//     }

//     public Map<String, Object> getSystemSnapshot(){
//         Map<String, Object> snapshot = new HashMap<>();

//         snapshot.put("currentTick", currentTick);
//         snapshot.put("router", router.getMetrics());

//         List<Map<String, Object>> hostMetrics = new ArrayList<>();
//         for (Host host : activeHosts){
//             hostMetrics.add(host.getMetrics());
//         }
//         snapshot.put("hosts", hostMetrics);
//         return snapshot;
//     }
// }
 package simulator.core;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 public class EventLoop implements Runnable{
    public long currentTick;
    private List<Host> activeHosts;
    public Router router;
    public packetPool pool;
    public boolean isRunning;

    public EventLoop(Router router, packetPool pool){
        this.currentTick = 0;
        this.activeHosts = new ArrayList<>();
        this.router = router;
        this.pool = pool;
        this.isRunning = false;
    }

    public void addHost(Host h){
        this.activeHosts.add(h);
    }

    public void removeHost(int hostId){
        this.activeHosts.removeIf(h -> h.hostId == hostId);
    }

    @Override
    public void run(){
        this.isRunning = true;

        while(isRunning){
            for(Host host : activeHosts){
                host.tick(currentTick, pool, router);
            }
            router.processTick(pool);
            currentTick++;
            try{
                Thread.sleep(50);
            }
            catch(InterruptedException e){
                    System.out.println("Event Loop Interrupted.");
                    isRunning = false;
            }
        }
        
    }
    public Map<String, Object> getSystemSnapshot(){
        Map<String, Object> snapshot = new HashMap<>();

        snapshot.put("currentTick", currentTick);
        snapshot.put("router", router.getMetrics());

        List<Map<String, Object>> hostMetrics = new ArrayList<>();
        for(Host host : activeHosts){
            hostMetrics.add(host.getMetrics());
        }
        snapshot.put("hosts", hostMetrics);

        return snapshot;
    }
 }