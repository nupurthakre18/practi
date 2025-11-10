import java.util.Scanner;

public class Assignment2 {

    static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    static int modInverse(int e, int phi) {
        for (int d = 1; d < phi; d++) {
            if ((e * d) % phi == 1)
                return d;
        }
        return -1;
    }

    static int powerMod(int base, int exp, int mod) {
        int result = 1;
        for (int i = 0; i < exp; i++) {
            result = (result * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input prime numbers p and q
        System.out.print("Enter first prime number (p): ");
        int p = sc.nextInt();
        System.out.print("Enter second prime number (q): ");
        int q = sc.nextInt();

        // Calculate n = p * q
        int n = p * q;

        // Calculate φ(n) = (p - 1) * (q - 1)
        int phi = (p - 1) * (q - 1);

        // Input e (coprime with phi)
        System.out.print("Enter value of e (coprime with " + phi + "): ");
        int e = sc.nextInt();

        // Find d (modular inverse of e)
        int d = modInverse(e, phi);

        System.out.println("\nPublic Key (e, n): (" + e + ", " + n + ")");
        System.out.println("Private Key (d, n): (" + d + ", " + n + ")");

        // Input message (as number)
        System.out.print("\nEnter message (number only): ");
        int msg = sc.nextInt();

        // --- Encryption ---
        int cipher = powerMod(msg, e, n);
        System.out.println("\nEncrypted Message: " + cipher);

        // --- Decryption ---
        int decrypted = powerMod(cipher, d, n);
        System.out.println("Decrypted Message: " + decrypted);

        sc.close();
    }
}
