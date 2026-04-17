package com.example.HireMeBot_Backend.Utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidUtils {

    /**
     * Converts a UUID to a byte array (16 bytes) for storage in BINARY(16) columns
     * 
     * @param uuid The UUID to convert
     * @return byte array representation of the UUID
     */
    public static byte[] uuidToBytes(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    /**
     * Converts a byte array (16 bytes) back to UUID
     * 
     * @param bytes The byte array to convert (must be 16 bytes)
     * @return UUID object
     * @throws IllegalArgumentException if bytes is not 16 bytes long
     */
    public static UUID bytesToUuid(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length != 16) {
            throw new IllegalArgumentException("UUID byte array must be exactly 16 bytes");
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }

    /**
     * Converts a UUID string to byte array
     * 
     * @param uuidString String representation of UUID (e.g., "550e8400-e29b-41d4-a716-446655440000")
     * @return byte array representation
     * @throws IllegalArgumentException if string is not a valid UUID
     */
    public static byte[] stringToBytes(String uuidString) {
        if (uuidString == null) {
            return null;
        }
        return uuidToBytes(UUID.fromString(uuidString));
    }

    /**
     * Converts byte array to UUID string
     * 
     * @param bytes The byte array to convert (must be 16 bytes)
     * @return String representation of UUID
     */
    public static String bytesToString(byte[] bytes) {
        UUID uuid = bytesToUuid(bytes);
        return uuid != null ? uuid.toString() : null;
    }

    /**
     * Generates a new random UUID as byte array
     * 
     * @return new random UUID as byte array
     */
    public static byte[] generateUuidBytes() {
        return uuidToBytes(UUID.randomUUID());
    }

    /**
     * Generates a new random UUID
     * 
     * @return new random UUID
     */
    public static UUID generateUuid() {
        return UUID.randomUUID();
    }
}
