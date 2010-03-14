//
//
//
//
//

package com.changeme.ringbuffer;

import java.util.Iterator;

import junit.framework.TestCase;


public class RingBufferTest extends TestCase
{
    public void testAddRemove() throws Exception
    {
        RingBuffer<Integer> buffer = new RingBuffer<Integer>(10, new RBDiscardPolicy<Integer>());
        buffer.add(1);
        assertTrue(buffer.count() == 1);
        buffer.add(2);
        assertTrue(buffer.count() == 2);
        buffer.add(3);
        assertTrue(buffer.count() == 3);
        buffer.add(4);
        assertTrue(buffer.count() == 4);
        buffer.add(5);
        assertTrue(buffer.count() == 5);
        buffer.add(6);
        assertTrue(buffer.count() == 6);
        buffer.add(7);
        assertTrue(buffer.count() == 7);
        buffer.add(8);
        assertTrue(buffer.count() == 8);
        buffer.add(9);
        assertTrue(buffer.count() == 9);
        buffer.add(10);
        assertTrue(buffer.count() == 10);

        buffer.add(11);
        assertTrue(buffer.count() == 10);

        // buffer should contain 2-11
        Integer remove = buffer.remove();
        assertTrue(remove == 2);
        assertTrue(buffer.count() == 9);
        remove = buffer.remove();
        assertTrue(remove == 3);
        assertTrue(buffer.count() == 8);
        remove = buffer.remove();
        assertTrue(remove == 4);
        assertTrue(buffer.count() == 7);
        remove = buffer.remove();
        assertTrue(remove == 5);
        assertTrue(buffer.count() == 6);
        remove = buffer.remove();
        assertTrue(remove == 6);
        assertTrue(buffer.count() == 5);
        remove = buffer.remove();
        assertTrue(remove == 7);
        assertTrue(buffer.count() == 4);
        remove = buffer.remove();
        assertTrue(remove == 8);
        assertTrue(buffer.count() == 3);
        remove = buffer.remove();
        assertTrue(remove == 9);
        assertTrue(buffer.count() == 2);
        remove = buffer.remove();
        assertTrue(remove == 10);
        assertTrue(buffer.count() == 1);
        remove = buffer.remove();
        assertTrue(remove == 11);
        assertTrue(buffer.count() == 0);
        assertTrue(buffer.isEmpty());
    }

    public void testWrapping() throws Exception
    {
        RingBuffer<Integer> buffer = new RingBuffer<Integer>(10, new RBDiscardPolicy<Integer>());

        for (int i = 0; i < 10; i++)
        {
            buffer.add(i);
        }
        assertTrue(buffer.count() == 10);
        assertTrue(buffer.tail() == 0);
        assertTrue(buffer.head() == 10);

        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();

        assertTrue(buffer.tail() == 5);
        assertTrue(buffer.head() == 10);
        assertTrue(buffer.count() == 5);

        buffer.add(666);
        buffer.add(666);
        buffer.add(666);

        assertTrue(buffer.tail() == 5);
        assertTrue(buffer.head() == 3);
        assertTrue(buffer.count() == 8);

        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();

        assertTrue(buffer.tail() == 0);
        assertTrue(buffer.head() == 3);
        assertTrue(buffer.count() == 3);

        assertTrue(buffer.remove() == 666);
        assertTrue(buffer.remove() == 666);
        assertTrue(buffer.remove() == 666);

        assertTrue(buffer.tail() == 3);
        assertTrue(buffer.head() == 3);
        assertTrue(buffer.isEmpty());
    }

    public void testSimpleIteration() throws Exception
    {
        RingBuffer<Integer> buffer = setupBuffer(8);

        Iterator<Integer> iterator = buffer.iterator();
        for (int i = 0; i < 8; i++)
        {
            assertTrue(iterator.hasNext());
            assertTrue(iterator.next() == i);
            System.out.print("" + i + "... ");
        }
        System.out.println();
        assertFalse(iterator.hasNext());
    }

