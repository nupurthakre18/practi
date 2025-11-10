import java.util.Scanner;

public class Assignment4 {
    public static long modExp(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1L) == 1L) result = (result * base) % mod;
            exp >>= 1;
            base = (base * base) % mod;
        }
        return result;
    }
     
    // small helper to print a line with label and value
    private static void print(String label, long value) {
        System.out.println(label + value);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read p and g from user
        System.out.print("Enter prime modulus p: ");
        long p = sc.nextLong();
        System.out.print("Enter generator g: ");
        long g = sc.nextLong();

        // Read private keys for A, B, C
        System.out.print("Enter A's private key (a): ");
        long a = sc.nextLong();
        System.out.print("Enter B's private key (b): ");
        long b = sc.nextLong();
        System.out.print("Enter C's private key (c) [MITM]: ");
        long c = sc.nextLong();

        // Compute public keys
        long A = modExp(g, a, p);   // A's public
        long B = modExp(g, b, p);   // B's public
        long C = modExp(g, c, p);   // C's public

        System.out.println(); // spacing
        System.out.println("Choose an option:");
        System.out.println("1. Exchange Public Keys between A and B");
        System.out.println("2. Perform Man-In-The-Middle Attack by C");
        System.out.print("Enter 1 or 2: ");

        int choice = sc.nextInt();

        if (choice == 1) {
            System.out.println("\n--- Normal Diffie-Hellman Exchange ---");
            print("A's Public Key: ", A);
            print("B's Public Key: ", B);

            long S_A = modExp(B, a, p);
            long S_B = modExp(A, b, p);

            print("A computes shared secret: ", S_A);
            print("B computes shared secret: ", S_B);

            if (S_A == S_B)
                System.out.println("Secure communication established. Shared secret = " + S_A);
            else
                System.out.println("Shared secrets do not match!");
        }
        else if (choice == 2) {
            System.out.println("\n--- Man-In-The-Middle Attack by C ---");
            print("A's Public Key (original): ", A);
            print("B's Public Key (original): ", B);

            print("C (MITM) sends his Public Key to A: ", C);
            print("C (MITM) sends his Public Key to B: ", C);

            long S_A = modExp(C, a, p); // A with C's public
            long S_B = modExp(C, b, p); // B with C's public

            print("A computes shared secret (with C): ", S_A);
            print("B computes shared secret (with C): ", S_B);

            long S_CwithA = modExp(A, c, p);  // C with A
            long S_CwithB = modExp(B, c, p);  // C with B

            print("C computes shared secret with A: ", S_CwithA);
            print("C computes shared secret with B: ", S_CwithB);

            if (S_A == S_CwithA && S_B == S_CwithB)
                System.out.println("Man-In-The-Middle attack successful. C can intercept and modify messages.");
            else
                System.out.println("MITM attack failed!");
        }
        else {
            System.out.println("Invalid option! Please run again and enter 1 or 2.");
        }

        sc.close();
    }
}
