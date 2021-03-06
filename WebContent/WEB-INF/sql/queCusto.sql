WITH CUSTO AS (
    SELECT
        CUS.CODPROD    AS CODPROD,
        CUS.CODEMP     AS CODEMP,
        CUS.CODLOCAL   AS CODLOCAL,
        CUS.CONTROLE   AS CONTROLE,
        CUS.DTATUAL    AS DTATUAL,
        CUS.CUSREP     AS CUSREP,
        MAX(CUS.DTATUAL) OVER(
            PARTITION BY CUS.CODPROD, CUS.CODEMP, CUS.CODLOCAL, CUS.CONTROLE
                ORDER BY CUS.CODPROD, CUS.CODEMP, CUS.CODLOCAL, CUS.CONTROLE
        ) ULTDT
    FROM
        TGFCUS CUS
)
SELECT
    CODPROD,
    CODEMP,
    CODLOCAL,
    CONTROLE,
    CUSREP,
    ULTDT,
    DTATUAL
FROM
    CUSTO
WHERE
    DTATUAL = ULTDT