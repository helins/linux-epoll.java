/*
 * Copyright 2018 Adam Helinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.dvlopt.linux.epoll ;


import com.sun.jna.Memory                              ;
import io.dvlopt.linux.epoll.EpollEvent                ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




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
