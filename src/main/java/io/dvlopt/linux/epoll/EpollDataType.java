package io.dvlopt.linux.epoll ;




public enum EpollDataType {


    INT    ( "u32" ) ,
    FD     ( "fd"  ) ,
    LONG   ( "u64" ) ,
    POINTER( "ptr" ) ;




    String fieldType ;


    EpollDataType( String fieldType ) {
    
        this.fieldType = fieldType ;
    }
}
