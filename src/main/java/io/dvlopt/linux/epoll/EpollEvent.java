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
import com.sun.jna.Pointer                             ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




/**
 * Class holding flags about what kind of events should be monitored (or later on, what occured) as well as an arbitrary
 * long which often describes a file descriptor or some kind of marker.
 * <p>
 * When a file descriptor is to be monitored for one or several possibles events, this class is used with
 * <strong>{@link Epoll#add( int, EpollEvent) Epoll#add}</strong>. Then, for monitoring, a new or reused instance is given
 * to <strong>{@link Epoll#wait( EpollEvent ) Epoll#wait}</strong>. When a relevant event happens, the kernel uses this
 * instance to describe what happened by setting the flags and copies back the arbitrary long value associated with the file
 * descriptor.
 */
public class EpollEvent {



    //
    // Related to event flags.
    //


    /**
     * Enum representing events that epoll can monitor.
     *
     * @see <a href="http://man7.org/linux/man-pages/man2/epoll_ctl.2.html">Man pages</a>
     */
    public static enum Flag {


        /**
         * Available for read operations.
         */
        EPOLLIN     ( 0x0001  ) ,

        /**
         * Urgent data for read.
         */
        EPOLLPRI    ( 0x0002  ) ,

        /**
         * Available for write operations.
         */
        EPOLLOUT    ( 0x0004  ) ,

        /**
         * Priority data can be read.
         */
        EPOLLRDBAND ( 0x0080  ) ,

        /**
         * Priority data can be written.
         */
        EPOLLWRBAND ( 0x0200  ) ,

        /**
         * Error condition.
         * <p>
         * Always monitored even if not set explicitly.
         */
        EPOLLERR    ( 0x0008  ) ,

        /**
         * Hang up, peer closed the channel.
         * <p>
         * Always monitored even if not set explicitly.
         */
        EPOLLHUP    ( 0x0010  ) ,

        /**
         * Read hang up, peer closed its side of the channel for reading but can still receive data.
         */
        EPOLLRDHUP  ( 0x2000  ) ,

        /**
         * Prevents hibernation, only to be used when really knowing what it does.
         */
        EPOLLWAKEUP ( 1 << 29 ) ,

        /**
         * One-shot behavior, meaning that after an event happens, it will not be monitored anymore.
         * <p>
         * The user can reactive it by calling <strong>{@link Epoll#add( int, EpollEvent ) Epoll#add}</strong> with the related
         * file descriptor.
         */
        EPOLLONESHOT( 1 << 30 ) ,

        /**
         * Edge Triggered behavior, only to be used when really knowing what it does.
         */
        EPOLLT      ( 1 << 31 ) ;




        // Internal flag value.
        //
        final int value ;




        // Private constructor assigning the flag value.
        //
        private Flag( int flag ) {
        
            this.value = flag ;
        }
    }








    /**
     * Class acting as a type safe container for event flags.
     *
     * @see EpollEvent.Flag
     */
    public static class Flags {


        // Internal value holding flags.
        //
        int value ;




        /**
         * Builds a new container for event flags.
         */
        public Flags() {
        
            this.value = 0 ;
        }




        // Package private constructor.
        //
        Flags( int value ) {
        
            this.value = value ;
        }




        /**
         * Is the given event flag set ?
         *
         * @param  flag
         *           The tested event flag.
         *
         * @return True if this flag is set.
         */
        public boolean isSet( Flag flag ) {
        
            return ( this.value & flag.value ) > 0 ;
        }




        /**
         * Sets the given flag.
         *
         * @param   flag
         *            The flag that needs to be set.
         *
         * @return  This instance.
         */
        public Flags set( Flag flag ) {
        
            this.value |= flag.value ;

            return this ;
        }




        /**
         * Unsets the given flag.
         *
         * @param   flag
         *            The flag that needs to be unset.
         *
         * @return  This instance.
         */
        public Flags unset( Flag flag ) {
        
            this.value &= ~flag.value ;

            return this ;
        }




        /**
         * Clears the event flags.
         * 
         * @return  This instance.
         */
        public Flags clear() {
        
            this.value = 0 ;

            return this ;
        }
    }








    //
    // Directly related to EpollEvent.
    //




    // Internal pointer to native structure.
    //
    Pointer ptr ;




    /**
     * Allocates a new instance.
     */
    public EpollEvent() {

        Memory memory = new Memory( NativeEpollEvent.SIZE ) ;

        memory.clear() ;

        this.ptr = (Pointer)memory ;
    }




    // Package private constructor from a pointer.
    //
    EpollEvent( Pointer ptr ) {

        this.ptr = ptr ;
    }




    /**
     * Retrieves the flags describing the type of events this instance is associated with.
     *
     * @return  The flags.
     */
    public Flags getFlags() {

        return new Flags( this.ptr.getInt( NativeEpollEvent.OFFSET_EVENTS ) ) ;
    }




    /**
     * Sets the flags describing the type of events this instance responds to.
     *
     * @param   flags
     *            Which events.
     *
     * @return  This instance.
     */
    public EpollEvent setFlags( Flags flags ) {

        this.ptr.setInt( NativeEpollEvent.OFFSET_EVENTS ,
                         flags.value                    ) ;
    
        return this ;
    }




    /**
     * Retrieves the arbitrary data stored by the user.
     *
     * @return  A long value.
     */
    public long getUserData() {

        return this.ptr.getLong( NativeEpollEvent.OFFSET_USER_DATA ) ;
    }




    /**
     * Stores arbitrary user data .
     *
     * @param   value
     *            Long value.
     *
     * @return  This instance.
     */
    public EpollEvent setUserData( long value ) {
    
        this.ptr.setLong( NativeEpollEvent.OFFSET_USER_DATA ,
                          value                             ) ;

        return this ;
    }
}
