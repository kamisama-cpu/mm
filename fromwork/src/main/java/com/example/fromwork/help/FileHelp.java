package com.example.fromwork.help;


/**
 * 文件帮助类
 */
public class FileHelp {

    private static FileHelp instance;

    public FileHelp() {
    }

    public static FileHelp getInstance(){
        if (instance==null){
            synchronized (FileHelp.class){
                if (instance ==null){
                    instance = new FileHelp();
                }
            }
        }
        return instance;
    }


}
