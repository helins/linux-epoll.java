package io.dvlopt.linux.epoll ;




/**
 * Enum for specifying the type of data stored in an EpollEvent.
 * <p>
 * This data is set by the user and when the event occurs, the kernel will
 * give it back.
 */
public enum EpollDataType {


    INT    ( "u32" ) ,
    FD     ( "fd"  ) ,
    LONG   ( "u64" ) ,
    POINTER( "ptr" ) ;




    String fieldType ;


    private EpollDataType( String fieldType ) {
    
        this.fieldType = fieldType ;
    }
}
