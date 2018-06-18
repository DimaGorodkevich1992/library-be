package com.intexsoft;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
public class FileLoader {


    public static Observable getFile(URL url, int bufferSize) {
        return Observable.unsafeCreate(subscriber -> {
            byte[] buffer = new byte[bufferSize];
            try (BufferedInputStream bis = new BufferedInputStream(url.openStream(), bufferSize)) {
                int bufferLength;
                while ((bufferLength = bis.read(buffer, 0, bufferSize)) != -1) {
                    byte[] sendBuffer = Arrays.copyOfRange(buffer, 0, bufferLength);
                    subscriber.onNext(sendBuffer);
                }
            } catch (IOException e) {
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
        
    }


}








