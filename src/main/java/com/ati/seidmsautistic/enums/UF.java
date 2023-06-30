package com.ati.seidmsautistic.enums;

public enum UF {
    AC("Acre", "Rio Branco"), AL("Alagoas", "Maceió"), AM("Amazonas", "Manaus"), AP("Amapá",
            "Macapá"),
    BA("Bahia", "Salvador"), CE("Ceará", "Fortaleza"), DF("Distrito Federal",
            "Brasília"),
    ES("Espírito Santo", "Vitória"), GO("Goiás", "Goiânia"), MA("Maranhão",
            "São Luís"),
    MG("Minas Gerais", "Belo Horizonte"), MS("Mato Grosso do Sul",
            "Campo Grande"),
    MT("Mato Grosso", "Cuiabá"), PA("Pará", "Belém"), PB("Paraíba",
            "João Pessoa"),
    PE("Pernambuco", "Recife"), PI("Piauí",
            "Teresina"),
    PR("Paraná", "Curitiba"), RJ("Rio de Janeiro",
            "Rio de Janeiro"),
    RN("Rio Grande do Norte", "Natal"), RO("Rondônia",
            "Porto Velho"),
    RR("Roraima", "Boa Vista"), RS(
            "Rio Grande do Sul", "Porto Alegre"),
    SC("Santa Cataria",
            "Florianópolis"),
    SE("Sergipe", "Aracaju"), SP(
            "São Paulo", "São Paulo"),
    TO("Tocantins", "Palmas");

    String state;
    String capital;

    private UF(String state, String capital) {
        this.state = state;
        this.capital = capital;
    }
}
