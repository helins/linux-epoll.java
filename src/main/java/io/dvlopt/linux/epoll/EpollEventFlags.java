package io.dvlopt.linux.epoll ;


import io.dvlopt.linux.epoll.EpollEventFlag ;




/**
 * A type safe container for event flags.
 *
 * @see EpollEventFlag
 */
public class EpollEventFlags {


    int flags ;




    /**
     * Builds a new container for event flags.
     */
    public EpollEventFlags() {
    
        this.flags = 0 ;
    }




    EpollEventFlags( int flags ) {
    
        this.flags = flags ;
    }




    /**
     * Is the given event flag set ?
     *
     * @param flag  The tested event flag.
     *
     * @return Whether this flag is set or not.
     */
    public boolean isSet( EpollEventFlag flag ) {
    
        return ( this.flags & flag.value ) > 0 ;
    }




    /**
     * Set the given flag.
     *
     * @param flag  The flag that needs to be set.
     *
     * @return  This EpollEventFlags instance.
     */
    public EpollEventFlags set( EpollEventFlag flag ) {
    
        this.flags |= flag.value ;

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
    
        this.flags &= ~flag.value ;

        return this ;
    }




    /**
     * Clear the event flags.
     * 
     * @return  This EpollEventFlags instance.
     */
    public EpollEventFlags clear() {
    
        this.flags = 0 ;

        return this ;
    }
}
