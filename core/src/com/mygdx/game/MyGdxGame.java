package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	//Texturas, bg, player, cenario
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoTopo;
	private Texture canoBaixo;

	//Movimentação
	private int movimentaY = 0;
	private int movimentaX = 0;

	private int pontos = 0;
	private int estadoJogo = 0;

	//Tamnho dispositivo
	private float larguraDispositivo;
	private float alturaDispositivo;

	//Float para mudar a sprite do Player
	private float variacao = 0;

	//Variaveis que se explicam por si só
	private int gravidade = 0;
	private float posicaoInicialVerticalPassaro = 0;
	private float posicaoCanohorizontal;
	private float espacoEntreCanos;
	private float posicaoCanoVertical;
	private Random random;

	//Texto da pontuação
	BitmapFont textoPontuacao;

	//Check se passou entre os canos
	private boolean passouCano = false;

	//Shape renderer
	private ShapeRenderer shapeRenderer;

	//Colisor do Player
	private Circle circuloPassaro;

	//Colisores dos canos em forma de Retangulo
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;


	@Override
	public void create () {

		inicializarTexturas();
		inicializarObjetos();
	}

	private void inicializarTexturas() {
		//Objc das Textures
		batch = new SpriteBatch();
		random = new Random();

		//Set texturas
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");


		//Background
		fundo = new Texture("fundo.png");

		//Texturas dso Canos
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");

	}

	private void inicializarObjetos() {
		//Referencia do tamanho do dispositivo
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

		//Set o spawn no meio da tela
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;

		//Set o spawn dos canos na direita
		posicaoCanohorizontal = larguraDispositivo;
		espacoEntreCanos = 350;

		//Starta o txt de pontuação
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);


		shapeRenderer = new ShapeRenderer();

		circuloPassaro = new Circle();

		retanguloCanoCima = new Rectangle();
		retanguloCanoBaixo = new Rectangle();
	}

	private void detectarColisao() {

		//Colocando o colider no Player
		circuloPassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

		//Colocando o Colider do cano de cima
		retanguloCanoCima.set(posicaoCanohorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
				canoTopo.getWidth(), canoTopo.getHeight());

		//Colocando o colider do cano de baixo
		retanguloCanoBaixo.set(posicaoCanohorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoBaixo.getWidth(), canoBaixo.getHeight());

		boolean colisaoCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean colisaoCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if(colisaoCanoBaixo || colisaoCanoCima) {
			estadoJogo = 2;
		}
	}


	private void verificarEstadoJogo() {

		if(estadoJogo == 0) {
			//JUMP
			if(Gdx.input.justTouched()) {
				gravidade = -25;
				estadoJogo = 1;
			}
		} else if (estadoJogo == 1) {
			if(Gdx.input.justTouched()) {
				gravidade = -25;
			}
		}

		//Mov dos canos
		posicaoCanohorizontal -= Gdx.graphics.getDeltaTime() * 200;
		if(posicaoCanohorizontal < - canoBaixo.getWidth()){
			posicaoCanohorizontal = larguraDispositivo;
			posicaoCanohorizontal = random.nextInt(400) - 200;
			passouCano = false;
		}
		boolean toqueTela = Gdx.input.justTouched();
		//Gravidade atuando no Player
		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		//Animation
		variacao += Gdx.graphics.getDeltaTime() * 10;


		//Limitando a variação do Player
		if(variacao > 3)
			variacao = 0;

		gravidade++;
		movimentaX++;
	}



	private void validarPontos() {
		if(posicaoCanohorizontal < 50 - passaros[0].getWidth()) {
			if(!passouCano) {
				pontos++;
				passouCano = true;
			}
		}
	}

	private void desenharTexturas() {
		//Inicio do Render
		batch.begin();
		//Setando o desenho e a position do dispositivo
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);
		//		Desenho canos
		batch.draw(canoBaixo, posicaoCanohorizontal - 100, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanohorizontal - 100, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);
		// Desenho pontos tela
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

		//fim do rneder

		batch.end();


	}



	@Override
	public void render () {
		verificarEstadoJogo();
		validarPontos();
		desenharTexturas();
		detectarColisao();
	}
	
	@Override
	public void dispose () {

	}
}
