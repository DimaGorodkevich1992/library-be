package com.intexsoft;

import lombok.extern.slf4j.Slf4j;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.io.*;
import java.net.URL;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        URL url = new URL("http://www.intexsoft.com/templates/intexsoft/img/common/logo.png");
        String pathFile = new File("/home/INTEXSOFT/dmitry.gorodkevich/projects/myTest/logo.png").toString();

        FileLoader.getFile(url, 2000)
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<byte[]>() {
                    private BufferedOutputStream bos = null;

                    private void closingStream(OutputStream outputStream) {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                log.warn("Stream not closed", e);
                            }
                        }
                    }

                    @Override
                    public void onStart() {
                        try {
                            bos = new BufferedOutputStream(new FileOutputStream(pathFile));
                        } catch (IOException e) {
                            log.warn("Stream can not be created");
                            onError(e);
                        }

                    }

                    @Override
                    public void onCompleted() {
                        closingStream(bos);
                        log.info("The file was uploaded");
                    }

                    @Override
                    public void onError(Throwable e) {
                        log.error("Error", e); //todo
                        closingStream(bos);
                        File file = new File(pathFile);
                        if (file.exists()) {
                            file.delete();
                        }

                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        try {
                            bos.write(bytes, 0, bytes.length);
                        } catch (IOException e) {
                            log.error("OutputStream can not write");
                            onError(e);
                        }

                    }

                });

        System.out.println("hello");
        Thread.sleep(6000);

    }
}
