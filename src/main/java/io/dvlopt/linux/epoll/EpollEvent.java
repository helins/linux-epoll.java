package io.dvlopt.linux.epoll ;


import com.sun.jna.Memory                              ;
import com.sun.jna.Pointer                             ;
import io.dvlopt.linux.epoll.EpollEventFlags           ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




/**
 * Class representing the type of events the kernel should monitor and storing some user data.
 * <p>
 * The data will be given back by the kernel when an event happens so the user can somehow identify it.
 */
public class EpollEvent {


    Pointer ptr ;




    /**
     * Allocates a new event.
     */
    public EpollEvent() {

        Memory memory = new Memory( NativeEpollEvent.SIZE ) ;

        memory.clear() ;

        this.ptr = (Pointer)memory ;
    }




    EpollEvent( Pointer ptr ) {

        this.ptr = ptr ;
    }




    /**
     * Retrieves the flags describing the type of events this EpollEvent is associated with.
     *
     * @return  The flags.
     */
    public EpollEventFlags getEventFlags() {

        return new EpollEventFlags( this.ptr.getInt( NativeEpollEvent.OFFSET_EVENTS ) ) ;
    }




    /**
     * Sets the flags describing the type of events this EpollEvent responds to.
     *
     * @param flags  Which events.
     *
     * @return  This EpollEvent.
     */
    public EpollEvent setEventFlags( EpollEventFlags flags ) {

        this.ptr.setInt( NativeEpollEvent.OFFSET_EVENTS ,
                         flags.value                    ) ;
    
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
