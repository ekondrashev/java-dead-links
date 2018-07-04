
class Main {
    public static boolean enableRecordingJson = true;
    public static String JsonRecordingFileName;

    public static void main(String[] args) {
        if (args.length > 1 && args[1].contains("--enable-json-recording=")) {
            enableRecordingJson = true;
            JsonRecordingFileName = args[1].substring(args[1].indexOf("=") + 1);
        }
        Links html = new Links.HTML(args[0], new HTTP.Default());
        System.out.println(html.toString());
    }
}
