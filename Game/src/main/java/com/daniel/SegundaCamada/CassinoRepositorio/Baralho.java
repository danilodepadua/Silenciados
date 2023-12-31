package com.daniel.SegundaCamada.CassinoRepositorio;

import com.daniel.PrimeiraCamada.Cassino.Carta;
import com.daniel.PrimeiraCamada.Exceptions.BaralhoVazioException;

import java.util.ArrayList;
import java.util.Collections;

public class Baralho {
    private ArrayList<Carta> cartas = new ArrayList<>();

    public Baralho() {
    }

    public void embaralhar() {
        System.out.println("Tamanho do baralho: " + tamanho());
        Collections.shuffle(cartas);
    }

    public Carta pegarCarta() throws BaralhoVazioException {
        if (!cartas.isEmpty()) {
            return cartas.remove(cartas.size() - 1);
        } else {
            throw new BaralhoVazioException();
        }
    }


    public void reiniciarBaralho() {
        // Limpa e reinicia o baralho
        cartas.clear();
        // Adicione as cartas novamente
        criarBaralho("hearts");
        criarBaralho("clubs");
        criarBaralho("spades");
        criarBaralho("diamonds");
    }

    public boolean vazio() {
        return cartas.isEmpty();
    }

    public int tamanho() {
        return cartas.size();
    }

    public void criarBaralho(String naipe) {
        for (int valor = 2; valor <= 10; valor++) {
            String valorCarta = obterNomeCarta(valor);
            String caminho = "/com.daniel.Images/Cartas/" + valorCarta + "_of_" + naipe.toLowerCase() + ".png";
            System.out.println("Caminho da imagem: " + caminho);
            cartas.add(new Carta(valorCarta, naipe, valor, caminho));
        }

        // Adiciona Ás, Valete, Dama e Rei
        cartas.add(new Carta("Ás", naipe, 11, "/com.daniel.Images/Cartas/ace_of_" + naipe.toLowerCase() + ".png"));
        cartas.add(new Carta("Valete", naipe, 10, "/com.daniel.Images/Cartas/jack_of_" + naipe.toLowerCase() + ".png"));
        cartas.add(new Carta("Dama", naipe, 10, "/com.daniel.Images/Cartas/queen_of_" + naipe.toLowerCase() + ".png"));
        cartas.add(new Carta("Rei", naipe, 10, "/com.daniel.Images/Cartas/king_of_" + naipe.toLowerCase() + ".png"));

    }

    private String obterNomeCarta(int valor) {
        switch (valor) {
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
            case 10: return "ten";
            default: return String.valueOf(valor);
        }
    }

}
