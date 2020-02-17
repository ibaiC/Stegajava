package app;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.Arrays;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class App {
    public static void main(String[] args) throws Exception {
        // System.out.println(TextToBinary("hello dawg"));
        // TextToBinary("hello dawg");
        // System.out.println(BinaryToText(TextToBinary("hello dawg")));
        // ByteArrayToImage(Encode(TextToBinary("hello dawg"), imageToBitMap("C:/Users/User/Documents/Forensics/forensics/App/res/baboon.bmp")));

        // Print menu and ask what the user wants to do
        Scanner scanning = new Scanner(System.in);
        System.out.print("Stegonography menu: ");
        System.out.println("Type 1 to ENCODE an IMAGE\nType 2 to ENCODE TEXT\nType 3 to DECODE an IMAGE\nType 4 to DECODE TEXT");
        String choice = scanning.next();
        scanning.close();

        switch(choice) {
            case "1": // ENCODE IMAGE
                // Get secret image path
                Scanner scan = new Scanner(System.in);
                System.out.print("Path to secret image: ");
                String secretImagePath = scan.next();
                scan.close();

                // Get cover image path
                Scanner scan_two = new Scanner(System.in);
                System.out.print("Path to cover image: ");
                String coverImagePath = scan_two.next();
                scan_two.close();

                // Get output path
                Scanner scan_three = new Scanner(System.in);
                System.out.print("Path to output file (include filename and extension): ");
                String outputPath = scan_three.next();
                scan_three.close();

                // Encode
                ByteArrayToImage(Encode(byteArrayToBinary(imageToBitMap(secretImagePath)), imageToBitMap(coverImagePath)));
                ByteArrayToImage(binaryToByteArray(Decode(imageToBitMap(outputPath))));
              break;
            case "2": // ENCODE TEXT
                // Get text to encode
                Scanner scan_four = new Scanner(System.in);
                System.out.print("Text to encode: ");
                String secretText = scan_four.next();
                scan_four.close();

                // Get text to encode
                Scanner scan_five = new Scanner(System.in);
                System.out.print("Text to encode: ");
                String coverImage = scan_four.next();
                scan_five.close();

                ByteArrayToImage(Encode(TextToBinary(secretText), imageToBitMap(coverImage)));
                break;
            case "3": // DECODE IMAGE

                break;

            case "4": // DECODE TEXT

                break;
            default:
              System.out.println("Your input was not recognized, please type 1 or 2 and press enter to choose an encoding process.");
              main(args);
          }

        // case image:
            // get image path

        // case text
            // ask for text input
        // ByteArrayToImage(Encode(byteArrayToBinary(imageToBitMap("E:/UNIVERSITY/Forensics/forensics/App/res/tigerSmall.bmp")), imageToBitMap("E:/UNIVERSITY/Forensics/forensics/App/res/baboon.bmp")));
        // System.out.println(byteArrayToBinary(imageToBitMap("C:/Users/User/Documents/Forensics/forensics/App/res/tigerSmall.bmp")));
        // System.out.println(Arrays.toString(imageToBitMap("C:/Users/User/Documents/Forensics/forensics/App/res/tigerSmall.bmp")));

        // ByteArrayToImage(binaryToByteArray(Decode(imageToBitMap("E:/UNIVERSITY/Forensics/forensics/App/res/outputfile.bmp"))));

        // System.out.println(BinaryToText(Decode(imageToBitMap("C:/Users/User/Desktop/outputfile.bmp"))));
    }

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
            // System.out.println(Arrays.toString(Arrays.copyOfRange(imageInByte, 0, 300)));
            // System.out.println(imageInByte.length);
            return imageInByte;
        }

        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void ByteArrayToImage(byte[] bitMap) throws IOException {
        // ByteArrayInputStream bis = new ByteArrayInputStream(bitMap);
        // BufferedImage newImage = ImageIO.read(bis);
        // ImageIO.write(newImage, "bmp", new File("C:/Users/User/Desktop/FixedOut.bmp"));

        File file = new File("E:/UNIVERSITY/Forensics/forensics/App/res/outputfile.bmp"); 
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            // Writes bytes from the specified byte array to this file output stream 
            fos.write(bitMap);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while writing file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
 
        }
        System.out.println("The new image has been saved successfully! DEBUG: Currently gets saved to /res folder as outputfile.bmp");

        // To get current directory do;
        // System.getProperty("user.dir");
    } 

    public static String BinaryToText(String binary){
        String Text = "";
        String bit;
        int charCode;

        System.out.println("binary" + binary);

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

        System.out.println("Decoded text: " + Text);
        return Text;

    }

    public static byte[] Encode(String binary , byte[] bitMap){
            Integer length = binary.length();
            String lengthBinary = Integer.toBinaryString(length);
            int lengthBinaryLength = lengthBinary.length();
            Integer magicBytes = 54;
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

            String binaryAndLength = lengthBinary+binary;
            for(int i=0; i < (length+64); i++){
                byte currentByte = bitMap[magicBytes+i];
                String binaryByte = Integer.toBinaryString(currentByte);

                if(binaryByte.charAt(binaryByte.length() -1) != binaryAndLength.charAt(i)){ 
                    currentByte^=1<<0;
                }
                bitMap[magicBytes+i] = currentByte;


            }
            System.out.println("Printing new byte array...");
            // System.out.println("UK" + Arrays.toString(Arrays.copyOfRange(bitMap, 54 , 118)));
            System.out.println("length is:" + length);
        return bitMap;

    } 
    public static String Decode(byte[] bitMap){
        int lengthBinaryLengthInBytes = 64;
        Integer magicBytes = 54;

        byte[] length = Arrays.copyOfRange(bitMap, magicBytes, magicBytes+lengthBinaryLengthInBytes);
        System.out.println("Printing array from Decode function");
        // System.out.println("BU" + Arrays.toString(length));
        String decodedBits = "";
        String dataBitLength = "";

        for (byte b1 : length){
            // System.out.println(b1);
			String s1 = Integer.toBinaryString(b1);
			// s1 += " " + Integer.toHexString(b1);
            // System.out.println(s1);
            char lastBit = s1.charAt(s1.length()-1);
            // System.out.println("France" + lastBit);
            dataBitLength += lastBit;
        }

        int bitLength=0;

        bitLength = Integer.parseInt(dataBitLength,2);

        for(int i=lengthBinaryLengthInBytes; i < (lengthBinaryLengthInBytes+bitLength); i++){
            byte currentByte = bitMap[magicBytes+i];
            String binaryByte = Integer.toBinaryString(currentByte);
            // String paddedBytebinaryByte.charAt(binaryByte.length()-1);
                    // Pad string
            
        // if (binaryByte.length() % 8 != 0) {
        //     int numberOfZerosToAddToTheLeft = 8 -(binaryByte.length() % 8);
        //     binaryByte = "0".repeat( numberOfZerosToAddToTheLeft ) + binaryByte;
        // }
            decodedBits += binaryByte.charAt(binaryByte.length()-1);
        }

        return decodedBits;

    }

    public static String byteArrayToBinary(byte[] bitMap) {
        String binaryString = "";
        String paddedBinary = "";

        for (byte b : bitMap) {
            System.out.println("hope:" + Integer.toBinaryString(b));
                    // Pad string
        if (Integer.toBinaryString(b).length() % 8 != 0) {
            int numberOfZerosToAddToTheLeft = 8 -(Integer.toBinaryString(b).length() % 8);
            paddedBinary = "0".repeat( numberOfZerosToAddToTheLeft ) + Integer.toBinaryString(b);
        }
            binaryString += paddedBinary;
        }

        return binaryString;
    }

    public static byte[] binaryToByteArray(String imageBinary) throws IOException {
        byte[] imageBytes = {};
        String bits;
        // System.out.println("yo" + imageBinary);

        // Pad string
        // if (imageBinary.length() % 8 != 0) {
        //     int numberOfZerosToAddToTheLeft = 8 -(imageBinary.length() % 8);
        //     imageBinary = "0".repeat( numberOfZerosToAddToTheLeft ) + imageBinary;
        // }

        
        imageBytes = new byte[imageBinary.length()/8];
        // System.out.println("IMAGE LENGTH: "+imageBinary.length());
        // imageBinary = "0" + imageBinary;
        int v=0;
        for(int i=0; i<imageBinary.length(); i=i+8){
            bits = imageBinary.substring(i,i+8);
            // bits = "0" + bits;
            // System.out.println("bit number " + i + ":" + bits);
            int whatParseByteWants = Integer.parseInt(bits, 2);
            // System.out.println("ParseBytewants: "+ whatParseByteWants);
            byte _byte = (byte) whatParseByteWants;
            // System.out.println("byte:" + _byte);

            imageBytes[v] = _byte;
            v++;
        }
        System.out.println(Arrays.toString(imageBytes));
        return imageBytes;
    }

}