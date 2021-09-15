SELECT
CUS.codprod,
CUS.codemp,
CUS.codlocal,
TO_CHAR(CUS.controle) AS CONTROLE,
CUS.dtatual,
CUS.CUSREP,
CUS.dtatual AS ULTDT
FROM TGFCUS CUS
WHERE
CUS.DTATUAL = (
    SELECT MAX(C.DTATUAL) FROM TGFCUS C WHERE C.CODPROD=CUS.CODPROD
    AND C.CODEMP=CUS.CODEMP AND C.CONTROLE=CUS.CONTROLE
    AND C.DTATUAL>=CUS.DTATUAL
)

ORDER BY CODEMP, CODPROD, CODLOCAL, CONTROLE