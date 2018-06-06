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
import io.dvlopt.linux.Linux             ;
import io.dvlopt.linux.epoll.EpollEvent  ;
import io.dvlopt.linux.epoll.EpollEvents ;
import io.dvlopt.linux.errno.Errno       ;
import io.dvlopt.linux.io.LinuxIO        ;
import java.io.IOException               ;
import java.util.Arrays                  ;
import java.util.List                    ;





/**
 * Class representing an epoll instance.
 * <p>
 * Quite close to the native API but wrapped in a java idiomatic interface.
 *
 * @see <a href="http://man7.org/linux/man-pages/man7/epoll.7.html">Epoll man pages</a>
 */
public class Epoll implements AutoCloseable {


    static {
    
        Native.register( "c" ) ;
    }




    // Values related to epoll operations.
    //
    private static final int EPOLL_CTL_ADD = 1 ;
    private static final int EPOLL_CTL_DEL = 2 ;
    private static final int EPOLL_CTL_MOD = 3 ;




    //
    // Private native functions.
    //

    private static native int epoll_create( int size ) ;


    private static native int epoll_ctl( int     epfd  ,
                                         int     op    ,
                                         int     fd    ,
                                         Pointer event ) ;


    private static native int epoll_wait( int     epfd      ,
                                          Pointer events    ,
                                          int     maxevents ,
                                          int     timeout   ) ;



    // Native file descriptor of an epoll instance.
    //
    private int epfd ;

    // Closing an epoll file descriptor twice is an error.
    //
    private boolean isClosed ;




    /**
     * Creates an epoll instance and allocates the needed resources.
     *
     * @throws IOException
     *           When one of these conditions occured :
     *           <ul>
     *               <li>Per-user limit on the number of epoll instances or per-process limit on the number of file descriptor has been reached.</li>
     *               <li>System-wide limit on the number of file descriptors has been reached.</li>
     *               <li>An unplanned error occured on the native side.</li>
     *           </ul>
     */
    public Epoll() throws IOException {
    
        this.epfd = epoll_create( 1 ) ;

        if ( this.epfd < 0 ) {

            int errno = Linux.getErrno() ;

            switch ( errno ) {

                case Errno.EMFILE : throw new IOException( "Per-user limit on the number of epoll instances or per-process limit on the number of file descriptor has been reached" ) ;
                
                case Errno.ENFILE : throw new IOException( "System-wide limit on the number of file descriptors has been reached" )                                                   ;

                default           : throw new IOException( "Native error while create epoll instance : errno " + errno )                                                              ;
            }
        }
    }




    /**
     * Closes this epoll instance and releases the allocated resources.
     *
     * @throws IOException
     *           When an unplanned error occured on the native side.
     */
    public void close() throws IOException {

        if ( this.isClosed == false ) {

            if ( LinuxIO.close( this.epfd ) != 0 ) {
                
                throw new IOException( "Native error while closing epoll instance : errno " + Linux.getErrno() ) ;
            }

            this.isClosed = true ;
        }
    }




    // Throws an IllegalStateException when the epoll instance is closed.
    //
    private void guardClosed() {
    
        if ( isClosed ) {
        
            throw new IllegalStateException( "Cannot perform operation on a closed epoll instance" ) ;
        }
    }




    /**
     * Retrieves the file descriptor of this epoll instance.
     * <p>
     * An epoll file descriptor can itself be monitored by another instance.
     *
     * @return  The file descriptor of this epoll instance.
     *
     * @throws  IllegalStateException
     *            When this instance has been closed.
     */
    public int getEpollFD() {

        this.guardClosed() ;
    
        return this.epfd ;
    }




    // Throws an exception when a generic problem happens during an operation.
    //
    private static void operationException( int errno ) throws IOException {
    
        switch ( errno ) {
        
            case Errno.EBADF  : throw new IllegalArgumentException( "Given file descriptor is invalid" )                ;

            case Errno.ENOMEM : throw new IOException( "Kernel has unsufficient memory for acting on file descriptor" ) ;
        }
    }




