package io.dvlopt.linux.epoll ;


import com.sun.jna.Memory                              ;
import com.sun.jna.Pointer                             ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




/**
 * An EpollEvent specifies the type of events the kernel should monitor, or did respond to, 
 * and allows the user to store some basic data such as a file descriptor and get it back 
 * later when an event occurs.
 */
public class EpollEvent {


    Pointer ptr ;




    /**
     * Allocates a new event.
     */
    public EpollEvent() {

        this.ptr = (Pointer)( new Memory( NativeEpollEvent.SIZE ) ) ;
    }




    EpollEvent( Pointer ptr ) {

        this.ptr = ptr ;
    }




    /**
     * Retrieves the flags describing the type of events this EpollEvent is associated with.
     *
     * @return  The flags.
     */
    public int getEvents() {

        return this.ptr.getInt( NativeEpollEvent.OFFSET_EVENTS ) ;
    }




    /**
     * Sets the flags describing the type of events this EpollEvent responds to.
     *
     * @param eventFlags  Which events.
     *
     * @return  This EpollEvent.
     */
    public EpollEvent setEvents( int eventFlags ) {
    
        this.ptr.setInt( NativeEpollEvent.OFFSET_EVENTS ,
                         eventFlags                     ) ;

        return this ;
    }
    



    /**
     * Retrieves the data stored by the user.
     *
     * @return  A long value.
     */
    public long getUserData() {

        return this.ptr.getLong( NativeEpollEvent.OFFSET_USER_DATA ) ;
    }




    /**
     * Stores arbitrary user data .
     *
     * @param value  A long value.
     *
     * @return  This EpollEvent instance.
     */
    public EpollEvent setUserData( long value ) {
    
        this.ptr.setLong( NativeEpollEvent.OFFSET_USER_DATA ,
                          value                             ) ;

        return this ;
    }
}
