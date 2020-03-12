/*  

*/



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

        // Print menu and ask what the user wants to do
        Scanner scanning = new Scanner(System.in);
        System.out.print("Stegonography menu: ");
        System.out.println("\nType 1 to ENCODE an IMAGE\nType 2 to ENCODE TEXT\nType 3 to DECODE an IMAGE\nType 4 to DECODE TEXT");
        String choice = scanning.next();
        

        switch(choice) {
            case "1": // ENCODE IMAGE
                String secretImagePath = getUI("Path to secret image: ");
                // Get cover image path
                String coverImagePath = getUI("Path to cover image: ");
                // Get output path
                String outputPath = getUI("Path to output file: ");

                // Encode
                ByteArrayToImage(Encode(byteArrayToBinary(imageToBitMap(secretImagePath)), imageToBitMap(coverImagePath)), outputPath);
              break;
            case "2": // ENCODE TEXT
                // Get text to encode
                String secretText = getUI("Text to encode: ");
                // Get cover image
                String coverImage = getUI("Path to cover image: ");
                // Get output path
                String outPath = getUI("Path to output file: ");

                // Encode
                ByteArrayToImage(Encode(TextToBinary(secretText), imageToBitMap(coverImage)), outPath);
                break;
            case "3": // DECODE IMAGE
                // Get encoded image
                String encodedImagePath = getUI("Encoded image: ");
                // Get output path
                String otptPath = getUI("Save decoded image in: ");

                // Decode
                ByteArrayToImage(binaryToByteArray(Decode(imageToBitMap(encodedImagePath))), otptPath);
                break;

            case "4": // DECODE TEXT
                // Get encoded image
                String hiddenImage = getUI("Path to image: ");

                // Print decoded data
                BinaryToText(Decode(imageToBitMap(hiddenImage)));
                break;
            default: // If all else fails (aka user is braindead)
              System.out.println("Your input was not recognized, please type 1,2,3 or 4 and press enter to choose an encoding process.");
              main(args);
          }
        scanning.close();
    }

    // Get user input
    public static String getUI(String printMe) {
        Scanner scan = new Scanner(System.in);
        System.out.print(printMe);
        String user_input = scan.next();
        return user_input;
    }

    // Converts string of text into a binary string
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
        return binary.toString();
    }

    // Converts a BMP image at a given location to a bitMap
    public static byte[] imageToBitMap(String ImagePath) throws IOException {

        // checks to see if BMP image exists then returns byte Array, returns exception and null if not
        try {
            BufferedImage originalImage = ImageIO.read(new File(ImagePath));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "bmp", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        }

        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Converts a byte array into an image and saves it at a given path
    public static void ByteArrayToImage(byte[] bitMap, String outputPath) throws IOException {

        File file = new File(outputPath); 
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
        System.out.println("The new image has been saved successfully in "+outputPath);
    } 

    // Converts Binary string to text
    public static String BinaryToText(String binary){
        String Text = "";
        String bit;
        int charCode;

        // Pad string to ensure each character is represented as 8 bits
        if (binary.length() % 8 != 0) {
            int numberOfZerosToAddToTheLeft = 8 -(binary.length() % 8);
            binary = "0".repeat( numberOfZerosToAddToTheLeft ) + binary;
        }
        
        // Go through each 8 bit pattern and decode it to text character
        for(int i=0; i<binary.length(); i=i+8){
            bit = binary.substring(i,i+8);
            charCode = Integer.parseInt(bit, 2);
            Text = Text + String.valueOf(Character.toChars(charCode));
        }

        System.out.println("Decoded text: " + Text);
        return Text;
    }

    // Encode binary string into a byteArray using Least Significant Bit method
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

                // If the last bit is not what we want it to be, XOR it to flip it
                if(binaryByte.charAt(binaryByte.length() -1) != binaryAndLength.charAt(i)){ 
                    currentByte^=1<<0;
                }
                bitMap[magicBytes+i] = currentByte;
            }

        return bitMap;
    }

    // Get the binary string from a byte array using Least Significant Bit method
    public static String Decode(byte[] bitMap){
        int lengthBinaryLengthInBytes = 64;
        Integer magicBytes = 54;
        byte[] length = Arrays.copyOfRange(bitMap, magicBytes, magicBytes+lengthBinaryLengthInBytes);
        String decodedBits = "";
        String dataBitLength = "";

        for (byte b1 : length){
			String s1 = Integer.toBinaryString(b1);
            char lastBit = s1.charAt(s1.length()-1);
            dataBitLength += lastBit;
        }

        int bitLength=0;
        bitLength = Integer.parseInt(dataBitLength,2);

        for(int i=lengthBinaryLengthInBytes; i < (lengthBinaryLengthInBytes+bitLength); i++){
            byte currentByte = bitMap[magicBytes+i];
            String binaryByte = Integer.toBinaryString(currentByte);

            decodedBits += binaryByte.charAt(binaryByte.length()-1);
        }

        return decodedBits;
    }

    // Convert byte array to a binary string
    public static String byteArrayToBinary(byte[] bitMap) {
        String binaryString = "";
        String paddedBinary = "";

        for (byte b : bitMap) {
            // Pad string
            if (Integer.toBinaryString(b).length() % 8 != 0) {
                int numberOfZerosToAddToTheLeft = 8 -(Integer.toBinaryString(b).length() % 8);
                paddedBinary = "0".repeat( numberOfZerosToAddToTheLeft ) + Integer.toBinaryString(b);
            }
            binaryString += paddedBinary;
        }

        return binaryString;
    }

    // Convert a binary string to a byte array
    public static byte[] binaryToByteArray(String imageBinary) throws IOException {
        byte[] imageBytes = {};
        String bits;
        int v=0;
        imageBytes = new byte[imageBinary.length()/8];
        
        for(int i=0; i<imageBinary.length(); i=i+8){
            bits = imageBinary.substring(i,i+8);
            int whatParseByteWants = Integer.parseInt(bits, 2);
            byte _byte = (byte) whatParseByteWants;
            imageBytes[v] = _byte;
            v++;
        }
        
        return imageBytes;
    }

}