    public void testWrappingIteration() throws Exception
    {
        RingBuffer<Integer> buffer = setupBuffer(8);

        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.add(8);
        buffer.add(9);
        buffer.add(10);

        assertTrue(buffer.count() == 7);
        assertTrue(buffer.tail() == 4);
        assertTrue(buffer.head() == 3);

        Iterator<Integer> iterator = buffer.iterator();
        for (int i = 0; i < 7; i++)
        {
            assertTrue(iterator.hasNext());
            assertTrue(iterator.next() == i + 4);
            System.out.print("" + (i + 4) + "... ");
        }
        System.out.println();
        assertFalse(iterator.hasNext());

        buffer.add(11);
        assertTrue(buffer.count() == 8);
        assertTrue(buffer.tail() == 4);
        assertTrue(buffer.head() == 4);

        iterator = buffer.iterator();
        for (int i = 0; i < 8; i++)
        {
            assertTrue(iterator.hasNext());
            assertTrue(iterator.next() == i + 4);
            System.out.print("" + (i + 4) + "... ");
        }
        System.out.println();
        assertFalse(iterator.hasNext());
    }

    private RingBuffer<Integer> setupBuffer(int size)
    {
        RingBuffer<Integer> buffer = new RingBuffer<Integer>(size, new RBDiscardPolicy<Integer>());

        for (int i = 0; i < size; i++)
        {
            buffer.add(i);
        }
        assertTrue(buffer.count() == size);
        assertTrue(buffer.tail() == 0);
        assertTrue(buffer.head() == size);
        return buffer;
    }

    public void testHasNext() throws Exception
    {
        RingBuffer<Integer> buffer = setupBuffer(10);

        Iterator<Integer> iterator = buffer.iterator();
        assertTrue(iterator.hasNext());
        for (int i = 0; i < 10; i++)
        {
            assertTrue(iterator.next() == i);
            if (i % 2 == 0)
            {
                assertTrue(iterator.hasNext());
            }
        }
        assertFalse(iterator.hasNext());
    }

    public void testAllIterations() throws Exception
    {
        RingBuffer<Integer> buffer = setupBuffer(10);

        Iterator<Integer> iterator = buffer.iterator();

        // ** M1 **
        iterator.next(); // 0
        iterator.next(); // 1
        iterator.next(); // 2
        iterator.next(); // 3
        iterator.next(); // 4
        iterator.next(); // 5
        iterator.next(); // 6

        // iterator points at 7th element
        // now change the mode from M1 to M2L
        buffer.remove(); // 0
        buffer.remove(); // 1
        buffer.remove(); // 2
        buffer.remove(); // 3
        buffer.remove(); // 4
        buffer.add(10);
        buffer.add(11);
        buffer.add(12);
        // Now M2R
        assertTrue(buffer.count() == 8);
        assertTrue(buffer.tail() == 5);
        assertTrue(buffer.head() == 3);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() == 7);
        assertTrue(iterator.next() == 8);
        assertTrue(iterator.next() == 9);
        // Now M2L
        assertTrue(iterator.next() == 10);
        assertTrue(iterator.next() == 11);
        assertTrue(iterator.next() == 12);

        // Now at the end
        assertFalse(iterator.hasNext());

        // Adding to the end
        buffer.add(13);
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() == 13);
        assertFalse(iterator.hasNext());
        buffer.add(14);
        assertTrue(iterator.hasNext());

        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();
        // M1 now, next is at index 4
        buffer.remove();

        assertTrue(buffer.count() == 4);
        assertTrue(buffer.tail() == 1);
        assertTrue(buffer.head() == 5);

        buffer.add(15);
        buffer.add(16);
        buffer.add(17);
        buffer.add(18);

        assertTrue(iterator.next() == 14);
        assertTrue(iterator.next() == 15);
        assertTrue(iterator.next() == 16);
        assertTrue(iterator.next() == 17);
        assertTrue(iterator.next() == 18);
        assertNull(iterator.next());
    }

    public void testOverflowIteration() throws Exception
    {
        RingBuffer<Integer> buffer = setupBuffer(8);

        buffer.remove();
        buffer.remove();
        buffer.remove();
        buffer.remove();

        assertTrue(buffer.count() == 4);
        assertTrue(buffer.tail() == 4);
        assertTrue("head = " + buffer.head(), buffer.head() == 8);

        Iterator<Integer> iterator = buffer.iterator();
        iterator.next();
        iterator.next(); // next is now at slot 6

        buffer.add(8); // 0
        buffer.add(9); // 1
        buffer.add(10);
        buffer.add(11);
        buffer.add(12);
        buffer.add(13);
        buffer.add(14);

        assertTrue(buffer.count() == 8);
        assertTrue(buffer.tail() == 7);
        assertTrue("head = " + buffer.head(), buffer.head() == 7);
        Integer next = iterator.next();
        assertTrue("next = " + next, next == 14);
    }
}
