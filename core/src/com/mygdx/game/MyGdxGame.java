package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {

	//Sprites e texturas
	private SpriteBatch batch;
	private Texture passaro;
	private Texture fundo;

	//Tamanho Celular
	private float larguraDispositivo;
	private float alturaDispositivo;

	//Movimentação
	private int movimentaY = 0;
	private int movimentaX = 0;
	
	@Override
	public void create () {
		//instancia uma nova texture
		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");
		fundo = new Texture("fundo.png");

		//ajusta o tamanho da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {
		//começo
		batch.begin();

		//Posicionamento da tela de fundo e do bird
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaro, 50, 50, movimentaX, movimentaY);

		//aumenta a variavel para a movimentação
		movimentaY++;
		movimentaX++;

		//fim
		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