    /**
     * Starts monitoring a file descriptor for relevant events.
     * <p>
     * A file descriptor can be added only once.
     *
     * @param  fd
     *           File descriptor.
     *
     * @param  event
     *           Describing what events should be monitored.
     *
     * @return   This instance.
     *
     * @throws IllegalArgumentException
     *           When the given file descriptor is invalid, has already been added, or is the file descriptor
     *           of this epoll instance.
     *
     * @throws IllegalStateException
     *           When this epoll instance has been closed.
     *
     * @throws IOException
     *           When one of these conditions occured :
     *           <ul>
     *               <li>Limit of maximum user epoll watches has been reached.</li>
     *               <li>Kernal had unsufficient memory for this operation.</li>
     *               <li>An unplanned error occured on the native side.</li>
     *           </ul>
     *
     * @throws UnsupportedOperationException
     *           When the given file descriptor does not support epoll.
     */
    public Epoll add( int        fd    ,
                      EpollEvent event ) throws IOException {

        this.guardClosed() ;

        if ( fd == this.epfd ) {

            throw new IllegalArgumentException( "Given file descriptor cannot be the same as the file descriptor of this epoll instance" ) ;
        }

        if ( epoll_ctl( this.epfd     ,
                        EPOLL_CTL_ADD ,
                        fd            ,
                        event.ptr     ) < 0 ) {

            int errno = Linux.getErrno() ;

            operationException( errno ) ;

            switch ( errno ) {
            
                case Errno.EEXIST : throw new IllegalArgumentException( "Given file descriptor has already been added" )      ;

                case Errno.ENOSPC : throw new IOException( "Limit of maximum user epoll watches reached" )                    ;

                case Errno.EPERM  : throw new UnsupportedOperationException( "Given file descriptor does not support epoll" ) ;

                default           : throw new IOException( "Native error while adding file descriptor : errno " + errno )     ;

            }
        }

        return this ;
    }




    /**
     * Modifies how a file descriptor is monitored.
     *
     * @param   fd
     *            File descriptor.
     *
     * @param   event
     *            Describing what events should be monitored.
     *
     * @return  This instance.
     *
     * @throws  IllegalArgumentException
     *            When the given file descriptor was invalid.
     *
     * @throws  IllegalStateException
     *            When this epoll instance has been closed or the given file descriptor has not been added.
     *
     * @throws  IOException
     *            When the kernel had unsufficient memory for this operation or another unplanned error occured on
     *            the native side.
     */
    public Epoll modify( int        fd    ,
                         EpollEvent event ) throws IOException {

        this.guardClosed() ;

        if ( epoll_ctl( this.epfd     ,
                        EPOLL_CTL_MOD ,
                        fd            ,
                        event.ptr     ) < 0 ) {

            int errno = Linux.getErrno() ;

            operationException( errno ) ;

            switch ( errno ) {
            
                case Errno.ENOENT : throw new IllegalStateException( "Unable to modify file descriptor which has not been added" )      ;

                default           : throw new IOException( "Native error while modifying file descriptor properties : errno " + errno ) ;
            }
        }

        return this ;
    }




    /**
     * Stops monitoring a previously added file descriptor.
     *
     * @param   fd
     *            File descriptor.
     *
     * @return  This Epoll instance.
     *
     * @throws  IllegalArgumentException
     *            When the given file descriptor was invalid.
     *
     * @throws  IllegalStateException
     *            When this epoll instance has been closed or the given file descriptor has not been added.
     *
     * @throws  IOException
     *            When the kernel had unsufficient memory for this operation or another unplanned error occured on
     *            the native side.
     */
    public Epoll remove( int fd ) throws IOException {

        this.guardClosed() ;
    
        if ( epoll_ctl( this.epfd     ,
                        EPOLL_CTL_DEL ,
                        fd            ,
                        null          ) < 0 ) {

            int errno = Linux.getErrno() ;

            operationException( errno ) ;

            switch ( errno ) {
            
                case Errno.ENOENT : throw new IllegalStateException( "Unable to remove file descriptor which has not been added" ) ;

                default           : throw new IOException( "Native error while removing file descriptor : errno " + errno )        ;
            }
        }

        return this ;
    }




