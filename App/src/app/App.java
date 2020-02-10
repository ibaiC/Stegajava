package app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class App {
    public static void main(String[] args) throws Exception {
        // System.out.println(TextToBinary("hello dawg"));
        // TextToBinary("hello dawg");
        // System.out.println(BinaryToText(TextToBinary("hello dawg")));
        // System.out.println(Encode(TextToBinary("hello dawg"), imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/boats.bmp")));
        // System.out.println(imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/boats.bmp"));
        ByteArrayToImage(Encode(TextToBinary("hello dawg"), imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/boats.bmp")));
        // imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/boats.bmp");
        // System.out.println("Hello Java");
        // imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/boats.bmp");
        BinaryToText(Decode(imageToBitMap("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/output.bmp")));
        // System.out.println(System.getProperty("user.dir"));
    }

    // public static Number[][] imageToBitMap(Image image){
    // return [1][0];

    // }
    public static String TextToBinary(String text) {
        byte[] bytes = text.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        // System.out.println("'" + text + "' to binary: " + binary);
        return binary.toString();
    }

    public static byte[] imageToBitMap(String ImagePath) throws IOException {

        try {
            BufferedImage originalImage = ImageIO.read(new File(ImagePath));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "bmp", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            System.out.println(Arrays.toString(Arrays.copyOfRange(imageInByte, 0, 300)));
            // System.out.println(imageInByte.length);
            return imageInByte;
        }

        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void ByteArrayToImage(byte[] bitMap) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bitMap);
        BufferedImage newImage = ImageIO.read(bis);
        ImageIO.write(newImage, "bmp", new File("/home/ibai/Documents/Uni/forensics/workspace/forensics/App/res/output.bmp"));
        System.out.println("The new image has been saved successfully! DEBUG: Currently gets saved to /res folder as output.bmp");
        // To get current directory do;
        // System.getProperty("user.dir");
    } 

    public static String BinaryToText(String binary){
        String Text = "";
        String bit;
        int charCode;

        // Pad string
        if (binary.length() % 8 != 0) {
            int numberOfZerosToAddToTheLeft = 8 -(binary.length() % 8);
            binary = "0".repeat( numberOfZerosToAddToTheLeft ) + binary;
        }

        for(int i=0; i<binary.length(); i=i+8){
            bit = binary.substring(i,i+8);
            charCode = Integer.parseInt(bit, 2);
            Text = Text + String.valueOf(Character.toChars(charCode));
        }
        System.out.println(Text);
        return Text;

    }

    public static byte[] Encode(String binary , byte[] bitMap){
            Integer length = binary.length();
            String lengthBinary = Integer.toBinaryString(length);
            int lengthBinaryLength = lengthBinary.length();
            Integer magicBytes = 54;
            String binaryAndLength = lengthBinary+binary;
            System.out.println("HERE:" + lengthBinaryLength);

            try {
                if(lengthBinaryLength<=64){
                    int numberOfZerosToAddToTheLeft = 64-lengthBinaryLength;
                    lengthBinary = "0".repeat( numberOfZerosToAddToTheLeft ) + lengthBinary;
                }
                else {
                    throw new Exception();
                };
                
            } 
            
            catch (Exception e) {
                System.out.println("message too big to encode");
            }

            for(int i=0; i < (length+lengthBinaryLength); i++){
                byte currentByte = bitMap[magicBytes+i];
                String binaryByte = Integer.toBinaryString(currentByte);

                if(binaryByte.charAt(binaryByte.length() -1) != binaryAndLength.charAt(i)){ 
                    currentByte^=1<<1;
                }
                bitMap[magicBytes+i] = currentByte;


            }
            System.out.println("Printing new byte array...");
            System.out.println(Arrays.toString(Arrays.copyOfRange(bitMap, 0, 300)));

        return bitMap;

    } 
    // public static String Decode(byte[] bitMap){

    //     int lengthBinaryLengthInBytes = 8;
    //     Integer magicBytes = 54;

    //     byte[] length = Arrays.copyOfRange(bitMap, magicBytes, magicBytes+lengthBinaryLengthInBytes);
    //     String dataBitLength = "";
    //     for (byte number : length) {
    //         dataBitLength = dataBitLength + Integer.toBinaryString(number);
    //     }
    //     int charCode = Integer.parseInt(dataBitLength, 2);
    //     System.out.println(charCode);
    //     String decodedBits = "";
    //     System.out.println(dataBitLength);

    //     // for(int i=0; i < (dataBitLength); i++){
    //     //     byte currentByte = bitMap[magicBytes+i];
    //     //     String binaryByte = Integer.toBinaryString(currentByte);
    //     //     decodedBits += binaryByte.charAt(binaryByte.length()-1);
    //     // }
    //     return decodedBits;

    // } 
    public static String Decode(byte[] bitMap){
        int lengthBinaryLengthInBytes = 8;
        Integer magicBytes = 54;

        byte[] length = Arrays.copyOfRange(bitMap, magicBytes, magicBytes+lengthBinaryLengthInBytes);
        // ByteBuffer wrapped = ByteBuffer.wrap(length); // big-endian by default
        // Integer dataBitLength = wrapped.getInt(); 
        String decodedBits = "";
        int dataBitLength = 0;
        String bit;

        for (byte b1 : length){
			String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
            System.out.println(s1);  
            

            for(int i=0; i<s1.length(); i=i+8){
                bit = s1.substring(i,i+8);
                dataBitLength += Integer.parseInt(bit, 2);
            }
        }
        System.out.println(dataBitLength);

        

        for(int i=0; i < (dataBitLength); i++){
            byte currentByte = bitMap[magicBytes+i];
            String binaryByte = Integer.toBinaryString(currentByte);
            decodedBits += binaryByte.charAt(binaryByte.length()-1);
        }
        return decodedBits;

    }

}