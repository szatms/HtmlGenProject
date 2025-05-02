package MainProject;

public class Info {
    public Info(){
        //Empty constructor
    }

    public void getInfo(){
        System.out.println("Image to HTML generator");
        System.out.println("To access info panel give argument '-i' or '--info'");
        System.out.println("Currently supported image formats are: jpeg, jpg, png, GIF");
        System.out.println("Current version: 0.1");
        System.out.println("\nHow to use: give the program a valid folder as an argument.");
        System.out.println("The program is going to convert all the supported image files into HTML format withing the folder and all of it's subfolders.");
    }
}
