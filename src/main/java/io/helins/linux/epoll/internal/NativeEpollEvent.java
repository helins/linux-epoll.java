/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */




package io.helins.linux.epoll.internal ;


import com.sun.jna.Pointer   ;
import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




/**
 * Kept public in order for JNA to work as needed, the user should not bother about it.
 */
public class NativeEpollEvent extends Structure {


    // Fields of the native structure.
    //
    public int  events   ;
    public long userData ;


    // Offsets of fields in the native structure.
    //
    public static final int OFFSET_EVENTS    ;
    public static final int OFFSET_USER_DATA ;
    public static final int SIZE             ;




    // Retrieves offsets of native fields.
    //
    static {
    
        NativeEpollEvent event = new NativeEpollEvent() ;

        OFFSET_EVENTS    = event.fieldOffset( "events" )   ;
        OFFSET_USER_DATA = event.fieldOffset( "userData" ) ;
        SIZE             = event.size()                    ;
    }




    // Public constructor.
    //
    public NativeEpollEvent() {}




    // Public constructor for instanciating from a pointer.
    //
    public NativeEpollEvent( Pointer ptr ) {
    
        super( ptr ) ;
    }




    // Needed for JNA.
    //
    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "events"   ,
                                             "userData" } ) ;
    }
}
