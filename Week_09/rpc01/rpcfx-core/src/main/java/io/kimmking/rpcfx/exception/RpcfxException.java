package io.kimmking.rpcfx.exception;

public class RpcfxException extends Exception{

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(Exception e) {
        super(e);
    }
}