    /**
     * Waits for an event to happen.
     *
     * @param  event
     *           Will be overwritten by the kernel in order to describe what happened and give back
     *           the previously registered arbitrary long value.
     *
     * @throws IllegalStateException
     *           When this epoll instance has been closed.
     *
     * @throws IOException
     *           When an unplanned errors occured on the native side.
     *
     * @see    #wait( EpollEvent, int )
     *
     */
    public void wait( EpollEvent event ) throws IOException {
    
        this.wait( event ,
                   -1    ) ;
    }




    /**
     * Waits for an event to happen within the given timeout.
     *
     * @param   event
     *            Will be overwritten by the kernel in order to describe what happened and give back
     *            the previously registered arbitrary long value.
     *
     * @param   timeout
     *            How many milliseconds at least should we wait. A timeout of -1 will block
     *            forever until something happens.
     *
     * @return  True if an event occured within the given timeout.
     *
     * @throws  IllegalStateException
     *            When this epoll instance has been closed.
     *
     * @throws  IOException
     *            When an unplanned errors occured on the native side.
     */
    public boolean wait( EpollEvent event   ,
                         int        timeout ) throws IOException {
    
        return this.wait( event.ptr ,
                          1         ,
                          timeout   ) > 0 ;
    }




    /**
     * Waits for events to happen (at most the size of <code>events</code>).
     *
     * @param   events
     *            Will be overwritten by the kernel in order to describe what happened and give back
     *            the previously registered arbitrary long value for each file descriptor involved.
     *
     * @return  How many events occured.
     *          This function unblocks as soon as something happens but sometimes, several
     *          events can happen virtually at once.
     *
     * @throws  IllegalStateException
     *            When this epoll instance has been closed.
     *
     * @throws  IOException
     *            When an unplanned errors occured on the native side.
     *
     * @see     #wait( EpollEvents, int )
     *
     */
    public int wait( EpollEvents events ) throws IOException {
    
        return this.wait( events ,
                          -1     ) ;
    }




    /**
     * Waits for events to happen (at most the size of <code>events</code>) within the
     * given timeout.
     * <p>
     * The given EpollEvents will be filled by the kernel to describe the type of
     * event that occured and write back the user data.
     *
     * @param   events
     *            Will be overwritten by the kernel in order to describe what happened and give back
     *            the previously registered arbitrary long value for each file descriptor involved.
     *
     * @param   timeout
     *            How many milliseconds at least should we wait. A timeout of -1 will block
     *            forever until something happens.
     *
     * @return  How many events occured before the timeout elapsed.
     *          This function unblocks as soon as something happens but sometimes, several
     *          events can happen virtually at once.
     *
     * @throws  IllegalStateException
     *            When this epoll instance has been closed.
     *
     * @throws  IOException
     *            When an unplanned errors occured on the native side.
     */
    public int wait( EpollEvents events  ,
                     int         timeout ) throws IOException {
    
        return this.wait( events.memory        ,
                          events.events.length ,
                          timeout              ) ;
    }




    // Waits for an epoll event using a raw pointer.
    //
    private int wait( Pointer events    ,
                      int     maxEvents ,
                      int     timeout   ) throws IOException {

        this.guardClosed() ;

        int result = epoll_wait( this.epfd ,
                                 events    ,
                                 maxEvents ,
                                 timeout   ) ;

        if ( result < 0 ) {
            
            throw new IOException( "Native error while waiting for an epoll event : errno " + Linux.getErrno() ) ;
        }

        if ( result == 0 && timeout < 0 ) {
        
            throw new IOException( "Epoll unexpectedly unblocked before an event occured" ) ;
        }

        return result ;
    }
}
