package br.com.vener.projetoJavaFx;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

public class PaneCell extends Pane {

	private int row;
	private int column;
	private String borders[];
	private PaneCell siameseCell;
	private String currentIdSiamese;
	private boolean[] fixedBorders;
	private int fixedCount;
	private String colorsPlayers[];

	public PaneCell(int row, int column) {

		// Cor padrão
		String dc = Enviroments.DEFAULT_COLOR;

		// Define o cursor da celula
		this.setCursor(Cursor.HAND);

		// Inicializa campos da celula
		this.row = row;
		this.column = column;
		colorsPlayers = new String[] { Enviroments.IA_COLOR, Enviroments.PLAYER_COLOR };
		fixedBorders = new boolean[5];
		borders = new String[] { dc, dc, dc, dc };
		currentIdSiamese = "";
		fixedCount = 0;

		// Definição dos eventos da celula
		defineEvents();

		// Define as bordas iniciais da celula
		drawColorCell(4, Enviroments.DEFAULT_COLOR);

	}

	private void defineEvents() {

		// Evento ao clicar na borda
		this.setOnMouseClicked((e) -> {

			int left = (int) e.getX();// distância do lado esquerdo da celula
			int top = (int) e.getY();// distância do topo da celula
			int right = (int) (this.getBoundsInLocal().getMaxX() - e.getX());// distância do lado direito
			int botton = (int) (this.getBoundsInLocal().getMaxY() - e.getY());// distância da base

			// Retorna a borda mais próxima 0=topo, 1=direita, 2=base, 3=esquerda
			int edge = detectEdge(top, right, botton, left);

			if (!fixedBorders[edge]) {

				// Aumenta bordas fixas
				if (edge < 4)
					fixedCount++;

				// Pinta a cor da borda
				drawColorCell(edge, colorsPlayers[JavaFxMain.currentColor]);

				// Fixa cor na borda
				fixedBorders[edge] = true;

				// Retorna a celula siamesa e destaca sua mais próxima a celula atual
				siameseCell = iterateSiameseCell(edge, colorsPlayers[JavaFxMain.currentColor], true);

				// Alterna jogador caso uma borda seja clicada e não fechada
				if (edge < 4) {
					if (siameseCell != null) {
						if (fixedCount < 4 && siameseCell.getFixedCount() < 4)
							JavaFxMain.currentColor = (JavaFxMain.currentColor + 1) % colorsPlayers.length;
					} else {
						if (fixedCount < 4)
							JavaFxMain.currentColor = (JavaFxMain.currentColor + 1) % colorsPlayers.length;
					}
				}
			}

		});

		// Evento ao mover o mouse na celula
		this.setOnMouseMoved((e) -> {

			int left = (int) e.getX();// distância do lado esquerdo da celula
			int top = (int) e.getY();// distância do topo da celula
			int right = (int) (this.getBoundsInLocal().getMaxX() - e.getX());// distância do lado direito
			int botton = (int) (this.getBoundsInLocal().getMaxY() - e.getY());// distância da base

			// Retorna a borda mais próxima 0=topo, 1=direita, 2=base, 3=esquerda
			int edge = detectEdge(top, right, botton, left);

			if (!fixedBorders[edge] && edge < 4) {
				// Destaca a borda mais próxima do cursor
				drawColorCell(edge, Enviroments.HIGHLIGHT_COLOR);

				// Retorna a celula siamesa e destaca sua mais próxima a celula atual
				siameseCell = iterateSiameseCell(edge, Enviroments.HIGHLIGHT_COLOR, false);
			}

		});

		this.setOnMouseExited((e) -> {

			// Normaliza borda destacada
			if (fixedCount < 4)
				drawColorCell(4, Enviroments.DEFAULT_COLOR);

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

	public void drawColorCell(int edge, String color) {
		// top right botton left

		String borderColors;
		// Atribui tamanho das bordas atuais
		String borderWidths = borderWidths();
		// Atribui cor padrão
		String defaultColor = Enviroments.DEFAULT_COLOR;
		// Cor de fundo
		String bgColor = "rgba(255,255,255,0)";

		switch (edge) {
		case 0:
			if (!fixedBorders[0])
				borders[0] = color;
			if (!fixedBorders[1])
				borders[1] = defaultColor;
			if (!fixedBorders[2])
				borders[2] = defaultColor;
			if (!fixedBorders[3])
				borders[3] = defaultColor;
			break;
		case 1:
			if (!fixedBorders[0])
				borders[0] = defaultColor;
			if (!fixedBorders[1])
				borders[1] = color;
			if (!fixedBorders[2])
				borders[2] = defaultColor;
			if (!fixedBorders[3])
				borders[3] = defaultColor;
			break;
		case 2:
			if (!fixedBorders[0])
				borders[0] = defaultColor;
			if (!fixedBorders[1])
				borders[1] = defaultColor;
			if (!fixedBorders[2])
				borders[2] = color;
			if (!fixedBorders[3])
				borders[3] = defaultColor;
			break;
		case 3:
			if (!fixedBorders[0])
				borders[0] = defaultColor;
			if (!fixedBorders[1])
				borders[1] = defaultColor;
			if (!fixedBorders[2])
				borders[2] = defaultColor;
			if (!fixedBorders[3])
				borders[3] = color;
			break;
		default:
			if (!fixedBorders[0])
				borders[0] = defaultColor;
			if (!fixedBorders[1])
				borders[1] = defaultColor;
			if (!fixedBorders[2])
				borders[2] = defaultColor;
			if (!fixedBorders[3])
				borders[3] = defaultColor;
		}

		// Atribui cores das bordas
		borderColors = borderColors();

		// Se 4 bordas fixadas pinta a celula
		if (fixedCount == 4)
			bgColor = color;

		// Atribui CSS string
		String border = "-fx-background-color:" + bgColor + ";  -fx-background-radius: 50; -fx-border-width:"
				+ borderWidths + "; -fx-border-color:" + borderColors + "; -fx-border-style: solid;";

		// Atribui estilo a celula
		this.setStyle(border);

	}

	private PaneCell iterateSiameseCell(int edge, String color, boolean fixed) {

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
		if (cell != null) {

			// Aumenta bordas fixas antes de definir a cor
			if (fixed)
				cell.setFixedCount(cell.getFixedCount() + 1);

			// define a cor
			cell.drawColorCell(siameseEdege, color);

			// Fixa borda após definir a cor
			if (fixed)
				cell.setFixedBorders(siameseEdege);

		}

		// Retorna a celula siamesa
		return cell;

	}

	private void restoreSiameseCell() {

		// Restaurando a celula siamesa
		if (siameseCell != null) {
			if (siameseCell.getFixedCount() < 4)
				siameseCell.drawColorCell(4, Enviroments.DEFAULT_COLOR);
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

	public void setFixedBorders(int edge) {
		fixedBorders[edge] = true;
	}

	public int getFixedCount() {
		return fixedCount;
	}

	public void setFixedCount(int fixedCount) {
		this.fixedCount = fixedCount;
	}

}
