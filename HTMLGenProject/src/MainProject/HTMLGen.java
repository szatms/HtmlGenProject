package MainProject;

import java.io.File;

public class HTMLGen {
    public static void main(String[] args) {
        //hibakezelés:
        Info myInfo = new Info(); //nem statikus metódus meghívásához
        if (args.length != 1){
            myInfo.getInfo();         //nem statikus metódus
            System.exit(1);
        }
        else if (args[0].contentEquals("-i") || args[0].contentEquals("--info")){
            myInfo.getInfo();         //nem statikus metódus
            System.exit(0);
        }

        File rootDir = new File(args[0]);
        if (!isValidFileAccess(rootDir)){
            System.err.println("Adjon meg egy érvényes elérési utat!");
            System.exit(2);
        }

        //A html generátor példányosítás, build metódus meghívása
        HTMLMaker maker = new HTMLMaker(rootDir);
        maker.build();
        System.out.println("\nAll folders navigated successfully!");
    }

    public static boolean isValidFileAccess(File filePath){
        return filePath.exists() && filePath.isDirectory();
    }
}