package io.dvlopt.linux.epoll ;


import com.sun.jna.Memory                              ;
import io.dvlopt.linux.epoll.EpollEvent                ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




/**
 * Class representing several epoll events continuously allocated in memory.
 *
 * @see EpollEvent
 */
public class EpollEvents {


    EpollEvent[] events ;
    Memory       memory ;




    /**
     * Allocates several events in memory.
     *
     * @param size  How many events should be allocated.
     */
    public EpollEvents( int size ) {
    
        this.memory = new Memory( size * NativeEpollEvent.SIZE ) ;
        this.events = new EpollEvent[ size ]                     ;

        this.memory.clear() ;

        for ( int i = 0 ;
              i < size  ;
              i += 1    ) {
            
            events[ i ] = new EpollEvent( this.memory.share(   i
                                                             * NativeEpollEvent.SIZE ) ) ;
        }
    }




    /**
     * Gets the epoll event located at `<code>index</code>`.
     *
     * @param index  The position of the event.
     *
     * @return  The requested EpollEvent.
     */
    public EpollEvent getEpollEvent( int index ) {
    
        return events[ index ] ;
    }
}
