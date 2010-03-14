//
//
//
//
//

package com.changeme.ringbuffer;


/**
 * This implements an overflow policy for the RingBuffer which simply discards the tail of the buffer and adds the new
 * object.
 * 
 */
public class RBDiscardPolicy<T> implements OverflowPolicy<T>
{
    @Override
    public void addToFullBuffer(T object, RingBuffer<T> bufferData)
    {
        bufferData.remove();
        bufferData.add(object);
    }
}
