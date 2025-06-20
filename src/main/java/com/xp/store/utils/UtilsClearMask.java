package com.xp.store.utils;

public class UtilsClearMask {

    public static String removeDocumentMask(String document) {
        String cleanDocument = document.replaceAll("[.\\-/]", "");
        if (cleanDocument.length() == 11) {
            return cleanDocument;
        } else if (cleanDocument.length() == 14) {
            return cleanDocument;
        } else {
            throw new IllegalArgumentException("Documento inválido. Deve ser CPF ou CNPJ.");
        }
    }
}
