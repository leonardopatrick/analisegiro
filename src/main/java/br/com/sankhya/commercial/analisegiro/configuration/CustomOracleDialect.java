package br.com.sankhya.commercial.analisegiro.configuration;

import org.hibernate.dialect.Oracle10gDialect;

public class CustomOracleDialect extends Oracle10gDialect {

    @Override
    public String getQuerySequencesString() {
        StringBuffer seq = new StringBuffer();
        seq.append("SELECT SEQUENCE_OWNER, SEQUENCE_NAME, ");
        seq.append(" greatest(MIN_VALUE,-9223372036854775807) MIN_VALUE,");
        seq.append("      Least(MAX_VALUE, 9223372036854775808) MAX_VALUE, ");
        seq.append("        INCREMENT_BY,CYCLE_FLAG, ORDER_FLAG, CACHE_SIZE, ");
        seq.append(" Least(greatest(LAST_NUMBER, -9223372036854775807), ");
        seq.append(" 9223372036854775808) LAST_NUMBER from ALL_SEQUENCES ");
        return seq.toString();
    }
}