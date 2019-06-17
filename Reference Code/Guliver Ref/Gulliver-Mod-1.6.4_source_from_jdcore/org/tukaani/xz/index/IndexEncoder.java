package org.tukaani.xz.index;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import org.tukaani.xz.XZIOException;
import org.tukaani.xz.common.EncoderUtil;

public class IndexEncoder
  extends IndexBase
{
  private final ArrayList records = new ArrayList();
  
  public IndexEncoder()
  {
    super(new XZIOException("XZ Stream or its Index has grown too big"));
  }
  
  public void add(long paramLong1, long paramLong2)
    throws XZIOException
  {
    super.add(paramLong1, paramLong2);
    records.add(new IndexRecord(paramLong1, paramLong2));
  }
  
  public void encode(OutputStream paramOutputStream)
    throws IOException
  {
    CRC32 localCRC32 = new CRC32();
    CheckedOutputStream localCheckedOutputStream = new CheckedOutputStream(paramOutputStream, localCRC32);
    localCheckedOutputStream.write(0);
    EncoderUtil.encodeVLI(localCheckedOutputStream, recordCount);
    Iterator localIterator = records.iterator();
    while (localIterator.hasNext())
    {
      IndexRecord localIndexRecord = (IndexRecord)localIterator.next();
      EncoderUtil.encodeVLI(localCheckedOutputStream, unpadded);
      EncoderUtil.encodeVLI(localCheckedOutputStream, uncompressed);
    }
    for (int i = getIndexPaddingSize(); i > 0; i--) {
      localCheckedOutputStream.write(0);
    }
    long l = localCRC32.getValue();
    for (int j = 0; j < 4; j++) {
      paramOutputStream.write((byte)(int)(l >>> j * 8));
    }
  }
}
