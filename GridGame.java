package br.com.vener.projetoJavaFx;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GridGame extends GridPane {

	private int columns;
	private int rows;

	public GridGame(int rows, int columns) {

		// Inicializa campos
		this.columns = columns;
		this.rows = rows;

		// Desenha a grade
		drawGrid();

		// Popula a grade com as celulas inteligentes
		populateGrid();

	}

	private void drawGrid() {

		// Tamanho padrão da celula, usado para largura e altura
		int size = Enviroments.DEFAULT_SIZE_CELL;

		// Define o tamanho das colunas
		for (int i = 0; i < columns; i++) {
			getColumnConstraints().add(new ColumnConstraints(size));
		}

		// Define o tamanho das linhas
		for (int i = 0; i < rows; i++) {
			getRowConstraints().add(new RowConstraints(size));
		}

	}

	private void populateGrid() {

		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < columns; row++) {
				PaneCell cell = new PaneCell(row, column);
				cell.setId("cell" + row + column);
				add(cell, column, row);
			}
		}

	}
}
