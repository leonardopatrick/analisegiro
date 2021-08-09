package br.com.sankhya.commercial.analisegiro.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static final BigDecimal CEM_VALUE = new BigDecimal("100");

	public static BigDecimal getValueOrZero(BigDecimal b) {
		return b==null ? BigDecimal.ZERO : b;
	}
}
