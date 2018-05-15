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


import io.dvlopt.linux.epoll.EpollEventFlag ;




/**
 * Class acting as a type safe container for event flags.
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
     * Sets the given flag.
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
     * Unsets the given flag.
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
     * Clears the event flags.
     * 
     * @return  This EpollEventFlags instance.
     */
    public EpollEventFlags clear() {
    
        this.value = 0 ;

        return this ;
    }
}
