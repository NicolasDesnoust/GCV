package gcv.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.math.BigInteger;

/**Hachage salé de mots de passe via PBKDF2.
 * 
 * Adapté à partir de : https://gist.github.com/jtan189/3804290
 * 
 * @author havoc AT defuse.ca
 * 
 * @see <a href="http://crackstation.net/hashing-security.htm">http://crackstation.net/hashing-security.htm</a>
 */
public class PasswordHash {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    public static final int SALT_BYTES = 24;
    public static final int HASH_BYTES = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    /**
     * Renvoie un hachage salé de type PBKDF2 du mot de passe.
     *
     * @param password Le mot de passe à hacher.
     * @return Un hachage salé de type PBKDF2 du mot de passe.
     */
    public static String createHash(String password)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    /**
     * Renvoie un hachage salé de type PBKDF2 du mot de passe.
     *
     * @param password Le mot de passe à hacher.
     * @return Un hachage salé de type PBKDF2 du mot de passe.
     */
    public static String createHash(char[] password)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Génère un sel aléatoire
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);

        // Hachage du mot de passe
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
        // formatage itérations:sel:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
    }

    /**
     * Valide un mot de passe en utilisant un hash.
     *
     * @param password Le mot de passe à vérifier.
     * @param goodHash Le hash du vrai mot de passe.
     * @return true si le mot de passe est correct, false sinon.
     */
    public static boolean validatePassword(String password, String goodHash)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        return validatePassword(password.toCharArray(), goodHash);
    }

    /**
     * Valide un mot de passe en utilisant un hash.
     *
     * @param password Le mot de passe à vérifier.
     * @param goodHash Le hash du vrai mot de passe.
     * @return true si le mot de passe est correct, false sinon.
     */
    public static boolean validatePassword(char[] password, String goodHash)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Decode the hash into its parameters
        String[] params = goodHash.split(":");
        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        byte[] salt = fromHex(params[SALT_INDEX]);
        byte[] hash = fromHex(params[PBKDF2_INDEX]);
        // Compute the hash of the provided password, using the same salt, 
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Compare deux tableaux de bytes en un temps proportionnel à leur longueur.
     * Cette méthode de comparaison est utilisée pour que les hashs de mots de passe
     * ne puissent pas être extraits à partir d'un système en ligne en utilisant une
     * attaque temporelle puis une attaque hors-ligne.
     * 
     * @param a Le premier tableau de bytes.
     * @param b Le second tableau de bytes.
     * @return true si les deux tableaux sont égaux, false sinon.
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     *  Calcul le hash de type PBKDF2 d'un mot de passe.
     *
     * @param password Le mot de passe à hacher.
     * @param salt Le sel.
     * @param iterations Le nombre d'itérations (facteur de lenteur).
     * @param bytes La longueur du hash à calculer en bytes.
     * @return Le hash de type PBKDF2 du mot de passe.
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converti une chaîne de caractères haxadécimaux en un tableau de bytes.
     *
     * @param hex La chaîne de caractères haxadécimaux.
     * @return La chaîne de caractères haxadécimaux décodée dans un tableau de bytes.
     */
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    /**
     * Converti un tableau de bytes en une chaîne de caractères haxadécimaux.
     *
     * @param array Le tableau de bytes à convertir.
     * @return une chaîne de caractères de longueur double codant le tableau de bytes.
     */
    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
}

