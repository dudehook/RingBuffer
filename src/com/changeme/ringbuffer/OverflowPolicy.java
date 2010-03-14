//
//
//
//
//

package com.changeme.ringbuffer;


public interface OverflowPolicy<T>
{
    public void addToFullBuffer(T object, RingBuffer<T> bufferData);
}
