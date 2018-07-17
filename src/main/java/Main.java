
class Main {
    public static void main(String[] args) {
        String recordingFilePath = null;

        if (args.length >= 2 && args[1].contains("--enable-json-recording=")) {
            recordingFilePath = args[1].replace("--enable-json-recording=", "");
        }
        Links html = new Links.HTML(args[0], new HTTP.Default(recordingFilePath));
        System.out.println(html.toString());
    }
}
