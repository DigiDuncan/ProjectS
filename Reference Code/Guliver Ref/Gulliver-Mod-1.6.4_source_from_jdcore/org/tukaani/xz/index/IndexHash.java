package org.tukaani.xz.index;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.CheckedInputStream;
import org.tukaani.xz.CorruptedInputException;
import org.tukaani.xz.XZIOException;
import org.tukaani.xz.check.Check;
import org.tukaani.xz.check.SHA256;
import org.tukaani.xz.common.DecoderUtil;

public class IndexHash
  extends IndexBase
{
  private Check hash;
  
  public IndexHash()
  {
    super(new CorruptedInputException());
    try
    {
      hash = new SHA256();
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      hash = new org.tukaani.xz.check.CRC32();
    }
  }
  
  public void add(long paramLong1, long paramLong2)
    throws XZIOException
  {
    super.add(paramLong1, paramLong2);
    ByteBuffer localByteBuffer = ByteBuffer.allocate(16);
    localByteBuffer.putLong(paramLong1);
    localByteBuffer.putLong(paramLong2);
    hash.update(localByteBuffer.array());
  }
  
  public void validate(InputStream paramInputStream)
    throws IOException
  {
    java.util.zip.CRC32 localCRC32 = new java.util.zip.CRC32();
    localCRC32.update(0);
    CheckedInputStream localCheckedInputStream = new CheckedInputStream(paramInputStream, localCRC32);
    long l1 = DecoderUtil.decodeVLI(localCheckedInputStream);
    if (l1 != recordCount) {
      throw new CorruptedInputException("XZ Index is corrupt");
    }
    IndexHash localIndexHash = new IndexHash();
    for (long l2 = 0L; l2 < recordCount; l2 += 1L)
    {
      long l4 = DecoderUtil.decodeVLI(localCheckedInputStream);
      long l5 = DecoderUtil.decodeVLI(localCheckedInputStream);
      try
      {
        localIndexHash.add(l4, l5);
      }
      catch (XZIOException localXZIOException)
      {
        throw new CorruptedInputException("XZ Index is corrupt");
      }
      if ((blocksSum > blocksSum) || (uncompressedSum > uncompressedSum) || (indexListSize > indexListSize)) {
        throw new CorruptedInputException("XZ Index is corrupt");
      }
    }
    if ((blocksSum != blocksSum) || (uncompressedSum != uncompressedSum) || (indexListSize != indexListSize) || (!Arrays.equals(hash.finish(), hash.finish()))) {
      throw new CorruptedInputException("XZ Index is corrupt");
    }
    DataInputStream localDataInputStream = new DataInputStream(localCheckedInputStream);
    for (int i = getIndexPaddingSize(); i > 0; i--) {
      if (localDataInputStream.readUnsignedByte() != 0) {
        throw new CorruptedInputException("XZ Index is corrupt");
      }
    }
    long l3 = localCRC32.getValue();
    for (int j = 0; j < 4; j++) {
      if ((l3 >>> j * 8 & 0xFF) != localDataInputStream.readUnsignedByte()) {
        throw new CorruptedInputException("XZ Index is corrupt");
      }
    }
  }
}
