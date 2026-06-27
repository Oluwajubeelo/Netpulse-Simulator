package simulator.core;
public class Packet{
    public long id;
    public int hostId;
    public long creationTick;
    public boolean isActive;

    public Packet(long id){
        this.id=id;
        this.isActive=false;
    }

    public void reset (int hostId, long creationTick){
        this.hostId=hostId;
        this.creationTick=creationTick;
        this.isActive=true;
    }
}