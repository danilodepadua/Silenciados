package com.daniel.PrimeiraCamada.Itens;

import com.daniel.PrimeiraCamada.Entidades.Player;
import com.daniel.PrimeiraCamada.Exceptions.PlayerInexistenteException;
import com.daniel.PrimeiraCamada.Interfaces.IEquipable;
import com.daniel.PrimeiraCamada.TiposDano;

public  class Arma extends Item implements IEquipable {
    protected int aumentoDeAtaqueFisico;
    protected int aumentoDeAtaqueMagico;
    protected TiposDano tipoDano;
    public Arma(){
        this.aumentoDeAtaqueFisico = 0;
        this.aumentoDeAtaqueMagico = 0;
        this.imagem = "/com.daniel.Images/Itens/FundoArma.png";
        this.nome = "";
        this.tipoDano = TiposDano.Fisico;
    }
    public int getAumentoDeAtaqueFisico() {
        return aumentoDeAtaqueFisico;
    }

    public int getAumentoDeAtaqueMagico() {
        return aumentoDeAtaqueMagico;
    }

    @Override
    public void equipar() throws PlayerInexistenteException {
        Player.getPlayer().desequiparArma();
        Player.getPlayer().equiparArma(this);
    }

    @Override
    public void desequipar() throws PlayerInexistenteException {
        Player.getPlayer().desequiparArma();
    }
    public TiposDano getTipoDano(){
        return this.tipoDano;
    }
}
