package io.dvlopt.linux.epoll ;




/**
 * Enum representing the type of events that can be monitored.
 *
 * @see <a href="http://man7.org/linux/man-pages/man2/epoll_ctl.2.html">Man pages</a>
 */
public enum EpollEventFlag {


    EPOLLIN     ( 0x0001  ) , 
    EPOLLPRI    ( 0x0002  ) ,
    EPOLLOUT    ( 0x0004  ) ,
    EPOLLRDNORM ( 0x0040  ) ,
    EPOLLRDBAND ( 0x0080  ) ,
    EPOLLWRNORM ( 0x0100  ) ,
    EPOLLWRBAND ( 0x0200  ) ,
    EPOLLMSG    ( 0x0400  ) ,
    EPOLLERR    ( 0x0008  ) ,
    EPOLLHUP    ( 0x0010  ) ,
    EPOLLRDHUP  ( 0x2000  ) ,
    EPOLLWAKEUP ( 1 << 29 ) ,
    EPOLLONESHOT( 1 << 30 ) ,
    EPOLLT      ( 1 << 31 ) ;




    final int value ;




    private EpollEventFlag( int flag ) {
    
        this.value = flag ;
    }
}
