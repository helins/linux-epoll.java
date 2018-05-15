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


import com.sun.jna.Memory                ;
import com.sun.jna.Native                ;
import com.sun.jna.Pointer               ;
import com.sun.jna.Structure             ;
import com.sun.jna.Union                 ;
import io.dvlopt.linux.LinuxException    ;
import io.dvlopt.linux.epoll.EpollEvent  ;
import io.dvlopt.linux.epoll.EpollEvents ;
import io.dvlopt.linux.io.LinuxIO        ;
import java.util.Arrays                  ;
import java.util.List                    ;





/**
 * Class representing an epoll instance.
 * <p>
 * It is quite close to the native API but wrapped in a java idiomatic interface.
 *
 * @see <a href="http://man7.org/linux/man-pages/man7/epoll.7.html">Epoll man pages</a>
 */
public class Epoll implements AutoCloseable {


    static {
    
        Native.register( "c" ) ;
    }




    private static final int EPOLL_CTL_ADD = 1 ;
    private static final int EPOLL_CTL_DEL = 2 ;
    private static final int EPOLL_CTL_MOD = 3 ;




    private static native int epoll_create( int size ) ;


    private static native int epoll_ctl( int     epfd  ,
                                         int     op    ,
                                         int     fd    ,
                                         Pointer event ) ;


    private static native int epoll_wait( int     epfd      ,
                                          Pointer events    ,
                                          int     maxevents ,
                                          int     timeout   ) ;



    private int     epfd     ;
    private boolean isClosed ;




    /**
     * Creates an instance and allocates the needed resources.
     *
     * @throws LinuxException   When the instance cannot be allocated.
     */
    public Epoll() throws LinuxException {
    
        this.epfd = epoll_create( 1 ) ;

        if ( this.epfd < 0 ) {
        
            throw new LinuxException( "Unable to create an epoll instance" ) ;
        }
    }




    /**
     * Closes this instance and the allocated resources.
     *
     * @throws LinuxException  In case of failure.
     */
    public void close() throws LinuxException {

        if ( this.isClosed == false ) {

            if ( LinuxIO.close( this.epfd ) != 0 ) {
                
                throw new LinuxException( "Unable to close epoll instance" ) ;
            }

            this.isClosed = true ;
        }
    }




    /**
     * Adds events to monitor for the given file descriptor.
     *
     * @param fd  File descriptor.
     *
     * @param event  Describing what events should be monitored and setting user data.
     *
     * @return This Epoll instance.
     *
     * @throws LinuxException  In case of failure.
     */
    public Epoll add( int        fd    ,
                      EpollEvent event ) throws LinuxException {

        if ( epoll_ctl( this.epfd     ,
                        EPOLL_CTL_ADD ,
                        fd            ,
                        event.ptr     ) < 0 ) {
        
            throw new LinuxException( "Unable to add epoll event" ) ;
        }

        return this ;
    }




    /**
     * Modifies the events to monitor for the given file descriptor.
     *
     * @return  This Epoll instance.
     *
     * @param fd  File descriptor.
     *
     * @param event  Describing what events should be monitored and setting user data.
     *
     * @throws LinuxException  In case of failure.
     */
    public Epoll modify( int        fd    ,
                         EpollEvent event ) throws LinuxException {

        if ( epoll_ctl( this.epfd     ,
                        EPOLL_CTL_MOD ,
                        fd            ,
                        event.ptr     ) < 0 ) {
        
            throw new LinuxException( "Unable to modify epoll event" ) ;
        }

        return this ;
    }




    /**
     * Removes this file descriptor so it is not monitored anymore.
     *
     * @param fd  File descriptor.
     *
     * @return  This Epoll instance.
     *
     * @throws LinuxException  In case of failure.
     */
    public Epoll remove( int fd ) throws LinuxException {
    
        if ( epoll_ctl( this.epfd     ,
                        EPOLL_CTL_DEL ,
                        fd            ,
                        null          ) < 0 ) {
        
            throw new LinuxException( "Unable to remove epoll event" ) ;
        }

        return this ;
    }




    /**
     * Waits for an event to happen.
     *
     * @param event  An EpollEvent that will be filled when something happens.
     *
     * @see wait( EpollEvent, int )
     *
     * @return  0 if nothing happened before unblocking, 1 if something did.
     *
     * @throws LinuxException  In case of failure.
     */
    public int wait( EpollEvent event ) throws LinuxException {
    
        return this.wait( event ,
                          -1    ) ;
    }




    /**
     * Waits for an event to happen within the given timeout.
     * <p>
     * The given EpollEvent will be filled by the kernel to describe the type of
     * event that happened and write back the user data.
     *
     * @param event  An EpollEvent that will be filled when something happens.
     *
     * @param timeout  How many milliseconds at least should we wait. A timeout of -1 will block
     *                 forever until something happens.
     *
     * @return  0 if nothing happened before the timeout elapsed, 1 if something did.
     *
     * @throws LinuxException  In case of failure.
     */
    public int wait( EpollEvent event   ,
                     int        timeout ) throws LinuxException {
    
        int result = this.wait( event.ptr ,
                                1         ,
                                timeout   ) ;

        if ( result < 0 ) throw new LinuxException( "While waiting for an epoll event" ) ;

        return result ;
    }




    /**
     * Wait for events to happen (at most the size of <code>events</code>).
     * <p>
     * The given EpollEvents will be filled by the kernel to describe the type of
     * event that happened and write back the user data.
     *
     * @param events   EpollEvents that will be filled when something happens.
     *
     * @return  Number describing how many events happened before the timeout elapsed.
     *          This function unblocks as soon as something happens but sometimes, several
     *          events can happen at once.
     *
     * @see  wait( EpollEvents, int )
     *
     * @throws LinuxException  In case of failure.
     */
    public int wait( EpollEvents events ) throws LinuxException {
    
        return this.wait( events ,
                          -1     ) ;
    }




    /**
     * Wait for events to happen (at most the size of <code>events</code>) within the
     * given timeout.
     * <p>
     * The given EpollEvents will be filled by the kernel to describe the type of
     * event that happened and write back the user data.
     *
     * @param events   EpollEvents that will be filled when something happens.
     *
     * @param timeout  How many milliseconds at least should we wait. A timeout of -1 will block
     *                 forever until something happens.
     *
     * @return  Number describing how many events happened before the timeout elapsed.
     *          This function unblocks as soon as something happens but sometimes, several
     *          events can happen at once.
     *
     * @throws LinuxException  In case of failure.
     */
    public int wait( EpollEvents events  ,
                     int         timeout ) throws LinuxException {
    
        return this.wait( events.memory        ,
                          events.events.length ,
                          timeout              ) ;
    }




    private int wait( Pointer events    ,
                      int     maxEvents ,
                      int     timeout   ) throws LinuxException {

        int result = epoll_wait( this.epfd ,
                                 events    ,
                                 maxEvents ,
                                 timeout   ) ;

        if ( result < 0 ) throw new LinuxException( "While waiting for an epoll event" ) ;

        return result ;
    }
}
