package pl.malgorzata.galera;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.util.UUID;

public final class BankDataGenerator {

    private BankDataGenerator() {
        throw new UnsupportedOperationException(
                "This is a utility class and should not be instantiated.");
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String generateIban() {
        return Iban.random(CountryCode.PL).toString();
    }

    public static Double generateRandomAccountBalance() {
        return Math.round((3000 + Math.random() * 2000) * 100.0) / 100.0;
    }
}
