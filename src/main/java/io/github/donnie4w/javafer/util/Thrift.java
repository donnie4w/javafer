package io.github.donnie4w.javafer.util;

import org.apache.thrift.TException;
import org.apache.thrift.TSerializable;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.jetbrains.annotations.NotNull;

public class Thrift {
    public static byte[] encode(@NotNull TSerializable ts) throws TException {
        TMemoryBuffer tmb = new TMemoryBuffer(0);
        ts.write(new TCompactProtocol(tmb));
        return tmb.getArray();
    }

    public static <T extends TSerializable> T decode(@NotNull byte[] bs, T ts) throws TException {
        TMemoryBuffer tmb = new TMemoryBuffer(1024);
        tmb.write(bs);
        ts.read(new TCompactProtocol(tmb));
        return ts;
    }

}
