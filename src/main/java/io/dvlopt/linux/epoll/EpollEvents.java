/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */




package io.helins.linux.epoll ;


import com.sun.jna.Memory                              ;
import io.helins.linux.epoll.EpollEvent                ;
import io.helins.linux.epoll.internal.NativeEpollEvent ;




/**
 * Class representing several epoll events meant to be used with <strong>{@link Epoll#wait( EpollEvents ) Epoll#wait}</strong>.
 *
 * @see EpollEvent
 */
public class EpollEvents {


    // Array of instanciated epoll events mapping to native memory.
    //
    EpollEvent[] events ;

    // Internal pointer to array of native epoll events.
    //
    Memory       memory ;




    /**
     * Allocates several events in memory.
     *
     * @param  size
     *           How many events should be allocated.
     *
     * @throws IllegalArgumentException
     *           When <strong>size</strong> is less than 1.
     */
    public EpollEvents( int size ) {

        if ( size < 1 ) {
        
            throw new IllegalArgumentException( "The number of epoll events must be >= 1" ) ;
        }
    
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
     * Gets the epoll event located at `<strong>index</strong>`.
     *
     * @param   index
     *            The position of the event.
     *
     * @return  The requested EpollEvent.
     */
    public EpollEvent getEpollEvent( int index ) {
    
        return events[ index ] ;
    }
}
