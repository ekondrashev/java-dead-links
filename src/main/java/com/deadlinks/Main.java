package com.deadlinks;

import java.io.File;

class Main {
    public static String PATHNAME = "src/main/resources/";
    public static boolean recordable = false;

    public static void main(String[] args) {
        checkInputParams(args);
        Links html = new Links.HTML(args[0], new HTTP.Default());
        System.out.println(html.toString());
    }

    private static void checkInputParams(String ... args) {
        if (args.length < 2) {
            recordable = false;
            return;
        }

        for (String arg: args) {
            String[] parts = arg.split("=");
            for (String part: parts) {
                if (part.equals("--enable-http-recording")) {
                    PATHNAME = PATHNAME + parts[1];
                }
            }
        }

        recordable = true;
        File file = new File(PATHNAME);
        if ((file).exists()) {
            file.delete();
        }
    }
}
