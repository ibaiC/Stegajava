package app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello Java");
        imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/boats.bmp");
    }

    public static byte[] imageToBitMap(String ImagePath) throws IOException {


        try {
            BufferedImage originalImage = ImageIO.read(new File(ImagePath));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( originalImage, "jpg", baos );
            baos.flush();
            byte [] imageInByte = baos.toByteArray();
            baos.close();
            System.out.println(Arrays.toString(imageInByte));
            System.out.println(imageInByte.length);
            return imageInByte;
        }   

        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    } 
    // public static String TextToBinary(String text){
    //     return "binary";

    // } 
    
    // public static Number[][] BitMapToImage(Number[][] bitMap){
    //     return image;

    // } 
    // public static String BinaryToText(String binary){
    //     return Text;

    // } 
    // public static Image Encode(String binary? , Number[][] bitMap?, Integer length){
    //     return EncodedImage;

    // } 
    // public static Image Decode(String binary? , Number[][] bitMap?, Integer length){
    //     return DecodedData;

    // } 

}