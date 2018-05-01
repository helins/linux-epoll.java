package io.dvlopt.linux.epoll ;


import com.sun.jna.Pointer                             ;
import io.dvlopt.linux.epoll.EpollDataType             ;
import io.dvlopt.linux.epoll.internal.NativeEpollEvent ;




public class EpollEvent {


    NativeEpollEvent nativeStruct ;


    public static final int SIZE = ( new NativeEpollEvent() ).size() ;




    public EpollEvent() {

        this.nativeStruct = new NativeEpollEvent() ;
    }


    public EpollEvent( Pointer ptr ) {
    
        this.nativeStruct = new NativeEpollEvent( ptr ) ;
    }



    public EpollEvent selectDataType( EpollDataType dataType ) {

        this.nativeStruct.data.setType( dataType.fieldType ) ;

        return this ;
    }


    public int getEvents() {
    
        return this.nativeStruct.events ;
    }


    public EpollEvent setEvents( int eventFlags ) {
    
        this.nativeStruct.events = eventFlags ;

        return this ;
    }
    

    public int getDataInt() {
    
        return this.nativeStruct.data.u32 ;
    }


    public EpollEvent setDataInt( int intValue ) {
    
        this.nativeStruct.data.u32 = intValue ;

        return this ;
    }


    public int getDataFD() {
    
        return this.nativeStruct.data.fd ;
    }


    public EpollEvent setDataFD( int fd ) {
    
        this.nativeStruct.data.fd = fd ;

        return this ;
    }


    public long getDataLong() {
    
        return this.nativeStruct.data.u64 ;
    }

    public EpollEvent setDataLong( long longValue ) {
    
        this.nativeStruct.data.u64 = longValue ;

        return this ;
    }


    public Pointer getDataPointer() {
    
        return this.nativeStruct.data.ptr ;
    }


    public EpollEvent setDataPointer( Pointer ptr ) {
    
        this.nativeStruct.data.ptr = ptr ;

        return this ;
    }
}
