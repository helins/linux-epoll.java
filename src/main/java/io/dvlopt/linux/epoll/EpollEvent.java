package io.dvlopt.linux.epoll ;


import com.sun.jna.Pointer                             ;
import io.dvlopt.linux.epoll.EpollDataType             ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




/**
 * An EpollEvent specifies the type of events the kernel should monitor, or did respond to, 
 * and allows the user to store some basic data such as a file descriptor and get it back 
 * later when an event occurs.
 */
public class EpollEvent {


    NativeEpollEvent nativeStruct ;




    /**
     * In bytes, the size of a native EpollEvent.
     */
    public static final int SIZE = ( new NativeEpollEvent() ).size() ;




    /**
     * Allocates a new event.
     */
    public EpollEvent() {

        this.nativeStruct = new NativeEpollEvent() ;
    }




    /**
     * Builds an event from a pointer.
     *
     * @param ptr  Pointer.
     */
    public EpollEvent( Pointer ptr ) {
    
        this.nativeStruct = new NativeEpollEvent( ptr ) ;
    }




    /**
     * Selects the type of data the user stores for this event.
     *
     * @param dataType  The type of data.
     *
     * @return  This EpollEvent.
     */
    public EpollEvent selectDataType( EpollDataType dataType ) {

        this.nativeStruct.data.setType( dataType.fieldType ) ;

        return this ;
    }




    /**
     * Retrieves the flags describing the type of events this EpollEvent is associated with.
     *
     * @return  The flags.
     */
    public int getEvents() {
    
        return this.nativeStruct.events ;
    }




    /**
     * Sets the flags describing the type of events this EpollEvent responds to.
     *
     * @param eventFlags  Which events.
     *
     * @return  This EpollEvent.
     */
    public EpollEvent setEvents( int eventFlags ) {
    
        this.nativeStruct.events = eventFlags ;

        return this ;
    }
    



    /**
     * Retrieves user data as a 32-bit integer.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @return User int.
     */
    public int getDataInt() {
    
        return this.nativeStruct.data.u32 ;
    }




    /**
     * Sets user data as a 32-bit integer.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @param intValue  New int value.
     *
     * @return This EpollEvent.
     */
    public EpollEvent setDataInt( int intValue ) {
    
        this.nativeStruct.data.u32 = intValue ;

        return this ;
    }




    /**
     * Retrieves user data as a file descriptor.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @return User file descriptor.
     */
    public int getDataFD() {
    
        return this.nativeStruct.data.fd ;
    }




    /**
     * Sets user data as a file descriptor.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @param fd  New file descriptor.
     *
     * @return  This EpollEvent.
     */
    public EpollEvent setDataFD( int fd ) {
    
        this.nativeStruct.data.fd = fd ;

        return this ;
    }




    /**
     * Retrieves user data as a 64-bit integer.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @return  User long.
     */
    public long getDataLong() {
    
        return this.nativeStruct.data.u64 ;
    }




    /**
     * Sets user data as a 64-bit integer.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @param longValue  New long value.
     *
     * @return  This EpollEvent.
     */
    public EpollEvent setDataLong( long longValue ) {
    
        this.nativeStruct.data.u64 = longValue ;

        return this ;
    }




    /**
     * Retrieves user data as a pointer.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @return  User pointer.
     */
    public Pointer getDataPointer() {
    
        return this.nativeStruct.data.ptr ;
    }




    /**
     * Sets user data as a pointer.
     * <p>
     * Attention, <code>selectDataType</code> should be called before.
     *
     * @param ptr  New user pointer.
     *
     * @return  This EpollEvent.
     */
    public EpollEvent setDataPointer( Pointer ptr ) {
    
        this.nativeStruct.data.ptr = ptr ;

        return this ;
    }
}
