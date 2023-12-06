package com.miaoqi.netgram.nio.copyfile;

import java.io.File;

@FunctionalInterface
public interface FileCopyRunner {

    void copy(File source, File dest);

}
