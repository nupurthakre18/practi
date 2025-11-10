import java.util.Scanner;

public class Assignment3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Encryption/Decryption Algorithm =====");
        System.out.print("Enter text (UPPERCASE only): ");
        String text = sc.nextLine();

        System.out.print("Enter key (number): ");
        int key = sc.nextInt();

        String encrypted = encrypt(text, key);
        System.out.println("Encrypted Text: " + encrypted);

        String decrypted = decrypt(encrypted, key);
        System.out.println("Decrypted Text: " + decrypted);

        sc.close();
    }
    
    // Encryption using formula E = (P + key + i) % 26
    static String encrypt(String text, int key) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                int p = ch - 'A';
                int c = (p + key + i) % 26;
                result += (char) (c + 'A');
            } else {
                result += ch; // Keep spaces/symbols unchanged
            }
        }
        return result;
    }

    // Decryption using formula D = (C - key - i + 26) % 26
    static String decrypt(String text, int key) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                int c = ch - 'A';
                int p = (c - key - i + 26) % 26;
                result += (char) (p + 'A');
            } else {
                result += ch;
            }
        }
        return result;

    }
}
