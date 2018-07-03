class Main {
    public static String recordingJson;

    public static void main(String[] args) {
        if (args.length >= 2 && (args[1].split("="))[0].equals("--enable-http-recording")) {
            recordingJson = (args[1].split("--enable-http-recording="))[1];
        }
        if (args.length > 0) {
            Links html = new Links.HTML(args[0], new HTTP.Default("D:\\"));
            System.out.println(html.toString());
        }
    }
}
