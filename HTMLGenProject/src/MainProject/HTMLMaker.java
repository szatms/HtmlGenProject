package MainProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTMLMaker {
    private File rootDir;
    private final String rootIndexName = "index.html";

    public HTMLMaker(File rootDir){
        this.rootDir = rootDir; //gyökérkönyvtár bekérése
    }

    public void build(){
        processDir(rootDir,0); //build metódus, nem statikus metódusokra épül, az osztályt példányosítani kell
        //relatív path-el működuk minden
    }

    private void processDir(File folder, int depth) { //a mappa feldolgozás

        List<File> directories = new ArrayList<>();
        List<File> images = new ArrayList<>();

        File[] files = folder.listFiles(); //a jelenlegi mappa fájljainak listázása
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println(file.getAbsolutePath());
                    directories.add(file);
                }
                else if (MyImage.isValid(file))
                    images.add(file);
            }
        }

        directories.sort((d1,d2) -> d1.getName().compareToIgnoreCase(d2.getName())); //mappák sorrendbe rendezése
        images.sort((f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName())); //képek sorrendbe rendezése

        generateIndexHtml(folder, directories, images, depth);

        generateImgHtml(images, depth);

        for (File directory : directories) { // rekurzió többi mappára
            processDir(directory, depth + 1);
        }
    }

    private void generateIndexHtml(File folder,List<File> directories, List<File> images, int depth) {
        StringBuilder rootPathBuilder = new StringBuilder(); //a gyökérkönyvtárhoz vezető relatív út létrehozásához
        for (int i = 0; i < depth; i++)
            rootPathBuilder.append("../");

        rootPathBuilder.append(rootIndexName); //relatív elérési út biztosítás mélységtől függően

        //index.html feje
        StringBuilder indexContent = new StringBuilder();
        indexContent.append("<!DOCTYPE html>\n<html>\n<head>\n<title>")
                .append(folder.getName())
                .append("</title>\n</head>\n<body>\n");
        //fej befejezése, törzs kezdése

        //mélységtől függő visszaugrás
        if (depth > 0){
            indexContent.append("<h1><a href=\"")
                    .append(rootPathBuilder)
                    .append("\">Start Page</a></h1>\n");
        }

        if (depth == 0) {
            // Gyökérkönyvtár egyedi tartalma, tehát itt nem kattintható a start page
            indexContent.append("<h1>Start Page</h1>\n<hr>\n");
            indexContent.append("<h2>Directories</h2>\n<ul>\n");
            for (File directory:directories){
                indexContent.append("<li><a href=\"")
                        .append(directory.getName())
                        .append("/index.html\">")
                        .append(directory.getName())
                        .append("</a></li>\n");
            }
            indexContent.append("</ul>\n");
        } else { //az összes többi index.html, a start page kattintható, a gyökérmappához visz
            indexContent.append("<hr>\n<h2>Directories</h2>\n<ul>\n");
            indexContent.append("<li><a href=\"../index.html\">&larr;</a></li>");
            for (File directory : directories) {
                indexContent.append("<li><a href=\"")
                        .append(directory.getName())
                        .append("/index.html\">")
                        .append(directory.getName())
                        .append("</a></li>\n");
            }
            indexContent.append("</ul>\n");
        }

        //Képek listázása
        indexContent.append("<hr>\n<h2>Images</h2>\n<ul>\n");
        for (File image : images) {
            String imageHtmlName = image.getName().substring(0, image.getName().lastIndexOf('.')) + ".html"; //kicseréli a kiterjesztést
            indexContent.append("<li><a href=\"")
                    .append(imageHtmlName)
                    .append("\">")
                    .append(image.getName())
                    .append("</a></li>\n");
        }
        indexContent.append("</ul>\n"); //lista vége

        indexContent.append("</body>\n</html>");
        //törzs vége

        //fájlba írás
        File htmlDone = new File(folder, rootIndexName);
        try(FileWriter writer = new FileWriter(htmlDone)){
            writer.write(indexContent.toString());
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void generateImgHtml(List<File> images, int depth){
        for (int i = 0; i < images.size(); i++) {
            File image = images.get(i);

            File previousImage = (i > 0) ? images.get(i - 1) : null; //ha a jelenlegi iteráció > 0, akkor beállít egy előző képet
            File nextImage = (i < images.size() - 1) ? images.get(i + 1) : null; //ha nem az utolsónál járunk akkor beállí egy következő képet

            StringBuilder rootPathBuilder = new StringBuilder();
            for (int d = 0; d < depth; d++)
                rootPathBuilder.append("../");
            rootPathBuilder.append(rootIndexName);

            // Az aktuális, előző és kövi képek neve .html kiterjesztéssel
            String currentHtmlName = image.getName().substring(0, image.getName().lastIndexOf('.')) + ".html";
            String previousHtmlName = (previousImage != null)
                    ? previousImage.getName().substring(0, previousImage.getName().lastIndexOf('.')) + ".html"
                    : null;
            String nextHtmlName = (nextImage != null)
                    ? nextImage.getName().substring(0, nextImage.getName().lastIndexOf('.')) + ".html"
                    : null;

            // HTML tartalom összeállítása
            StringBuilder imgContent = new StringBuilder();
            imgContent.append("<!DOCTYPE html>\n<html>\n<head>\n<title>")
                    .append(image.getName())
                    .append("</title>\n</head>\n<body>\n"); //fej vége, törzs kezdete

            imgContent.append("<p><h1><a href=\"") //vissza a kezdéshez
                    .append(rootPathBuilder)
                    .append("\">Start Page</a></h1></p>\n<hr>\n");

            imgContent.append("<a href=\"index.html\"><h3>&uarr;</h3></a>\n"); //felfelé nyíl

            // Felirat a nyilakkal
            imgContent.append("<h1>");
            if (previousImage != null) { //ha van előző kép
                imgContent.append("<a href=\"")
                        .append(previousHtmlName)
                        .append("\">&larr;</a> ");
            }
            imgContent.append(image.getName());
            if (nextImage != null) { //ha van kövi kép
                imgContent.append(" <a href=\"")
                        .append(nextHtmlName)
                        .append("\">&rarr;</a>");
            }
            imgContent.append("</h1>\n");

            // Kép megjelenítése kattinthatósággal
            if (nextImage != null) {
                imgContent.append("<a href=\"")
                        .append(nextHtmlName)
                        .append("\"><img src=\"")
                        .append(image.getName())
                        .append("\" alt=\"")
                        .append(image.getName())
                        .append("\"></a>\n");
            } else {
                // Utolsó kép: nem kattintható
                imgContent.append("<img src=\"")
                        .append(image.getName())
                        .append("\" alt=\"")
                        .append(image.getName())
                        .append("\">\n");
            }
            imgContent.append("</body>\n</html>");

            //a kép html fájlba írása
            File htmlFile = new File(image.getParentFile(), currentHtmlName);
            try(FileWriter writer = new FileWriter(htmlFile)){
                writer.write(imgContent.toString());
            } catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }
}
