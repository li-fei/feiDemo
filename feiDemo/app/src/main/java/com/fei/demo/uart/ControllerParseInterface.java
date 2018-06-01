package com.fei.demo.uart;

public interface ControllerParseInterface {

    byte[] toCommandData(byte[] data, int offset, int length);

}
