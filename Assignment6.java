import java.util.Scanner;

public class Assignment6 {

    static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    static long modInverse(long e, long phi) {
        for (long i = 1; i < phi; i++) {
            if ((e * i) % phi == 1)
                return i;
        }
        return 0; 
    }

    static long modExp(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1L) == 1L)
                result = (result * base) % mod;
            exp = exp >> 1;
            base = (base * base) % mod;
        }
        return result;
    }

    static long simpleHash(String msg) {
        long hash = 0;
        for (int i = 0; i < msg.length(); i++) {
            hash = (hash + msg.charAt(i)) % 100; 
        }
        return hash;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        long p, q, n, phi, e, d;
        System.out.print("Enter two prime numbers p and q (>5): ");
        p = sc.nextLong();
        q = sc.nextLong();

        n = p * q;
        phi = (p - 1) * (q - 1);
        System.out.println("Computed n = " + n + ", phi = " + phi);

        do {
            System.out.print("Enter public exponent e (1 < e < phi, gcd(e, phi)=1): ");
            e = sc.nextLong();
            if (gcd(e, phi) != 1)
                System.out.println("Invalid e! Try again.");
        } while (gcd(e, phi) != 1);

        d = modInverse(e, phi);
        System.out.println("Private exponent (d) = " + d);

        sc.nextLine(); 
        System.out.print("\nEnter message: ");
        String message = sc.nextLine();

        long hashValue = simpleHash(message);
        System.out.println("\nMessage Hash Value: " + hashValue);

        long signature = modExp(hashValue, d, n);
        System.out.println("Digital Signature (Sender X): " + signature);

        System.out.println("\nEncrypting Message using Receiver Y's Public Key...");
        System.out.print("Ciphertext: ");
        long[] cipher = new long[message.length()];
        for (int i = 0; i < message.length(); i++) {
            cipher[i] = modExp((int) message.charAt(i), e, n);
            System.out.print(cipher[i] + " ");
        }
        System.out.println();

        System.out.println("\nDecrypting Message using Receiver Y's Private Key...");
        System.out.print("Decrypted Message: ");
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = (char) modExp(cipher[i], d, n);
            decrypted.append(ch);
        }
        System.out.println(decrypted.toString());

        System.out.println("\nVerifying Digital Signature...");
        long verifyHash = modExp(signature, e, n);         // decrypted signature -> hash from sender
        long receiverHash = simpleHash(decrypted.toString()); // hash computed by receiver

        System.out.println("Decrypted Signature (Hash from sender): " + verifyHash);
        System.out.println("Receiver's Computed Hash: " + receiverHash);

        if (verifyHash == receiverHash)
            System.out.println("\nSignature Verified! Message is Authentic, Intact, and Sender cannot deny it.");
        else
            System.out.println("\nVerification Failed! Message altered or sender not authentic.");

        sc.close();
    }
}
