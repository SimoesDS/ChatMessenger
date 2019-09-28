package Misc;

import java.io.Serializable;

public class Usuario implements Serializable {
  
  private int id = -1;
  private String nome;
  private String nomeLogin;
  private String senha;
  private boolean status;
  
  
  public Usuario (){
     
  }
  
  public Usuario (int id, String nome, String nomeLogin, String senha, boolean status) {
    this.id = id;
    this.nome = nome;
    this.nomeLogin = nomeLogin;
    this.senha = senha;
    this.status = status;
  }
  
  public Usuario (int id, String nome, boolean status) {
    this.id = id;
    this.nome = nome;
    this.status = status;
  }
  
  public Usuario (String nomeLogin, String senha) {
    this.nomeLogin = nomeLogin;
    this.senha = senha;
  }

  public int getId () {
    return id;
  }

  public void setId (int id) {
    this.id = id;
  }

  public String getNome () {
    return nome;
  }

  public void setNome (String nome) {
    this.nome = nome;
  }

  public String getSenha () {
    return senha;
  }

  public void setSenha (String senha) {
    this.senha = senha;
  }

  public boolean isOnline() {
    return status;
  }

  public String getNomeLogin() {
    return nomeLogin;
  }

  public void setNomeLogin(String nomeLogin) {
    this.nomeLogin = nomeLogin;
  }
  
  public void atualizaUser(Usuario user) {
    
	this.id = user.getId();
	this.nome = user.getNome();
	this.nomeLogin = user.getNomeLogin();
	this.senha = user.getSenha();
	this.status = user.isOnline();
  }
}
