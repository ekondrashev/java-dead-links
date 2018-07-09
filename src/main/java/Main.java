class Main {
    public static void main(String[] args) {
        Links html;

        if ((args.length > 1) && (args[0].contains("--enable-json-recording="))) {
            String pathToFile = (args[0].split("--enable-http-recording="))[1];
            html = new Links.HTML(args[1], new HTTP.Default(pathToFile));
        } else {
            html = new Links.HTML(args[0], new HTTP.Default());
        }
        System.out.println(html.toString());
    }
}
