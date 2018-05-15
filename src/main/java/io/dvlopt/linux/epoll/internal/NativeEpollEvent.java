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


package io.dvlopt.linux.epoll.internal ;


import com.sun.jna.Pointer   ;
import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




/**
 * Kept public in order for JNA to work as needed, the user should not bother about it.
 */
public class NativeEpollEvent extends Structure {


    public int  events   ;
    public long userData ;


    public static final int OFFSET_EVENTS    ;
    public static final int OFFSET_USER_DATA ;
    public static final int SIZE             ;




    static {
    
        NativeEpollEvent event = new NativeEpollEvent() ;

        OFFSET_EVENTS    = event.fieldOffset( "events" )   ;
        OFFSET_USER_DATA = event.fieldOffset( "userData" ) ;
        SIZE             = event.size()                    ;
    }




    public NativeEpollEvent() {}




    public NativeEpollEvent( Pointer ptr ) {
    
        super( ptr ) ;
    }




    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "events"   ,
                                             "userData" } ) ;
    }
}
