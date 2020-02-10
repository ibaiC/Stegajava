package app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println(TextToBinary("hello dawg"));
        // TextToBinary("hello dawg");
        System.out.println(BinaryToText(TextToBinary("hello dawg")));
        System.out.println("Hello Java");
        imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/boats.bmp");
    }

    // public static Number[][] imageToBitMap(Image image){
    //     return [1][0];

    // } 
    public static String TextToBinary(String text){
        byte[] bytes = text.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
           int val = b;
           for (int i = 0; i < 8; i++)
           {
              binary.append((val & 128) == 0 ? 0 : 1);
              val <<= 1;
           }
        }
        System.out.println("'" + text + "' to binary: " + binary);
        return binary.toString();
    }

    public static byte[] imageToBitMap(String ImagePath) throws IOException {


        try {
            BufferedImage originalImage = ImageIO.read(new File(ImagePath));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( originalImage, "bmp", baos );
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

    // public static Number[][] BitMapToImage(Number[][] bitMap){
    //     return image;

    // } 
    public static String BinaryToText(String binary){
        String Text = "";
        String bit;
        int charCode;
        for(int i=0; i<binary.length(); i=i+8){
            bit = binary.substring(i,i+8);
            charCode = Integer.parseInt(bit, 2);
            Text = Text + String.valueOf(Character.toChars(charCode));
        }
        return Text;

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