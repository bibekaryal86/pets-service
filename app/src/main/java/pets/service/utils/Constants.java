package pets.service.utils;

import java.util.List;

import static java.util.Arrays.asList;

public class Constants {

    /**
     * for SonarLint
     */
    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    protected static final String LOG_MASKER_PATTERN = "(.*?)";

    public static final String ACCOUNT_TYPE_ID_CASH = "5ede4cbb0525eb78290332e4";
    public static final String ACCOUNT_TYPE_ID_CHECKING = "5ede4cc80525eb78290332e5";
    public static final String ACCOUNT_TYPE_ID_CREDIT_CARD = "5ede4cf30525eb78290332e7";
    public static final String ACCOUNT_TYPE_ID_LOANS_MORTGAGES = "5ede4d080525eb78290332e8";
    public static final String ACCOUNT_TYPE_ID_OTHER_DEPOSITS = "5ede4d170525eb78290332e9";
    public static final String ACCOUNT_TYPE_ID_OTHER_LOANS = "5ede4d1d0525eb78290332ea";
    public static final String ACCOUNT_TYPE_ID_SAVINGS = "5ede4cde0525eb78290332e6";
    public static final String ACCOUNT_TYPE_ID_INVESTMENT = "5fa83f9d465347404cc6aa21";

    protected static final List<String> ACCOUNT_TYPES_DEPOSIT_ACCOUNTS = asList(ACCOUNT_TYPE_ID_CASH,
            ACCOUNT_TYPE_ID_CHECKING, ACCOUNT_TYPE_ID_OTHER_DEPOSITS, ACCOUNT_TYPE_ID_SAVINGS, ACCOUNT_TYPE_ID_INVESTMENT);

    protected static final List<String> ACCOUNT_TYPES_LOAN_ACCOUNTS = asList(ACCOUNT_TYPE_ID_CREDIT_CARD,
            ACCOUNT_TYPE_ID_LOANS_MORTGAGES, ACCOUNT_TYPE_ID_OTHER_LOANS);

    public static final String TRANSACTION_TYPE_ID_EXPENSE = "5ede664746fa58038df1b422";
    public static final String TRANSACTION_TYPE_ID_INCOME = "5ede663e46fa58038df1b421";
    public static final String TRANSACTION_TYPE_ID_TRANSFER = "5ede664e46fa58038df1b423";

    private static final String MERCHANT_ID_TRANSFER = "5f9f861c083c2023ef009a9a";
    private static final String MERCHANT_ID_FAMILY = "5fa0f0e97ed1a3304dee7caa";
    private static final String MERCHANT_ID_CASH_RECON = "5fa419dfb5e94e065bd9dad8";

    protected static final List<String> SYSTEM_DEPENDENT_MERCHANTS = asList(MERCHANT_ID_TRANSFER,
            MERCHANT_ID_FAMILY, MERCHANT_ID_CASH_RECON);

    public static final String CATEGORY_ID_REFUNDS = "5ede618546fa58038df1b3e8";
    private static final String CATEGORY_ID_AUTO_PAYMENT = "5fa8d3a4465347404cc6aa22";
    private static final String CATEGORY_ID_MORTGAGE_PAYMENT = "5fa8d3de465347404cc6aa23";
    private static final String CATEGORY_ID_OTHER_LOAN_PAYMENT = "5fa8d3ec465347404cc6aa24";

    public static final List<String> CATEGORY_ID_LOAN_PAYMENTS = asList(CATEGORY_ID_AUTO_PAYMENT,   // NOSONAR
            CATEGORY_ID_MORTGAGE_PAYMENT, CATEGORY_ID_OTHER_LOAN_PAYMENT);

    public static final String BASIC_AUTH_USR = "BASIC_AUTH_USR";
    public static final String BASIC_AUTH_PWD = "BASIC_AUTH_PWD";
    public static final String BASIC_AUTH_USR_PETSDATABASE = "BASIC_AUTH_USR_PETSDATABASE";
    public static final String BASIC_AUTH_PWD_PETSDATABASE = "BASIC_AUTH_PWD_PETSDATABASE";
}
