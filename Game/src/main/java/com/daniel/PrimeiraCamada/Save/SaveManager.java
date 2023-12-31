package com.daniel.PrimeiraCamada.Save;


import com.daniel.PrimeiraCamada.Entidades.Player;
import com.daniel.PrimeiraCamada.Exceptions.PlayerInexistenteException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
public class SaveManager {
    Path CaminhoSave;
    public SaveManager(){
        String CaminhoAppData = System.getenv("AppData");

        CaminhoSave = Paths.get(CaminhoAppData, "..","Local", "NomeProvisorio", "Saves", "Save.dat").normalize();
        System.out.println("Caminho do diretório a ser criado: " + CaminhoSave.getParent());
        try {
            Files.createDirectories(CaminhoSave.getParent());
        }
        catch (IOException e){
            System.out.println("Não foi possivel criar o diretorio");
        }
    }
    public void Salvar() throws PlayerInexistenteException {
        Save save = new Save(Player.getPlayer());
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CaminhoSave.toFile()));
            out.writeObject(save);
            System.out.println("Arquivo criado/atualizado com sucesso");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao salvar/atualizar arquivo");
        }
    }
    public void Carregar(){
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(CaminhoSave.toFile()));
            Save save = (Save) in.readObject();
            Player.setPlayer(save.player);
            System.out.println(Player.getPlayer().getName());
            System.out.println(save.player.getName());
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("Falha ao carregar arquivo");
        } catch (PlayerInexistenteException e) {
            throw new RuntimeException(e);
        }
    }
}
