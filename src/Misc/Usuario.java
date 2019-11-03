package Misc;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 7050891453829734452L;
	private int id = -1;
	private String nome;
	private String nomeLogin;
	private String senha;
	private boolean status = false;

	public Usuario(int id, String nome, boolean status) {
		this(id, nome);
		this.status = status;
	}
	
	public Usuario(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Usuario(String nomeLogin, String senha) {
		this.nomeLogin = nomeLogin;
		this.senha = senha;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getSenha() {
		return senha;
	}

	public boolean setOnline() {
		return this.status = true;
	}

	public boolean setOffline() {
		return this.status = false;
	}
	
	public boolean isOnline() {
		return this.status;
	}

	public String getNomeLogin() {
		return nomeLogin;
	}
}
