package io.dvlopt.linux.epoll.internal ;


import com.sun.jna.Pointer                            ;
import com.sun.jna.Structure                          ;
import io.dvlopt.linux.epoll.internal.NativeEpollData ;
import java.util.Arrays                               ;
import java.util.List                                 ;




/**
 * This class is needed internally to describe a native data structure.
 * The user should not bother about this.
 */
public class NativeEpollEvent extends Structure {


    public int              events ;
    public NativeEpollData  data   ;


    public static final int SIZE = ( new NativeEpollEvent() ).size() ;


    public NativeEpollEvent() {}


    public NativeEpollEvent( Pointer ptr ) {
    
        super( ptr ) ;
    }


    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "events" ,
                                             "data"   } ) ;
    }
}
