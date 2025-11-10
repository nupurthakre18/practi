import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Assignment5 {

    // rotate-left 32-bit
    private static int rotl(int x, int n) { return (x << n) | (x >>> (32 - n)); }

    // Manual SHA-1 implementation (returns 40-hex lowercase)
    public static String sha1(String input) {
        byte[] data = input.getBytes(StandardCharsets.UTF_8);
        long bitLen = (long) data.length * 8L;
        int padLen = (int) ((56 - (data.length + 1) % 64 + 64) % 64);
        int total = data.length + 1 + padLen + 8;
        byte[] buf = new byte[total];
        System.arraycopy(data, 0, buf, 0, data.length);
        buf[data.length] = (byte) 0x80;
        for (int i = 0; i < 8; i++) buf[total - 1 - i] = (byte) ((bitLen >>> (8 * i)) & 0xFF);

        int h0 = 0x67452301, h1 = 0xEFCDAB89, h2 = 0x98BADCFE, h3 = 0x10325476, h4 = 0xC3D2E1F0;

        for (int b = 0; b < buf.length; b += 64) {
            int[] w = new int[80];
            for (int i = 0; i < 16; i++) {
                int j = b + i * 4;
                w[i] = ((buf[j] & 0xFF) << 24) | ((buf[j + 1] & 0xFF) << 16)
                     | ((buf[j + 2] & 0xFF) << 8) | (buf[j + 3] & 0xFF);
            }
            for (int i = 16; i < 80; i++) w[i] = rotl(w[i-3] ^ w[i-8] ^ w[i-14] ^ w[i-16], 1);

            int a = h0, b1 = h1, c = h2, d = h3, e = h4;
            for (int i = 0; i < 80; i++) {
                int f, k;
                if (i < 20) { f = (b1 & c) | ((~b1) & d); k = 0x5A827999; }
                else if (i < 40) { f = b1 ^ c ^ d; k = 0x6ED9EBA1; }
                else if (i < 60) { f = (b1 & c) | (b1 & d) | (c & d); k = 0x8F1BBCDC; }
                else { f = b1 ^ c ^ d; k = 0xCA62C1D6; }
                int tmp = rotl(a,5) + f + e + k + w[i];
                e = d; d = c; c = rotl(b1,30); b1 = a; a = tmp;
            }
            h0 += a; h1 += b1; h2 += c; h3 += d; h4 += e;
        }

        byte[] dig = new byte[20];
        for (int i = 0; i < 4; i++) dig[i] = (byte)((h0 >>> (24 - i*8)) & 0xFF);
        for (int i = 0; i < 4; i++) dig[4+i] = (byte)((h1 >>> (24 - i*8)) & 0xFF);
        for (int i = 0; i < 4; i++) dig[8+i] = (byte)((h2 >>> (24 - i*8)) & 0xFF);
        for (int i = 0; i < 4; i++) dig[12+i] = (byte)((h3 >>> (24 - i*8)) & 0xFF);
        for (int i = 0; i < 4; i++) dig[16+i] = (byte)((h4 >>> (24 - i*8)) & 0xFF);

        StringBuilder sb = new StringBuilder(40);
        for (byte by : dig) sb.append(String.format("%02x", by & 0xFF));
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== SHA-1 Transmission Simulation (User1 → User2) ===");

        // User1: prepare message and its SHA-1
        System.out.print("\nUser1: Enter message to send: ");
        String user1Message = sc.nextLine();
        String user1Hash = sha1(user1Message);

        System.out.println("\n[User1] Message: " + user1Message);
        System.out.println("[User1] SHA-1: " + user1Hash);

        System.out.println("\n[Network] Sending message and hash to User2...");

        // User2: receive message and claimed hash (simulation)
        // Allowing User2 to enter the received message (can be same or altered)
        System.out.print("\nUser2: Enter the received message: ");
        String user2Received = sc.nextLine();

        System.out.println("\n[User2] Received (claimed) SHA-1 from network: " + user1Hash);
        String recomputed = sha1(user2Received);
        System.out.println("[User2] Recomputed SHA-1 of received message: " + recomputed);

        if (recomputed.equals(user1Hash)) {
            System.out.println("\nHash match — message integrity verified (no alteration).");
        } else {
            System.out.println("\nHash mismatch — message was altered during transmission!");
        }

        sc.close();
    }
}
