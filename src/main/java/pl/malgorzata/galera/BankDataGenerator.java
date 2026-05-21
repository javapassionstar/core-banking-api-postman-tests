package pl.malgorzata.galera;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import java.util.UUID;

public final class BankDataGenerator {

    // Prywatny konstruktor blokuje możliwość stworzenia obiektu tej klasy (częsta praktyka dla klas Utility)
    private BankDataGenerator() {
        throw new UnsupportedOperationException("To jest klasa narzędziowa i nie można tworzyć jej instancji.");
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
