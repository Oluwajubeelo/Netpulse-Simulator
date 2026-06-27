package simulator.core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;

public class Router {
    private Queue<Packet> buffer;
    public int maxBufferSize;
    public int processingRate;
    public long totalDropped;

    public Router(int maxBufferSize, int processingRate){
        this.buffer= new LinkedList<>();
        this.maxBufferSize= maxBufferSize;
        this.processingRate= processingRate;
        this.totalDropped = 0;
    }
    public void enqueue(Packet p, Host sender, packetPool pool){
        if (buffer.size() >= maxBufferSize){
            totalDropped+=1;
            pool.returnPacket(p);
            sender.notifyDrop();
        }
        else{
            buffer.add(p);
        }
    }
    public void processTick(packetPool pool){
        int processedThisTick = 0;
        while (!buffer.isEmpty() && processedThisTick < processingRate){
            Packet p = buffer.poll();
            pool.returnPacket(p);
            processedThisTick+=1;
        }
    }
    public Map<String, Object> getMetrics(){
        return Map.of(
        "queueSize", buffer.size(),
        "maxCapacity", maxBufferSize,
        "totalDropped", totalDropped
        ); 
    }
}
