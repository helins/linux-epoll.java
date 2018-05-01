package io.dvlopt.linux.epoll.internal ;


import com.sun.jna.Pointer ;
import com.sun.jna.Union   ;




public class NativeEpollData extends Union {


    public Pointer    ptr ;
    public int        fd  ;
    public int        u32 ;
    public long       u64 ;
}
