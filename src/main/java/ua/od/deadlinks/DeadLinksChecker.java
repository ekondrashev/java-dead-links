package ua.od.deadlinks;


/**
 * https://github.com/ekondrashev/java-dead-links
 */
public class DeadLinksChecker {

    public static void main(String[] args) {
        DeadLinksChecker deadLinksChecker = new DeadLinksChecker();
        if(deadLinksChecker.checkParameters(args)) System.exit(0);
        System.out.println(new UrlChecker(args[0]).checkUrls());
    }


    private boolean checkParameters(String[] args){
        boolean isParam = false;
        if(args.length == 0){
            System.out.println("Please run a program with url parameter");
            isParam = true;
        } else if(args.length > 1){
            System.out.println("Too many parameters, should be url only");
            isParam = true;
        }
        return isParam;
    }
}
