package io.dvlopt.linux.epoll ;


import com.sun.jna.Memory                              ;
import io.dvlopt.linux.epoll.EpollDataType             ;
import io.dvlopt.linux.epoll.EpollEvent                ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




public class EpollEvents {


    EpollEvent[] events    ;
    Memory       allocated ;


    public EpollEvents( int size ) {
    
        this.allocated = new Memory( size * NativeEpollEvent.SIZE ) ;
        this.events    = new EpollEvent[ size ]                     ;

        for ( int i = 0 ;
              i < size  ;
              i += 1    ) {
            
            events[ i ] = new EpollEvent( this.allocated.share(   i
                                                                * NativeEpollEvent.SIZE ) ) ;
        }
    }


    public EpollEvents selectDataType( EpollDataType type ) {
    
        for ( EpollEvent event : events ) event.nativeStruct.data.setType( type.fieldType ) ;

        return this ;
    }


    public EpollEvent getEpollEvent( int index ) {
    
        return events[ index ] ;
    }


    void readNFirst( int n ) {

        n = Math.min( n             ,
                      events.length ) ;

        for ( int i = 0 ;
              i < n     ;
              i += 1    ) events[ i ].nativeStruct.read() ;
    }
}
