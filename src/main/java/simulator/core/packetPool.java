package simulator.core;

public class packetPool {
    private Packet[] pool;
    public final int MAX_POOL_SIZE;

    private int availableCount;

    public packetPool(int size){
        this.MAX_POOL_SIZE=size;
        this.pool=new Packet[size];
        this.availableCount=size;

        for(int i=0; i<size; i+=1){
            pool[i]= new Packet(i);
        }
    }
    public Packet borrowPacket(int hostId, long currentTick){
        if(availableCount <=0){
            return null;
        }
        availableCount--;
        Packet p = pool[availableCount];

        p.reset(hostId, currentTick);
        return p;
    }
    public void returnPacket(Packet p){
        if (p != null && p.isActive){
            p.isActive = false;

            pool[availableCount]=p;
            availableCount++;
        }
    }
}
