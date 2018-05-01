package io.dvlopt.linux.epoll ;


import com.sun.jna.Memory                              ;
import io.dvlopt.linux.epoll.EpollDataType             ;
import io.dvlopt.linux.epoll.EpollEvent                ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




/**
 * This class represents several epoll events continuously allocated in memory.
 *
 * @see EpollEvent
 */
public class EpollEvents {


    EpollEvent[] events    ;
    Memory       allocated ;




    /**
     * Allocate several events in memory.
     *
     * @param size  How many events should be allocated.
     */
    public EpollEvents( int size ) {
    
        this.allocated = new Memory( size * NativeEpollEvent.SIZE ) ;
        this.events    = new EpollEvent[ size ]                     ;

        for ( int i = 0 ;
              i < size  ;
              i += 1    ) {
            
            events[ i ] = new EpollEvent( this.allocated.share(   i
                                                                * NativeEpollEvent.SIZE ) ) ;
        }
    }



    /**
     * Selects the user data type for all events.
     *
     * @param type  The user data type.
     *
     * @return  This EpollEvents.
     */
    public EpollEvents selectDataType( EpollDataType type ) {
    
        for ( EpollEvent event : events ) event.nativeStruct.data.setType( type.fieldType ) ;

        return this ;
    }




    /**
     * Gets the event located at <code>index</code>
     *
     * @param index  The position of the event.
     *
     * @return  The requested EpollEvent.
     */
    public EpollEvent getEpollEvent( int index ) {
    
        return events[ index ] ;
    }



    /**
     * Reads the <code>n</code> first events from the native memory.
     *
     * @param n  How many events.
     */
    void readNFirst( int n ) {

        n = Math.min( n             ,
                      events.length ) ;

        for ( int i = 0 ;
              i < n     ;
              i += 1    ) events[ i ].nativeStruct.read() ;
    }
}
