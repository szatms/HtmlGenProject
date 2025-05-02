package MainProject;

import java.io.File;

public class MyImage {
    //Supported media: jpeg, jpg, png, GIF
    public MyImage(){
        //Empty constructor
    }

    public static boolean isValid(File f){
        boolean result = false;

        if (f.getAbsolutePath().toLowerCase().endsWith(".jpg"))
            result=true;
        else if (f.getAbsolutePath().toLowerCase().endsWith(".jpeg"))
            result=true;
        else if (f.getAbsolutePath().toLowerCase().endsWith(".png"))
            result = true;
        else if (f.getAbsolutePath().toLowerCase().endsWith(".gif"))
            result = true;

        return result;
    }
}
