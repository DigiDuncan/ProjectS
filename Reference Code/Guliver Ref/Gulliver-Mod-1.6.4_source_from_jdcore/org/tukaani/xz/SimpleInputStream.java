package org.tukaani.xz;

import java.io.IOException;
import java.io.InputStream;
import org.tukaani.xz.simple.SimpleFilter;

class SimpleInputStream
  extends InputStream
{
  private InputStream in;
  private final SimpleFilter simpleFilter;
  private final byte[] tmpbuf = new byte['က'];
  private int pos = 0;
  private int filtered = 0;
  private int unfiltered = 0;
  private boolean endReached = false;
  private IOException exception = null;
  
  static int getMemoryUsage()
  {
    return 5;
  }
  
  SimpleInputStream(InputStream paramInputStream, SimpleFilter paramSimpleFilter)
  {
    if (paramInputStream == null) {
      throw new NullPointerException();
    }
    assert (paramSimpleFilter == null);
    in = paramInputStream;
    simpleFilter = paramSimpleFilter;
  }
  
  public int read()
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    return read(arrayOfByte, 0, 1) == -1 ? -1 : arrayOfByte[0] & 0xFF;
  }
  
  /* Error */
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    // Byte code:
    //   0: iload_2
    //   1: iflt +21 -> 22
    //   4: iload_3
    //   5: iflt +17 -> 22
    //   8: iload_2
    //   9: iload_3
    //   10: iadd
    //   11: iflt +11 -> 22
    //   14: iload_2
    //   15: iload_3
    //   16: iadd
    //   17: aload_1
    //   18: arraylength
    //   19: if_icmple +11 -> 30
    //   22: new 8	java/lang/IndexOutOfBoundsException
    //   25: dup
    //   26: invokespecial 32	java/lang/IndexOutOfBoundsException:<init>	()V
    //   29: athrow
    //   30: iload_3
    //   31: ifne +5 -> 36
    //   34: iconst_0
    //   35: ireturn
    //   36: aload_0
    //   37: getfield 21	org/tukaani/xz/SimpleInputStream:in	Ljava/io/InputStream;
    //   40: ifnonnull +13 -> 53
    //   43: new 14	org/tukaani/xz/XZIOException
    //   46: dup
    //   47: ldc 1
    //   49: invokespecial 40	org/tukaani/xz/XZIOException:<init>	(Ljava/lang/String;)V
    //   52: athrow
    //   53: aload_0
    //   54: getfield 19	org/tukaani/xz/SimpleInputStream:exception	Ljava/io/IOException;
    //   57: ifnull +8 -> 65
    //   60: aload_0
    //   61: getfield 19	org/tukaani/xz/SimpleInputStream:exception	Ljava/io/IOException;
    //   64: athrow
    //   65: iconst_0
    //   66: istore 4
    //   68: aload_0
    //   69: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   72: iload_3
    //   73: invokestatic 33	java/lang/Math:min	(II)I
    //   76: istore 5
    //   78: aload_0
    //   79: getfield 24	org/tukaani/xz/SimpleInputStream:tmpbuf	[B
    //   82: aload_0
    //   83: getfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   86: aload_1
    //   87: iload_2
    //   88: iload 5
    //   90: invokestatic 37	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   93: aload_0
    //   94: dup
    //   95: getfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   98: iload 5
    //   100: iadd
    //   101: putfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   104: aload_0
    //   105: dup
    //   106: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   109: iload 5
    //   111: isub
    //   112: putfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   115: iload_2
    //   116: iload 5
    //   118: iadd
    //   119: istore_2
    //   120: iload_3
    //   121: iload 5
    //   123: isub
    //   124: istore_3
    //   125: iload 4
    //   127: iload 5
    //   129: iadd
    //   130: istore 4
    //   132: aload_0
    //   133: getfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   136: aload_0
    //   137: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   140: iadd
    //   141: aload_0
    //   142: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   145: iadd
    //   146: sipush 4096
    //   149: if_icmpne +33 -> 182
    //   152: aload_0
    //   153: getfield 24	org/tukaani/xz/SimpleInputStream:tmpbuf	[B
    //   156: aload_0
    //   157: getfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   160: aload_0
    //   161: getfield 24	org/tukaani/xz/SimpleInputStream:tmpbuf	[B
    //   164: iconst_0
    //   165: aload_0
    //   166: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   169: aload_0
    //   170: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   173: iadd
    //   174: invokestatic 37	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   177: aload_0
    //   178: iconst_0
    //   179: putfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   182: iload_3
    //   183: ifeq +10 -> 193
    //   186: aload_0
    //   187: getfield 18	org/tukaani/xz/SimpleInputStream:endReached	Z
    //   190: ifeq +15 -> 205
    //   193: iload 4
    //   195: ifle +8 -> 203
    //   198: iload 4
    //   200: goto +4 -> 204
    //   203: iconst_m1
    //   204: ireturn
    //   205: getstatic 16	org/tukaani/xz/SimpleInputStream:$assertionsDisabled	Z
    //   208: ifne +18 -> 226
    //   211: aload_0
    //   212: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   215: ifeq +11 -> 226
    //   218: new 5	java/lang/AssertionError
    //   221: dup
    //   222: invokespecial 29	java/lang/AssertionError:<init>	()V
    //   225: athrow
    //   226: sipush 4096
    //   229: aload_0
    //   230: getfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   233: aload_0
    //   234: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   237: iadd
    //   238: aload_0
    //   239: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   242: iadd
    //   243: isub
    //   244: istore 6
    //   246: aload_0
    //   247: getfield 21	org/tukaani/xz/SimpleInputStream:in	Ljava/io/InputStream;
    //   250: aload_0
    //   251: getfield 24	org/tukaani/xz/SimpleInputStream:tmpbuf	[B
    //   254: aload_0
    //   255: getfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   258: aload_0
    //   259: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   262: iadd
    //   263: aload_0
    //   264: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   267: iadd
    //   268: iload 6
    //   270: invokevirtual 28	java/io/InputStream:read	([BII)I
    //   273: istore 6
    //   275: iload 6
    //   277: iconst_m1
    //   278: if_icmpne +24 -> 302
    //   281: aload_0
    //   282: iconst_1
    //   283: putfield 18	org/tukaani/xz/SimpleInputStream:endReached	Z
    //   286: aload_0
    //   287: aload_0
    //   288: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   291: putfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   294: aload_0
    //   295: iconst_0
    //   296: putfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   299: goto +77 -> 376
    //   302: aload_0
    //   303: dup
    //   304: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   307: iload 6
    //   309: iadd
    //   310: putfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   313: aload_0
    //   314: aload_0
    //   315: getfield 23	org/tukaani/xz/SimpleInputStream:simpleFilter	Lorg/tukaani/xz/simple/SimpleFilter;
    //   318: aload_0
    //   319: getfield 24	org/tukaani/xz/SimpleInputStream:tmpbuf	[B
    //   322: aload_0
    //   323: getfield 22	org/tukaani/xz/SimpleInputStream:pos	I
    //   326: aload_0
    //   327: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   330: invokeinterface 41 4 0
    //   335: putfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   338: getstatic 16	org/tukaani/xz/SimpleInputStream:$assertionsDisabled	Z
    //   341: ifne +22 -> 363
    //   344: aload_0
    //   345: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   348: aload_0
    //   349: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   352: if_icmple +11 -> 363
    //   355: new 5	java/lang/AssertionError
    //   358: dup
    //   359: invokespecial 29	java/lang/AssertionError:<init>	()V
    //   362: athrow
    //   363: aload_0
    //   364: dup
    //   365: getfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   368: aload_0
    //   369: getfield 20	org/tukaani/xz/SimpleInputStream:filtered	I
    //   372: isub
    //   373: putfield 25	org/tukaani/xz/SimpleInputStream:unfiltered	I
    //   376: goto -308 -> 68
    //   379: astore 4
    //   381: aload_0
    //   382: aload 4
    //   384: putfield 19	org/tukaani/xz/SimpleInputStream:exception	Ljava/io/IOException;
    //   387: aload 4
    //   389: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	390	0	this	SimpleInputStream
    //   0	390	1	paramArrayOfByte	byte[]
    //   0	390	2	paramInt1	int
    //   0	390	3	paramInt2	int
    //   66	133	4	i	int
    //   379	9	4	localIOException	IOException
    //   76	54	5	j	int
    //   244	66	6	k	int
    // Exception table:
    //   from	to	target	type
    //   65	204	379	java/io/IOException
    //   205	379	379	java/io/IOException
  }
  
  public int available()
    throws IOException
  {
    if (in == null) {
      throw new XZIOException("Stream closed");
    }
    if (exception != null) {
      throw exception;
    }
    return filtered;
  }
  
  public void close()
    throws IOException
  {
    if (in != null) {
      try
      {
        in.close();
      }
      finally
      {
        in = null;
      }
    }
  }
}
