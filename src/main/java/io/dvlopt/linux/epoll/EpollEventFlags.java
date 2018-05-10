package io.dvlopt.linux.epoll ;


import io.dvlopt.linux.epoll.EpollEventFlag ;




/**
 * A type safe container for event flags.
 *
 * @see EpollEventFlag
 */
public class EpollEventFlags {


    int value ;




    /**
     * Builds a new container for event flags.
     */
    public EpollEventFlags() {
    
        this.value = 0 ;
    }




    EpollEventFlags( int value ) {
    
        this.value = value ;
    }




    /**
     * Is the given event flag set ?
     *
     * @param flag  The tested event flag.
     *
     * @return Whether this flag is set or not.
     */
    public boolean isSet( EpollEventFlag flag ) {
    
        return ( this.value & flag.value ) > 0 ;
    }




    /**
     * Set the given flag.
     *
     * @param flag  The flag that needs to be set.
     *
     * @return  This EpollEventFlags instance.
     */
    public EpollEventFlags set( EpollEventFlag flag ) {
    
        this.value |= flag.value ;

        return this ;
    }




    /**
     * Unset the given flag.
     *
     * @param flag  The flag that needs to be unset.
     *
     * @return  This EpollEventFlags instance.
     */
    public EpollEventFlags unset( EpollEventFlag flag ) {
    
        this.value &= ~flag.value ;

        return this ;
    }




    /**
     * Clear the event flags.
     * 
     * @return  This EpollEventFlags instance.
     */
    public EpollEventFlags clear() {
    
        this.value = 0 ;

        return this ;
    }
}
