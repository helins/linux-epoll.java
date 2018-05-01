package io.dvlopt.linux.epoll.internal ;


import com.sun.jna.Pointer ;
import com.sun.jna.Union   ;




/**
 * This class is needed internally to describe a native data structure.
 * The user should not bother about this.
 */
public class NativeEpollData extends Union {


    public Pointer    ptr ;
    public int        fd  ;
    public int        u32 ;
    public long       u64 ;
}
