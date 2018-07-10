class Main {

    public static void main(String[] args) {
        if (args.length >= 2 && (args[1].split("="))[0].equals("--enable-http-recording")) {
            Links html = new Links.HTML(args[0], new HTTP.Default((args[1].split("--enable-http-recording="))[1]));
            System.out.println(html.toString());
        } else if (args.length > 0) {
            Links html = new Links.HTML(args[0], new HTTP.Default());
            System.out.println(html.toString());
        }
    }
}
