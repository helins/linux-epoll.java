package io.dvlopt.linux.epoll ;


import com.sun.jna.Memory                ;
import com.sun.jna.Native                ;
import com.sun.jna.Pointer               ;
import com.sun.jna.Structure             ;
import com.sun.jna.Union                 ;
import io.dvlopt.linux.LinuxException    ;
import io.dvlopt.linux.io.LinuxIO        ;
import io.dvlopt.linux.epoll.EpollEvent  ;
import io.dvlopt.linux.epoll.EpollEvents ;
import java.util.Arrays                  ;
import java.util.List                    ;




public class Epoll implements AutoCloseable {


    static {
    
        Native.register( "c" ) ;
    }


    public static final int EPOLLIN      = 0x0001  ; 
    public static final int EPOLLPRI     = 0x0002  ;
    public static final int EPOLLOUT     = 0x0004  ;
    public static final int EPOLLRDNORM  = 0x0040  ;
    public static final int EPOLLRDBAND  = 0x0080  ;
    public static final int EPOLLWRNORM  = 0x0100  ;
    public static final int EPOLLWRBAND  = 0x0200  ;
    public static final int EPOLLMSG     = 0x0400  ;
    public static final int EPOLLERR     = 0x0008  ;
    public static final int EPOLLHUP     = 0x0010  ;
    public static final int EPOLLRDHUP   = 0x2000  ;
    public static final int EPOLLWAKEUP  = 1 << 29 ;
    public static final int EPOLLONESHOT = 1 << 30 ;
    public static final int EPOLLT       = 1 << 31 ;


    private static final int EPOLL_CTL_ADD = 1 ;
    private static final int EPOLL_CTL_DEL = 2 ;
    private static final int EPOLL_CTL_MOD = 3 ;




    private static native int epoll_create( int size ) ;


    private static native int epoll_ctl( int     epfd  ,
                                         int     op    ,
                                         int     fd    ,
                                         Pointer event ) ;


    private static native int epoll_wait( int     epfd      ,
                                          Pointer events    ,
                                          int     maxevents ,
                                          int     timeout   ) ;



    private int epfd ;


    public Epoll() throws LinuxException {
    
        this.epfd = epoll_create( 1 ) ;

        if ( this.epfd < 0 ) {
        
            throw new LinuxException( "Unable to create an epoll instance" ) ;
        }
    }


    public void close() throws LinuxException {

        if ( LinuxIO.close( this.epfd ) != 0 ) {
            
            throw new LinuxException( "Unable to close epoll instance" ) ;
        }
    }




    public Epoll add( int        fd    ,
                      EpollEvent event ) throws LinuxException {


        event.nativeStruct.write() ;

        if ( epoll_ctl( this.epfd                       ,
                        EPOLL_CTL_ADD                   ,
                        fd                              ,
                        event.nativeStruct.getPointer() ) < 0 ) {
        
            throw new LinuxException( "Unable to add epoll event" ) ;
        }

        return this ;
    }




    public Epoll modify( int        fd    ,
                         EpollEvent event ) throws LinuxException {

        event.nativeStruct.write() ;
    
        if ( epoll_ctl( this.epfd                       ,
                        EPOLL_CTL_MOD                   ,
                        fd                              ,
                        event.nativeStruct.getPointer() ) < 0 ) {
        
            throw new LinuxException( "Unable to modify epoll event" ) ;
        }

        return this ;
    }




    public Epoll remove( int fd ) throws LinuxException {
    
        if ( epoll_ctl( this.epfd     ,
                        EPOLL_CTL_DEL ,
                        fd            ,
                        null          ) < 0 ) {
        
            throw new LinuxException( "Unable to remove epoll event" ) ;
        }

        return this ;
    }




    public int wait( EpollEvent event ) throws LinuxException {
    
        return this.wait( event ,
                          -1    ) ;
    }


    public int wait( EpollEvent event   ,
                     int        timeout ) throws LinuxException {
    
        int result = this.wait( event.nativeStruct.getPointer() ,
                                1                               ,
                                timeout                         ) ;

        if ( result < 0 ) throw new LinuxException( "While waiting for an epoll event" ) ;

        event.nativeStruct.read() ;

        return result ;
    }


    public int wait( EpollEvents events ) throws LinuxException {
    
        return this.wait( events ,
                          -1     ) ;
    }


    public int wait( EpollEvents events  ,
                     int         timeout ) throws LinuxException {
    
        int result = this.wait( events.allocated     ,
                                events.events.length ,
                                timeout              ) ;

        if ( result < 0 ) throw new LinuxException( "While waiting for an epoll event" ) ;

        events.readNFirst( result ) ;

        return result ;
    }





    private int wait( Pointer events    ,
                      int     maxEvents ) throws LinuxException {

        return this.wait( events    ,
                          maxEvents ,
                          -1        ) ;
    }


    private int wait( Pointer events    ,
                      int     maxEvents ,
                      int     timeout   ) throws LinuxException {

        int result = epoll_wait( this.epfd ,
                                 events    ,
                                 maxEvents ,
                                 timeout   ) ;

        if ( result < 0 ) throw new LinuxException( "While waiting for an epoll event" ) ;

        return result ;
    }
}
