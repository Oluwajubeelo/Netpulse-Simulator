package simulator.core;

import java.util.Map;

public class Host {
    public int hostId;
    public static boolean useSlowStart = true;

    public int cwnd;
    public int ssthresh;
    public String tcpState;

    private boolean experiencedDropThisTick;

    public Host(int hostId){
        this.hostId=hostId;
        this.cwnd = 1;
        this.ssthresh = 64;
        this.tcpState = useSlowStart ? "SLOW_START" : "CONGESTION_AVOIDANCE";
        this.experiencedDropThisTick = false;
    }
    public void tick(long currentTick, packetPool pool, Router router){
        for(int i=0; i<cwnd; i+=1){
            Packet p = pool.borrowPacket(this.hostId, currentTick);
            if(p!=null){
                router.enqueue(p, this, pool);
            }
        }
        updateTCPMath();
        experiencedDropThisTick = false;
    }

    public void notifyDrop(){
        this.experiencedDropThisTick = true;
    }

    private void updateTCPMath(){
        if(experiencedDropThisTick){
            ssthresh= Math.max(cwnd/2, 2);
            cwnd= ssthresh;
            tcpState = "CONGESTION_AVOIDANCE";
        }
        else{
            if(tcpState.equals("SLOW_START")){
                // EXPONENTIAL  GROWTH (SLOW START TYPE SHII). Consistently double speed to quickly finr the network's limit
                cwnd *=2;
                if(cwnd >= ssthresh){
                    tcpState = "CONGESTION_AVOIDANCE";
                }
            }
            else{
                tcpState="CONGESTION_AVOIDANCE";
                cwnd+=1;
            }
        }
    }

    public Map<String, Object> getMetrics(){
        return Map.of(
            "hostId", hostId,
            "cwnd", cwnd,
            "state", tcpState
        );
    }
}
