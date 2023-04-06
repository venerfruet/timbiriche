package br.com.vener.projetoJavaFx;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

public class PaneCell extends Pane {

	private int row;
	private int column;
	private String borders[];
	private String borderStyles;
	private PaneCell siameseCell;
	private String currentIdSiamese;

	public PaneCell(int row, int column) {

		// Cor padrão
		String dc = Enviroments.DEFAULT_COLOR;

		// Define o cursor da celula
		this.setCursor(Cursor.HAND);

		// Inicializa campos da celula
		this.row = row;
		this.column = column;
		borders = new String[] { dc, dc, dc, dc };
		borderStyles = "solid solid solid solid";
		currentIdSiamese = "";

		// Definição dos eventos da celula
		defineEvents();

		// Define as bordas iniciais da celula
		drawBorder(4);

	}

	private void defineEvents() {

		// Evento ao mover o mouse na celula
		this.setOnMouseMoved((e) -> {

			int left = (int) e.getX();// distância do lado esquerdo da celula
			int top = (int) e.getY();// distância do topo da celula
			int right = (int) (this.getBoundsInLocal().getMaxX() - e.getX());// distância do lado direito
			int botton = (int) (this.getBoundsInLocal().getMaxY() - e.getY());// distância da base

			// Retorna a borda mais próxima 0=topo, 1=direita, 2=base, 3=esquerda
			int edge = detectEdge(top, right, botton, left);

			// Destaca a borda mais próxima do cursor
			drawBorder(edge);

			// Retorna a celula siamesa e destaca sua mais próxima a celula atual
			siameseCell = detectSiameseCell(edge);

		});

		this.setOnMouseExited((e) -> {

			restoreBorder();
			drawBorder(4);
			restoreSiameseCell();

		});

	}

	private int detectEdge(int top, int right, int botton, int left) {

		// Tolerância para a distância da borda
		int tolerance = Enviroments.TOLERANCE;

		// Retorna a borda dentro da tolerância
		if (top <= tolerance)
			return 0;
		if (right <= tolerance)
			return 1;
		if (botton <= tolerance)
			return 2;
		if (left <= tolerance)
			return 3;

		return 4;

	}

	public void drawBorder(int edge) {
		// top right botton left

		String borderColors;
		String borderWidths = borderWidths();// Atribui tamanho das bordas atuais
		String highLightColor = Enviroments.HIGHLIGHT_COLOR;// Atribui cor de destaque
		String defaultColor = Enviroments.DEFAULT_COLOR;// Atribui cor padrão

		switch (edge) {
		case 0:
			borders[0] = highLightColor;
			borders[1] = defaultColor;
			borders[2] = defaultColor;
			borders[3] = defaultColor;
			break;
		case 1:
			borders[0] = defaultColor;
			borders[1] = highLightColor;
			borders[2] = defaultColor;
			borders[3] = defaultColor;
			break;
		case 2:
			borders[0] = defaultColor;
			borders[1] = defaultColor;
			borders[2] = highLightColor;
			borders[3] = defaultColor;
			break;
		case 3:
			borders[0] = defaultColor;
			borders[1] = defaultColor;
			borders[2] = defaultColor;
			borders[3] = highLightColor;
			break;
		default:
			restoreBorder();
		}

		borderColors = borderColors();// Atribui cores das bordas

		// Atribui CSS string
		String border = "" + "-fx-border-width:" + borderWidths + ";" + " -fx-border-color:" + borderColors + ";"
				+ " -fx-border-style:" + borderStyles + ";";

		// Atribui estilo à esta celula
		this.setStyle(border);

	}

	private PaneCell detectSiameseCell(int edge) {

		PaneCell cell = null;
		int siameseEdege = 4;
		String id = "";

		// Determina o id e a borda da celula siamesa mais próxima a borda desta celula
		switch (edge) {
		case 0:
			id = "#cell" + (row - 1) + column;
			siameseEdege = 2;
			break;
		case 1:
			id = "#cell" + row + (column + 1);
			siameseEdege = 3;
			break;
		case 2:
			id = "#cell" + (row + 1) + column;
			siameseEdege = 0;
			break;
		case 3:
			id = "#cell" + row + (column - 1);
			siameseEdege = 1;
			break;
		}

		// Caso o id seja diferente da celula siamesa anterior restaura a celula siamesa
		// anterior
		if (!currentIdSiamese.equals(id)) {
			restoreSiameseCell();
		}

		// Necessário para a verificação na mudança de borda
		currentIdSiamese = id;

		// Captura a celula siamesa
		cell = (PaneCell) this.getScene().lookup(id);

		// Destaca a borda da celula siamesa
		if (cell != null)
			cell.drawBorder(siameseEdege);

		// Retorna a celula siamesa
		return cell;

	}

	public void restoreBorder() {

		// Cor padrão
		String defaultColor = Enviroments.DEFAULT_COLOR;

		// Define todas as bordas com a cor padrão
		borders[0] = defaultColor;
		borders[1] = defaultColor;
		borders[2] = defaultColor;
		borders[3] = defaultColor;

	}

	private void restoreSiameseCell() {

		// Restaurando a celula siamesa
		if (siameseCell != null) {
			siameseCell.restoreBorder();
			siameseCell.drawBorder(4);
		}

	}

	private String borderWidths() {

		// Variaveis locais
		int maxRows = Enviroments.MAX_ROWS - 1;
		int maxColumns = Enviroments.MAX_COLUMNS - 1;
		int dbw = Enviroments.DEFAULT_BORDER_WIDTH;
		int dbw2 = dbw * 2;
		int borders[] = { dbw, dbw, dbw, dbw };
		String strBorders = "";

		// Define bordas externas com o dobro do tamanho
		if (row == 0)
			borders[0] = dbw2;
		if (row == maxRows)
			borders[2] = dbw2;
		if (column == 0)
			borders[3] = dbw2;
		if (column == maxColumns)
			borders[1] = dbw2;

		for (int it : borders) {
			strBorders += " " + it;
		}

		return strBorders;

	}

	private String borderColors() {

		String strBorders = "";

		// Monta o CSS string de cor das bordas
		for (String it : borders) {
			strBorders += " " + it;
		}

		return strBorders;

	}

}
